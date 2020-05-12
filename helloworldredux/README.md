# Google.org Fellowship Starter

A starter app for the Google.org fellowship team. The app here is built based
on the team's understanding of ITS's preferred infrastructure. The app contains:

  * An [Express.js](http://expressjs.com/) server with a
    [React.js](https://reactjs.org/) frontend.
  * A [Spring Boot](https://spring.io/projects/spring-boot) server with a basic
    REST service.
  * A [MongoDB](https://www.mongodb.com/) server.
  * Kubernetes configuration to manage the deployment of the three services with
    an ingress controller.
  * A [Tilt](https://tilt.dev) configuration for setting up a local dev
    environment.

## Prerequisites

  * A local Kubernetes cluster
    * Mac: [Docker for Mac](https://docs.docker.com/docker-for-mac/install/)
    * Linux: [minikube](https://minikube.sigs.k8s.io/docs/start/)
  * [Tilt](https://docs.tilt.dev/install.html)

## Building and running

### Start your Kubernetes environment

#### Mac

TBD

#### Linux

```bash
minikube start
```

### Start the app

```
tilt up
```

Then get the IP address for the app and connect to it on port 80

#### Mac

TBD

#### Linux

```bash
minikube ip
```
