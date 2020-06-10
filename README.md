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

and the app by:

```
> cd src/app
src/app> yarn build
src/app> yarn start
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
app-6574b66989-lpjpr     1/1     Running   0          61s
> kubectl logs -f app-6574b66989-lpjpr
# logs begin streaming from the selected container
```

## Running tests

To run api tests:

```
> cd src/api
src/api> ./gradlew test
```

To run app tests:

```
> cd src/app
src/app> yarn test
```

## Source Code Headers

Every file containing source code must include copyright and license
information. This includes any JS/CSS files that you might be serving out to
browsers. (This is to help well-intentioned people avoid accidental copying that
doesn't comply with the license.)

Apache header:

    Copyright 2020 Google LLC

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
