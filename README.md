# new-project-ny

NOTE: This is not an officially supported Google product.

A quick and dirty hello world docker test.

For more details on specific processes or systems, see the wiki.

## Pre-requisites

* [Docker (19.03.8 or higher)](https://docs.docker.com/get-started/#download-and-install-docker-desktop)
* [kubectl (1.18.2 or
  higher)](https://kubernetes.io/docs/tasks/tools/install-kubectl)
* [minikube (1.9.2 or
  higher)](https://kubernetes.io/docs/tasks/tools/install-minikube/)

## Building and running

Before starting anything, make sure minikube is running.

```shell
minikube status || minikube start
```

Note that as long as Docker is installed properly, minikube should use
Docker as its driver, and a separate Hypervisor should not be necessary to
install.

To build and run:

* `./gradlew appName:buildImage` - Builds an image for a local run
* `./gradlew appName:buildProdImage` - Builds a hermetic prod image
* `./gradlew appName:start` - Starts the app locally
* `./gradlew appName:stop` - Stops the running app

Replace `appName` with the directory name you'd like to run or exclude it to
build or run everything (e.g. `./gradlew callmeback:run).

Note that during development, only `./gradlew appName:start` should be necessary
to rebuild and restart the application with the updated code.

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
