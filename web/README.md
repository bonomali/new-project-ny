# web

A Spring Boot app with a React UI. Starter code generated from https://start.spring.io.

Spring Boot code is under `src`. React code is under `frontend`. Upon compilation, `frontend` code will be packaged with the Spring Boot app into a JAR for deployment.

## Build

We have two build configurations that can be used either for quick development
builds or hermetic production builds.

### Dev builds

Builds on your machine and utilizes your local build cache before loading the
result into Docker.

```bash
gradle build
docker build --tag=google-org/new-project-ny-web:dev --file dev.Dockerfile .
```

### Prod builds

Copies all source to the Docker container, builds there, then generates the
image.

```bash
docker build --tag=google-org/new-project-ny-web:prod .
```

## Run

```bash
VERSION=(dev|prod)
docker run --publish 8080:8080 --detach --name web google-org/new-project-ny-web:$VERSION
```

Open [http://localhost:8080](http://localhost:8080) with your browser to see
the result. To shut down the container run

```bash
docker rm --force web
```

## Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/gradle-plugin/reference/html/)

## Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)
