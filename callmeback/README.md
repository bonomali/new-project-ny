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

# start the mongo CLI. replace <password> with the value in backend/k8s.yml
mongo -u root -p <password>

# print all documents to console, confirming a successful CLI session
use nyst
db.reservation.find()
```

### Access the HAL Explorer

The [HAL Explorer](https://github.com/toedter/hal-explorer) provides a user 
interface for interacting with the REST API. You can view the UI by
navigating to `http://<hostname>/api/v1/`.
