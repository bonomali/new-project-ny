package org.google.callmeback.api;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reservations")
public class Reservation {
    @Id
    public String id;
    public String name;
    public String phone;
    public String topic;
}