# Multi-stage build image for frontend

### BUILD STEP ###
FROM openjdk:8-slim as builder
ENV APP_HOME=/usr/app

# This is a trick from Sairam Krish on Stack Overflow. If we just build the
# gradle file, we have to redownload all the dependencies when a single file
# changes. We don't care about rebuilding those dependencies every single time
# unless we add or change one. So if we run it now, it will fail, but all the
# dependencies will be there. We stop everything from failing by `return`ing 0
# to force a success. Any format failures in the build file will be caught when
# we pull the rest of the source code.
#
# https://stackoverflow.com/questions/25873971/docker-cache-gradle-dependencies
WORKDIR $APP_HOME
COPY build.gradle settings.gradle gradlew $APP_HOME/
COPY gradle $APP_HOME/gradle
COPY package.json package-lock.json $APP_HOME/
RUN ./gradlew --no-daemon npmInstall

# Now copy everything else and build it as a separte step.
COPY . .
RUN ./gradlew --no-daemon build -PassumeDeps

### RUN STEP ###
FROM node:14.0.0
ENV APP_HOME=/usr/app

# Copy all the JAR file over
COPY --from=builder --chown=node:node $APP_HOME $APP_HOME
EXPOSE 3000
WORKDIR $APP_HOME
ENTRYPOINT ["npm", "run", "server"]
