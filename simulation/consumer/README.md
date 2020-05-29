# consumer

A "consumer" script to be run via the Command Line that simulates handling and completing call requests.

## Prerequisites

* [Install Node](https://nodejs.org/en/download/)

## Build and running

From the consumer directory, run:

```bash
npm install
node main.js --api-url 'http://192.168.64.4:31442'
```

where the api-url is the URL (host + port) where the Call Me Back API is running, and is required.

There are other optional flags:

* --length = duration of each simulated call in minutes. Defaults to 1 minute.
* --period = time in minutes between attempts to handle the next call. Defaults to 5 minutes.

For example,

```bash
node main.js --length 1 --period .5 --api-url 'http://192.168.64.4:31442'
```

would start two calls per minute, each call lasting one minute with a 30 second gap in between the call start times.
