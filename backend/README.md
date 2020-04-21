# backend

A simple Gradle app (eventually with Spring Boot)

## Build and run

```sh
./gradlew build
docker build --tag backend:1.0 .
docker run --name backend backend:1.0
```
