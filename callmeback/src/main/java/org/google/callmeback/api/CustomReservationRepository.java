package org.google.callmeback.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Criteria;

interface CustomReservationRepository {
    public void startNextCall();
}

@Component
class CustomReservationRepositoryImpl implements CustomReservationRepository {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public CustomReservationRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void startNextCall() {
        Sort sort = Sort.by("reservationCreatedDate").ascending();
        Query query = new Query(Criteria.where("events").is(null)).with(sort).limit(1);

        List<ReservationEvent> resEvents = new ArrayList<ReservationEvent>();
        ReservationEvent resEvent = new ReservationEvent();
        resEvent.date = new Date();
        resEvent.type = ReservationEventType.ATTEMPTED;
        resEvents.add(resEvent);
        Update update = new Update().set("events", resEvents);

        mongoTemplate.findAndModify(query, update, null, Reservation.class, "reservations");
    }
}