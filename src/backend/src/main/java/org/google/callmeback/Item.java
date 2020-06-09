package org.google.callmeback;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document
public class Item {
  @Id 
  public String id;

  public String name;
}