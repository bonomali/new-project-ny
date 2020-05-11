# Multi-stage build image for Spring Boot applications.

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
RUN ./gradlew --no-daemon build || echo "Build failed...this is expected."

# Now copy everything else and build it as a separte step.
COPY . .
RUN ./gradlew --no-daemon build

### RUN STEP ###
FROM openjdk:8-slim
ENV APP_HOME=/usr/app

# By default, Docker runs apps as a root user in the container. We'll create a
# separate one just for the app.
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring
WORKDIR $APP_HOME

# Copy all the JAR file over
COPY --from=builder --chown=spring:spring $APP_HOME/build/libs/*.jar app.jar

# Start the main class on container start.
ENTRYPOINT ["java","-jar","app.jar"]
