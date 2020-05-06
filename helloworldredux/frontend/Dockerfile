FROM node:14 as node

COPY --chown=node:node . /home/node
WORKDIR /home/node
RUN ["npm", "install"]
RUN ["npm", "run", "build"]

FROM nginx:1.17
COPY --from=node --chown=nginx:nginx /home/node/build /usr/share/nginx/html
