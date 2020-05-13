# producer

A "producer" script to be run via the Command Line that simulates call requests arriving to the call center queue.

## Prerequisites

* [Install Node](https://nodejs.org/en/download/)

## Build and running

From the producer directory, run:
```
node main.js --apiUrl 'http://192.168.64.4:31442'
```
where the apiUrl is the URL (host + port) where the Call Me Back API is running, and is required.

There are other optional flags:
  * --callsPerHour = the number of calls that should be sent each hour; defaults to 10
  * --speedUpFactor = the factor at which real wall clock time should be sped up to simulation time. For example, providing 60 would cause the simulation to run 60 times faster than real time, and would send requests every minute, instead of every 60 minutes. Defaults to 1, which means it will be run in real time

For example,
```
node main.js --callsPerHour 100 --speedUpFactor 120 --apiUrl 'http://192.168.64.4:31442'
```
would run the script with 100 calls per "hour" where an "hour" takes 30 seconds to simulate (60 minutes is sped up by a factor of 120).