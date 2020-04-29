package org.google.callmeback.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="people")
public class Person {
  @Id
  private final UUID id;
  @NotBlank private final String name;
  @NotBlank private final String phone_number;
  private final String status;

  public Person(@JsonProperty("id") UUID id, @JsonProperty("name") String name,
                @JsonProperty("phone_number") String phone_number,
                @JsonProperty("status") String status) {
    this.id = id;
    this.name = name;
    this.phone_number = phone_number;
    this.status = status;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getPhoneNumber() { return phone_number;}

  public String getStatus() { return status;}
}
