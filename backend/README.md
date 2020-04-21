# backend

A Spring Boot app

## Build and run

```bash
./gradlew build
docker build --tag=backend:1.0 .
docker run --publish 8080:8080 --name backend backend:1.0
```

Open [http://localhost:8080](http://localhost:8080) with your browser to see the result.
