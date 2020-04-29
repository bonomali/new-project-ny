package org.google.callmeback;

import org.google.callmeback.dao.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.MongoDbFactory;

@SpringBootApplication
public class DemoPersonApi {

  private final MongoDbFactory mongo;

  @Autowired private PersonRepository repository;

  public DemoPersonApi(MongoDbFactory mongo) {
    this.mongo = mongo;
  }

  public static void main(String[] args) {
    SpringApplication.run(DemoPersonApi.class, args);
  }
}
