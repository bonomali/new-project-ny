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
      * [Allowing Ingress in Docker](https://kubernetes.github.io/ingress-nginx/deploy/#docker-for-mac) - Run:
       ```
       kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-0.32.0/deploy/static/provider/cloud/deploy.yaml
       ```
        * This may need to be re-run if you blow up your Kubernetes configuration 
    * Linux: [minikube](https://minikube.sigs.k8s.io/docs/start/)
  * [Tilt](https://docs.tilt.dev/install.html)
    * Mac: May need to update homebrew permissions to have write access to /usr/local/bin directory (prompts with necessary commands will surface on the terminal if so)

## Building and running

### Start your Kubernetes environment

#### Mac

Enabling Kubernetes in Docker (as in the Tilt instructions) allows this to work. Nothing additional needs to be run.

#### Linux

```bash
minikube start
```

### Start the app

```
tilt up
```

Then get the IP address for the app and connect to it on port 80:
```
kubectl describe ingress
```
