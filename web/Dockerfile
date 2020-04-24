# Initializes a back-end service with a SpringBoot app based on
# https://github.com/spring-guides/gs-spring-boot-docker

# Use JDK 8 image
FROM openjdk:8-jdk-alpine

# By default, Docker runs apps as a root user in the container. We'll create a
# separate one just for the app.
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy all the JAR file over
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# Start the main class on container start.
ENTRYPOINT ["java","-jar","/app.jar"]
