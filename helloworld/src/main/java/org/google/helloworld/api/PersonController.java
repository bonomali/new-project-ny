package org.google.helloworld.api;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.google.helloworld.dao.PersonRepository;
import org.google.helloworld.model.Person;
import org.google.helloworld.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/person")
@RestController
public class PersonController {
  private PersonRepository personRepository;

  @GetMapping("/insert")
  public Person insert() {
    Person person = new Person(UUID.randomUUID(), "Alice");
    personRepository.save(person);
    return person;
  }

  @GetMapping("/select")
  public List<Person> select() {
    List<Person> people = personRepository.findByName("Alice");
    return people;
  }

  @Autowired private final PersonService personService;

  @Autowired
  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  @PostMapping
  public Person addPerson(@Valid @NonNull @RequestBody Person person) {
    return personService.addPerson(person);
  }

  @GetMapping
  public List<Person> getAllPeople() {
    return personService.getAllPeople();
  }

  @GetMapping(path = "{id}")
  public Person getPersonById(@PathVariable("id") UUID id) {
    return personService.getPersonById(id).orElse(null);
  }

  @DeleteMapping(path = "{id}")
  public void deletePersonById(@PathVariable("id") UUID id) {
    personService.deletePerson(id);
  }

  @PutMapping(path = "{id}")
  public void updatePerson(
      @PathVariable("id") UUID id, @Valid @NonNull @RequestBody Person personToUpdate) {
    personService.updatePerson(id, personToUpdate);
  }
}
