FROM node:14 AS node
COPY --chown=node:node . /home/node
WORKDIR /home/node
RUN ["npm", "run", "build"]

FROM nginx:1.17
COPY --from=node --chown=nginx:nginx /home/node/build /usr/share/nginx/html
