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

* --flag = description

For example,

```bash
node main.js --flag description --api-url 'http://192.168.64.4:31442'
```

would run the script with ...
