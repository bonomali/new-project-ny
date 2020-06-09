package gov.ny.its;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApplicationTest {
  @Autowired 
  private ItemRepository repo;

  @Test
  public void contextLoads() {
    assertThat(repo).isNotNull();
  }
}
