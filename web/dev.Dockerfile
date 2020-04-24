# Run a back-end service with a locally built SpringBoot app based on
# https://github.com/spring-guides/gs-spring-boot-docker
#
# This is what you would want to run to quickly test changes. If you're looking
# to prepare an image for production use, build with `Dockerfile`.

FROM openjdk:8-slim

# By default, Docker runs apps as a root user in the container. We'll create a
# separate one just for the app.
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Copy all the JAR file over
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# Start the main class on container start.
ENTRYPOINT ["java","-jar","/app.jar"]
