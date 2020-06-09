FROM node:current-alpine

WORKDIR /src

ADD package.json package.json
ADD yarn.lock yarn.lock
RUN yarn install

ADD . /src

ENTRYPOINT yarn start
