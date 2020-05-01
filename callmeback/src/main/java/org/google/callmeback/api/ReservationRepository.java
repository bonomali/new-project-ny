package org.google.callmeback.api;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ReservationRepository extends MongoRepository<Reservation, String> {
  List<Reservation> findByTopics(@Param("topic") String topic);
}
