package org.google.callmeback;

import java.util.Properties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class CallMeBackApp {
  public static void main(String[] args) {
    Properties props = new Properties();
    props.put("spring.data.mongodb.host", System.getenv("MONGODB_HOST"));
    props.put("spring.data.mongodb.port", System.getenv("MONGODB_PORT"));
    props.put("spring.data.mongodb.username", System.getenv("MONGODB_USER"));
    props.put("spring.data.mongodb.password", System.getenv("MONGODB_PASSWORD"));

    new SpringApplicationBuilder(CallMeBackApp.class).properties(props).run(args);
  }
}
