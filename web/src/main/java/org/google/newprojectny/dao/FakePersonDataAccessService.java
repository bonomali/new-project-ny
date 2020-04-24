package org.google.newprojectny.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.google.newprojectny.model.Person;
import org.springframework.stereotype.Repository;

@Repository("fakeDao")
public class FakePersonDataAccessService implements PersonDao {

  private static List<Person> DB = new ArrayList<>();

  @Override
  public Person insertPerson(UUID id, Person person) {
    Person p = new Person(id, person.getName());
    DB.add(p);
    return p;
  }

  @Override
  public List<Person> selectAllPeople() {
    return DB;
  }

  @Override
  public Optional<Person> selectPersonById(UUID id) {
    return DB.stream().filter(person -> person.getId().equals(id)).findFirst();
  }

  @Override
  public int deletePersonById(UUID id) {
    Optional<Person> personMaybe = selectPersonById(id);
    if (!personMaybe.isPresent()) {
      return 0;
    }
    DB.remove(personMaybe.get());
    return 1;
  }

  @Override
  public int updatePersonById(UUID id, Person update) {
    return selectPersonById(id)
        .map(
            person -> {
              int indexOfPersonToUpdate = DB.indexOf(person);
              if (indexOfPersonToUpdate >= 0) {
                DB.set(indexOfPersonToUpdate, new Person(id, update.getName()));
                return 1;
              }
              return 0;
            })
        .orElse(0);
  }
}
