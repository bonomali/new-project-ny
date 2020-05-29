# simulation

To simulate call requests joining and being taken off of the call center queue, the consumer and producer scripts can be used. 

## Prerequisites

* [Install Node](https://nodejs.org/en/download/)

## Build and running

Details about running the two scripts are outlined in the consumer and producer READMEs.

## Simulation Parameters

Both scripts take parameters to indicate how quickly calls should be added and removed from the queue. The
following outlines some parameters that can be used to explore wait times in a meaningful way.

1. Seed the database with a history of resolved calls to have a starting point for wait time calculations:
    * Run producer script with --calls 10 --rewind-request-dates-by 60. This will add 10 calls that all have request dates 60 minutes before the current time. Once this completes, ctrl+C to stop the script.
    * Run consumer script with --period 0.1 --length .5. This will relatively quickly pick up the calls in the queue (one every 6 seconds) and then resolve each call after 30 seconds. Once this completes, ctrl+C to stop the script
    * Once the previous steps complete, the database will have 10 calls with wait times around an hour each.
2. Make a reservation in the UI. Both the naive and moving average wait times should be around an hour given the seeded data in the database.
3. Start running the scripts again in a slower fashion so that you can explore how the wait times change:
    * Run producer script with --calls 10 --period 10. This will add 10 calls every 10 minutes. Keep the script running.
    * Run consumer script with --period 3 --length 1. This will grab a call off the queue every 3 minutes, and the calls will last 1 minute. Keep the script running.
    * You can then look in the UI at the reservations you are creating (grab a reservation ID from the producer script output, or look in the database / HAL browser for open reservations). For an open reservation that has not yet been CONNECTED to an agent, the wait time will likely decrease. It would have originally been close to an hour because of the historical reservations in the system, but as the newer reservations get handled every 3 minutes, the average wait time will decrease. The wait time will decrease more quickly when using the exponential moving average as opposed to the naive average calculation.
4. To load test the system, consider upping the number of calls added and make the period in which those calls are added shorter (e.g. allow 100 calls to be added every 5 minutes). Simulate agents working more quickly (or more agents being added) by decreasing the period representing the amount of time it takes to get the first caller off of the queue.