# kubernetes

This contains all the configuration files to spin up a kubernetes cluster with
the web and mongodb services running.

## Setting up

You'll need to install [minikube](https://minikube.sigs.k8s.io/docs/start/)
and before doing these next steps.

```bash
minikube start

# Use minikube docker instead of machine docker
eval $(minikube docker-env)
```

Go and build the Docker images in `/web` and `/storage` as per their
instructions.

### Starting

```bash
kubectl apply -k .
```

### Stopping

```bash
kubectl delete -k .
```

## Things that are done here that you absolutely shouldn't do in prod

* Put your passwords in the `kustomization.yaml` file directly. You really would
  want these managed in some kind of password store or secret store and not with
  your source.
* Don't use root for everything. It seems like the idomatic way here is to have
  a bootsrtap configuration script pulled from some private cloud storage or
  something similar.
