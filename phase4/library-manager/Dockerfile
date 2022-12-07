FROM node:19.1.0-slim

ARG NODE_ENV_ARG="development"
ENV NODE_ENV="${NODE_ENV_ARG}"

# Copy Oracle-related files.
COPY ./oracle-instantclient /opt/oracle
ENV LD_LIBRARY_PATH="/opt/oracle"

RUN DEBIAN_FRONTEND=noninteractive apt-get update && apt-get install -yq libaio-dev

WORKDIR /usr/src/app
COPY package*.json ./
RUN npm install --only="production"
COPY . .
CMD [ "npm", "start" ]
