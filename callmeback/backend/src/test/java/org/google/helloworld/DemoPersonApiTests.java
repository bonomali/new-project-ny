package org.google.helloworld;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.google.helloworld.dao.FakePersonDataAccessService;
import org.google.helloworld.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

// Start a test environment mimicking the @SpringBootApplication annotated class
@SpringBootTest
// Create a MockMvc class and inject it into SpringBoot's dependency management system
@AutoConfigureMockMvc
class DemoPersonApiTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private FakePersonDataAccessService personRepository;

  @Test
  void addingNewPersonWorksThroughAllLayers() {
    UUID id = UUID.randomUUID();
    Person p = new Person(id, "Jean Luc Picard");

    try {
      mockMvc
          .perform(
              post("/v1/person")
                  .contentType("application/json")
                  .content(objectMapper.writeValueAsString(p)))
          .andExpect(status().isOk());
    } catch (Exception e) {
      e.printStackTrace();
    }

    Person pFromDb = personRepository.selectPersonById(id).get();
    assertEquals("Jean Luc Picard", pFromDb.getName(), "Name does not match.");
  }
}
