package gov.ny.its;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document
public class Item {
  @Id 
  public long id;

  public String name;
}