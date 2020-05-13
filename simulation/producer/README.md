# producer

A "producer" script to be run via the Command Line that simulates call requests arriving to the call center queue.

## Prerequisites

* [Install Node](https://nodejs.org/en/download/)

## Build and running

From the producer directory, run:
```
node main.js --api-url 'http://192.168.64.4:31442'
```
where the api-url is the URL (host + port) where the Call Me Back API is running, and is required.

There are other optional flags:
  * --calls = the number of calls that should be sent during each period; defaults to 10
  * --period = the length of time (in minutes) during which the specified number of calls are sent; defaults to 60 minutes. This represents the wall clock time, and can be sped up by the --speed-up flag.
  * --speed-up = the factor at which wall clock time should be sped up for the simulation. For example, if the period is 60 minutes, and the speed-up factor is 60, the simulation would run 60 times faster than real time and would send requests every minute, instead of every 60 minutes. Defaults to 1, which means it will be run in real time

For example,
```
node main.js --calls 100 --period 60 --speed-up 120 --api-url 'http://192.168.64.4:31442'
```
would run the script with 100 calls per hour where an hour takes 30 seconds to simulate (60 minutes is sped up by a factor of 120).