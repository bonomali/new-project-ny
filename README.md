# new-project-ny

NOTE: This is not an officially supported Google product.

This project is a simple web app running with Spring Boot, React, MongoDB,
and Docker. It includes a proof of concept starter app as well as a more
detailed app implementation based on the starter.

For more details on specific processes or systems, see the wiki.

## Pre-requisites

* [Docker (19.03.8 or higher)](https://docs.docker.com/get-started/#download-and-install-docker-desktop)
* [kubectl (1.18.2 or
  higher)](https://kubernetes.io/docs/tasks/tools/install-kubectl)
* [minikube (1.9.2 or
  higher)](https://kubernetes.io/docs/tasks/tools/install-minikube/) (optional)
* [tilt.dev](https://tilt.dev/)
* Java 8
* Node (current)

Note: minikube is not required on platforms, such as OSX, that support Docker
Desktop (as opposed to Docker Engine). Docker Desktop supports Kubernetes and
kubectl out of the box.

## Building and running

For local development, the easiest way to build and run is via tilt.dev. For
example:

```
> cd src
src> tilt up
```

You can also build and run the api by:

```
> cd src/api
src/api> ./gradlew build
src/api> ./gradlew start
```

and the web by:

```
> cd src/web
src/web> yarn build
src/web> yarn start
```

If ever builds don't seem to be working exactly right, try cleaning your build
directories with:

```
> git clean -dfX
```

## Viewing logs

To view logs:

```
> kubectl get pods
NAME                         READY   STATUS    RESTARTS   AGE
database-0                    1/1     Running   0          61s
web-6574b66989-lpjpr     1/1     Running   0          61s
> kubectl logs -f web-6574b66989-lpjpr
# logs begin streaming from the selected container
```

## Running tests

To run api tests:

```
> cd src/api
src/api> ./gradlew test
```

To run web tests:

```
> cd src/web
src/web> yarn test
```

# Starter

The app here is built based on the team's understanding of NYS ITS's
preferred infrastructure. The app contains:

  * An [Express.js](http://expressjs.com/) server with a
    [React.js](https://reactjs.org/) app.
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

When using docker-desktop as your local kubernetes cluster, you can start the
application by navigating to `http://localhost`. Depending on your computer's
proxy configuration, you may need to open the application in an Incognito
window.

When using minikube as your local kubernetes cluster, you will need to navigate
to the specific IP address for the application. You can find the address via:

```
kubectl get ingress -o go-template --template="{{(index .items 0).status.loadBalancer.ingress}}"
```

If this doesn't return anything then you may have not enabled the ingress
controller above.

### Access Mongo shell

Once the app has started:

```
# open a bash shell in the mongo pod
kubectl exec -it database-0 sh

# start the mongo CLI. replace <password> with the value in api/k8s.yml
mongo -u root -p <password>

# print all documents to console, confirming a successful CLI session
use nyst
db.reservation.find()
```

### Access the HAL Explorer

The [HAL Explorer](https://github.com/toedter/hal-explorer) provides a user
interface for interacting with the REST API. You can view the UI by
navigating to `http://<hostname>/api/v1/`.
