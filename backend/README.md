# backend

A Spring Boot app. Starter code generated from https://start.spring.io

## Build and run

```bash
./gradlew build
docker build --tag=backend:1.0 .
docker run --publish 8080:8080 --name backend backend:1.0
```

Open [http://localhost:8080](http://localhost:8080) with your browser to see the result.

## Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/gradle-plugin/reference/html/)

## Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)
