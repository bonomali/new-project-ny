package org.google.newprojectny.model;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.repository.CrudRepository;

public interface KeyValueRepository extends CrudRepository<KeyValue, String> {
}
