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

# Run oracle dbms.
docker run -d --network=host --name comp322-oracle-dbms container-registry.oracle.com/database/enterprise:latest

# Run the application server.
docker run -d --network=host --name comp322-library-manager comp322-library-manager:latest
