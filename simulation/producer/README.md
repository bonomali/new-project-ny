# producer

A "producer" script to be run via the Command Line that simulates call requests arriving to the call center queue.

## Prerequisites

* [Install Node](https://nodejs.org/en/download/)

## Build and running

From the producer directory, run:
```
npm install
node main.js --api-url 'http://192.168.64.4:31442'
```
where the api-url is the URL (host + port) where the Call Me Back API is running, and is required.

There are other optional flags:
  * --calls = the number of calls that should be sent during each period; defaults to 10
  * --period = the length of time (in minutes) during which the specified number of calls are sent; defaults to 60 minutes. This represents the wall clock time, and can be sped up by the --speed-up flag.
  * --rewind-request-dates-by = the number of minutes the calls' request dates should be rewinded from the present time; default to 0 minutes (wall clock time). For example, if this is 30, the request date for each call will be
  30 minutes prior to the time the request is actually created.

For example,
```
node main.js --calls 100 --period 60 --rewind-request-dates-by 30 --api-url 'http://192.168.64.4:31442'
```
would run the script with 100 calls per hour where requests start dates are 30 minutes before the wall clock time.