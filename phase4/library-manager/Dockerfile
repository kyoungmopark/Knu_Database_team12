FROM node:19.1.0-slim

ARG NODE_ENV_ARG="development"
ENV NODE_ENV="${NODE_ENV_ARG}"

WORKDIR /usr/src/app
COPY package*.json ./
RUN npm clean-install --only="production"
COPY . .
CMD [ "npm", "start" ]
