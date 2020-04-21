# Initializes a front-end service with a simple Java app.

# Pull the image from JDK's Docker Hub image (https://hub.docker.com/_/openjdk)
FROM openjdk:14.0.1-slim

# By default, Docker runs apps as a root user in the container. We'll create a
# separate one just for the app.
RUN addgroup --system jvm && adduser --system jvm --ingroup jvm
USER jvm:jvm
WORKDIR /home/jvm

# Copy the compiled app to the container. Note that this assumes the JAR file
# has already been built, so to run this app would require you to first build
# the app and then build the container.
COPY build/distributions/backend.tar .

# Extract (-x) the tar file (-f) backend.tar.
RUN ["tar", "-xf", "backend.tar"]

# On startup, run the startup script. This will also pass any additional params.
ENTRYPOINT ["/home/jvm/backend/bin/backend"]
