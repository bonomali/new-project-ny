# Build and run a back-end service with a SpringBoot app based on
# https://github.com/spring-guides/gs-spring-boot-docker

### BUILD STEP ###
FROM gradle:jdk8 as builder

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build


### RUN STEP ###
FROM openjdk:8-slim

# By default, Docker runs apps as a root user in the container. We'll create a
# separate one just for the app.
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Copy all the JAR file over
ARG JAR_FILE=build/libs/*.jar
COPY --from=builder /home/gradle/src/build/libs/*.jar app.jar

# Start the main class on container start.
ENTRYPOINT ["java","-jar","/app.jar"]
