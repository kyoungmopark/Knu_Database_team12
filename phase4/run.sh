#!/bin/bash

set -o errexit -o nounset -o noglob -o pipefail

if [ "${EUID}" != 0 ]; then
	echo "This script requires root privilege! Please try to use 'sudo'." >&2
	exit 1
fi

# Pull oracle image from the oracle container registry.
# Note that you might need to make some kind of agreement on the oracle website.
# See: https://container-registry.oracle.com/ords/f?p=113:4:13898099368784:::::
echo "Login to oracle container registry using your oracle ID and password."
docker login container-registry.oracle.com
docker pull container-registry.oracle.com/database/enterprise:latest

# Build our application server.
# Note that the application server will use port 3000 by default.
(cd ./library-manager && docker build . --build-arg NODE_ENV_ARG=production -t comp322-library-manager:latest)

# Run Oracle DBMS.
docker rm -f comp322-oracle-dbms || :
docker run -d --network=host --env ORACLE_PWD="password" --name comp322-oracle-dbms container-registry.oracle.com/database/enterprise:latest
until [[ "$(docker inspect -f {{.State.Health.Status}} comp322-oracle-dbms)" == "healthy" ]]; do
	echo "Wait until the Oracle DBMS is ready. It may take a while!"
	sleep 1;
done

# Create an Oracle DBMS user.
echo "create user university identified by comp322;" \
	| docker exec -i comp322-oracle-dbms sqlplus sys/password@orclpdb1 as sysdba
echo "grant all privileges to university;" \
	| docker exec -i comp322-oracle-dbms sqlplus sys/password@orclpdb1 as sysdba

# Copy SQL scripts into the Oracle DBMS container.
docker cp ../create_table.sql comp322-oracle-dbms:/home/oracle
docker cp ../insert.sql comp322-oracle-dbms:/home/oracle

# Execute the SQL scripts inside the Oracle DBMS container.
echo "@create_table" | docker exec -i comp322-oracle-dbms sqlplus university/comp322@orclpdb1
echo "@insert" | docker exec -i comp322-oracle-dbms sqlplus university/comp322@orclpdb1

# Run the application server.
docker rm -f comp322-library-manager || :
docker run -d --network=host --name comp322-library-manager comp322-library-manager:latest

echo "Oracle DBMS and NodeJS application server are up and running!"
