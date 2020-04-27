# new-project-ny

NOTE: This is not an officially supported Google product.

A quick and dirty hello world docker test.

## Architecture

This repository contains two docker images that represent a web and storage service. Normally we would probably have these as separate repositories but for learning and proof of concept purposes we'll put
them all in separate directories here.

For more details on specific processes or systems, see the wiki.

## Pre-requisites

* [Docker (19.03.8 or higher)](https://docs.docker.com/get-started/#download-and-install-docker-desktop)

The build scripts will automatically install a local version of:

* [JDK (8 or higher)](https://www.oracle.com/java/technologies/javase-downloads.html)
* [Gradle (6.3 or higher)](https://gradle.org/install/)
* [Node.js (6.14.4 or higher)](https://nodejs.org/en/)

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
