package org.google.callmeback;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoAuditing
@EnableScheduling
public class CallMeBackApp {
  public static void main(String[] args) {
    new SpringApplicationBuilder(CallMeBackApp.class).run(args);
  }
}
