package org.google.callmeback.api;

import java.util.Date;

/**
 * Represents the resident's feedback about their service experience.
 */
public class ReservationFeedback {
    /**
     * The date when the feedback was provided.
     */
    public Date date;

    /**
     * The resident's rating (1-5) of their experience.
     */
    public int rating;

    /**
     * The resident's open-ended comment about their experience.
     */
    public String comment;
}
