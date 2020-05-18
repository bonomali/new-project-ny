# Call Me Back

Prototype project allowing for NYS residents to contact a customer service
representative. The app here is built based on the team's understanding of ITS's
preferred infrastructure. The app contains:

  * An [Express.js](http://expressjs.com/) server with a
    [React.js](https://reactjs.org/) frontend.
  * A [Spring Boot](https://spring.io/projects/spring-boot) server with a basic
    REST service.
  * A [MongoDB](https://www.mongodb.com/) server.
  * Kubernetes configuration to manage the deployment of the three services with
    an [Nginx Ingress Controller](https://kubernetes.github.io/ingress-nginx/).
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

Enabling Kubernetes in Docker (as in the Tilt instructions) allows this to work. Nothing additional needs to be run.

You may need to set Docker as your Kubernetes context:

```sh
kubectl config set-context docker-desktop
```

#### Linux

```bash
minikube start
```

### Enable Ingress Functionality

#### Mac

Follow the instructions for [Allowing Ingress in Docker](https://kubernetes.github.io/ingress-nginx/deploy/#docker-for-mac)

Run:

```
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-0.32.0/deploy/static/provider/cloud/deploy.yaml
```

This may need to be re-run if you blow up your Kubernetes configuration.

#### Linux

```
minikube addons enable ingress
```

### Start the app

```
tilt up
```

Then get the IP address for the app and connect to it on port 80:
```
kubectl describe ingress
```

## Known Issues

* The HAL Browser redirect doesn't quite work properly if you go to `/api/v1`.
  * Workaround: Go to `/api/v1/browser/index.html`.
  * More reading:
    https://stackoverflow.com/questions/41116262/the-hal-browser-doesnt-get-autoconfigured-correctly-in-spring-data-rest
