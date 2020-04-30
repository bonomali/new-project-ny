package org.google.callmeback;

import java.util.Properties;

import org.google.callmeback.dao.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.mongodb.MongoDbFactory;

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
