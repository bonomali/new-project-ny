package org.google.helloworld.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.google.helloworld.dao.PersonDao;
import org.google.helloworld.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

  private final PersonDao personDao;

  @Autowired
  public PersonService(@Qualifier("fakeDao") PersonDao personDao) {
    this.personDao = personDao;
  }

  public Person addPerson(Person person) {
    return personDao.insertPerson(person);
  }

  public List<Person> getAllPeople() {
    return personDao.selectAllPeople();
  }

  public Optional<Person> getPersonById(UUID id) {
    return personDao.selectPersonById(id);
  }

  public int deletePerson(UUID id) {
    return personDao.deletePersonById(id);
  }

  public int updatePerson(UUID id, Person newPerson) {
    return personDao.updatePersonById(id, newPerson);
  }
}
