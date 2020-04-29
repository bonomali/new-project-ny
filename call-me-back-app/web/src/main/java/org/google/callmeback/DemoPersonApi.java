package org.google.callmeback;

import com.mongodb.DB;
import java.util.UUID;
import org.google.newprojectny.dao.PersonRepository;
import org.google.newprojectny.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.MongoDbFactory;

@SpringBootApplication
public class DemoPersonApi {

  private final MongoDbFactory mongo;

  @Autowired
  private PersonRepository repository;

  public DemoPersonApi(MongoDbFactory mongo) {
    this.mongo = mongo;
  }

  public static void main(String[] args) {
    SpringApplication.run(DemoPersonApi.class, args);
  }

  public void run(String... args) throws Exception {

    repository.deleteAll();

    System.out.println("Here are the env vars: ");
    System.out.println(System.getenv("MONGODB_HOST"));
    System.out.println(System.getenv("MONGODB_PORT"));
    System.out.println(System.getenv("MONGODB_USER"));
    System.out.println(System.getenv("MONGODB_PASSWORD"));

//    MongoClient mongoClient = new MongoClient(serverAddress, credential);
//    DB db = mongoClient.getDB(mongoDatabase);

    // save a couple of people
    UUID alice_id = UUID.randomUUID();
    UUID bob_id = UUID.randomUUID();
    UUID second_alice_id = UUID.randomUUID();
    repository.save(new Person(alice_id, "Alice"));
    repository.save(new Person(bob_id, "Bob"));
    repository.save(new Person(second_alice_id, "Alice"));

    // fetch all people
    System.out.println("People found with findAll():");
    System.out.println("-------------------------------");
    for (Person person : repository.findAll()) {
      System.out.println(person);
    }
    System.out.println();

    // fetch an individual person
    System.out.println("Person found with findById('123'):");
    System.out.println("--------------------------------");
    System.out.println(repository.findById(alice_id));

    System.out.println("Person found with findByName('Bob'):");
    System.out.println("--------------------------------");
    for (Person person : repository.findByName("Bob")) {
      System.out.println(person);
    }

    // fetch multiple people matching
    System.out.println("People found with findByName('Alice'):");
    System.out.println("--------------------------------");
    for (Person person : repository.findByName("Alice")) {
      System.out.println(person);
    }

  }
}
