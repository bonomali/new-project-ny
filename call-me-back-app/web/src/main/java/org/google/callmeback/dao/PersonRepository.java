package org.google.newprojectny.dao;

import java.util.List;
import java.util.UUID;
import org.google.newprojectny.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonRepository extends MongoRepository<Person, String> {

  public Person findById(UUID id);

  public List<Person> findByName(String name);
}
