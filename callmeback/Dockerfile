# Build and run a back-end service with a SpringBoot app based on
# https://github.com/spring-guides/gs-spring-boot-docker
#
# This is what you would want to run for a clean build and deployment. If you're
# looking for running a quicker version based on your local environment, build
# with `dev.Dockerfile`

### BUILD STEP ###
FROM gradle:jdk8 as builder

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN ./gradlew bootJar


### RUN STEP ###
FROM openjdk:8-slim

# By default, Docker runs apps as a root user in the container. We'll create a
# separate one just for the app.
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring
WORKDIR /home/spring

# Copy all the JAR file over
ARG JAR_FILE=build/libs/*.jar
COPY --from=builder --chown=spring:spring /home/gradle/src/${JAR_FILE} app.jar

# Start the main class on container start.
ENTRYPOINT ["java","-jar","app.jar"]
