# consumer

A "consumer" script to be run via the Command Line that simulates handling and completing call requests.

## Prerequisites

* [Install Node](https://nodejs.org/en/download/)

## Build and running

From the consumer directory, run:

```bash
node main.js --api-url 'http://192.168.64.4:31442'
```

where the api-url is the URL (host + port) where the Call Me Back API is running, and is required.

There are other optional flags:

* --length = duration of each simulated call in minutes
* --period = time in minutes between attempts to handle the next call

For example,

```bash
node main.js --length 1 --period .5 --api-url 'http://192.168.64.4:31442'
```

would handle two calls per minute, and each of those calls would be simulated for 1 minute before
being marked as resolved.
