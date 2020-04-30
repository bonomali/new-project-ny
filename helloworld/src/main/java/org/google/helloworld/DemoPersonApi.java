package org.google.helloworld;

import java.util.Properties;

import org.google.helloworld.dao.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoPersonApi {
  @Autowired private PersonRepository repository;

  public static void main(String[] args) {
    Properties props = new Properties();
    props.put("spring.data.mongodb.host", System.getenv("MONGODB_HOST"));
    props.put("spring.data.mongodb.port", System.getenv("MONGODB_PORT"));
    props.put("spring.data.mongodb.username",
        System.getenv("MONGODB_USER"));
    props.put("spring.data.mongodb.password",
        System.getenv("MONGODB_PASSWORD"));

    new SpringApplicationBuilder(DemoPersonApi.class)
        .properties(props).run(args);
  }
}
//  public void run(String... args) throws Exception {
//
//    repository.deleteAll();
//
//    // save a couple of people
//    UUID alice_id = UUID.randomUUID();
//    UUID bob_id = UUID.randomUUID();
//    UUID second_alice_id = UUID.randomUUID();
//    repository.save(new Person(alice_id, "Alice"));
//    repository.save(new Person(bob_id, "Bob"));
//    repository.save(new Person(second_alice_id, "Alice"));
//
//    // fetch all people
//    System.out.println("People found with findAll():");
//    System.out.println("-------------------------------");
//    for (Person person : repository.findAll()) {
//      System.out.println(person);
//    }
//    System.out.println();
//
//    // fetch an individual person
//    System.out.println("Person found with findById('123'):");
//    System.out.println("--------------------------------");
//    System.out.println(repository.findById(alice_id));
//
//    System.out.println("Person found with findByName('Bob'):");
//    System.out.println("--------------------------------");
//    for (Person person : repository.findByName("Bob")) {
//      System.out.println(person);
//    }
//
//    // fetch multiple people matching
//    System.out.println("People found with findByName('Alice'):");
//    System.out.println("--------------------------------");
//    for (Person person : repository.findByName("Alice")) {
//      System.out.println(person);
//    }
//
//  }

//    public void example() {
//        DB db = mongo.getDb();
//    }

// }
