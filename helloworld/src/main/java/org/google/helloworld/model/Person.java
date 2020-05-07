package org.google.helloworld.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "people")
public class Person {
  @Id private final UUID id;
  @NotBlank private final String name;

  public Person(@JsonProperty("id") UUID id, @JsonProperty("name") String name) {
    this.id = id;
    this.name = name;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return getId() + "/" + getName();
  }
}
