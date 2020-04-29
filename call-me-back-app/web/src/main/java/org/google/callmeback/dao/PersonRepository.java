package org.google.callmeback.dao;

import java.util.List;
import java.util.UUID;
import org.google.callmeback.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonRepository extends MongoRepository<Person, String> {

    public Person findById(UUID id);
    public List<Person> findByName(String name);
    public List<Person> findByPhoneNumber(String phone_number);
    public List<Person> findByStatus(String status);
  
}
