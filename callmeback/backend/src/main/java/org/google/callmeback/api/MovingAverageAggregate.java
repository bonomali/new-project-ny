package org.google.callmeback.api;

/**
 * MovingAverageAggregate represents a reservation which has been annotated with a moving average
 * for the wait time at the moment that this reservation is connected to an agent. Therefore, the 
 * moving average stored on a reservation document does *not* represent the amount of time that the
 * reservation waited before being connected with an agent.
 * 
 * To avoid confusing the actual call handle time with the moving average, this class should be used 
 * with {@code org.springframework.data.mongodb.core.MongoTemplate} functions only when retrieving 
 * the waitTimeMovingAverage to calculate subsequent moving averages.
 */
public class MovingAverageAggregate extends Reservation {

    /**
     * The exponential moving average, in minutes, representing the average wait time based on the 
     * previous moving average and the time that it took for this Reservation to be connected with 
     * an agent.
     */
    public double waitTimeMovingAverage;
}