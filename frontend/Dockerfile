# Initializes a front-end service with a simple Next.js/React app

# Use the official image as a parent image.
FROM node:current-slim

# By default, Docker runs apps as a root user in the container. We'll create a
# separate one just for the app.
RUN addgroup --system nextjs && adduser --system nextjs --ingroup nextjs
USER nextjs:nextjs
WORKDIR /home/nextjs

# Copy the package details to the container and install dependencies
COPY --chown=nextjs:nextjs package.json .
RUN ["npm", "install"]

# This is where the Next.js app runs, so we want to expose that.
EXPOSE 3000

# Copy the rest of the source and build
# `npm run` is a Next.js specific script
COPY --chown=nextjs:nextjs . .
RUN ["npm", "run", "build"]

# When the container is run, start the app
CMD ["npm", "run", "start"]
