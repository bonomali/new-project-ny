package org.google.newprojectny.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Entity
public class KeyValue {
  private @Id String key;
  private String value;

  public KeyValue(String key, String value) {
    this.key = key;
    this.value = value;
  }

  @Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		KeyValue kv = (KeyValue) o;
		return Objects.equals(key, kv.key) &&
			Objects.equals(value, kv.value);
	}

  @Override
	public int hashCode() {
		return Objects.hash(key, value);
	}

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
	public String toString() {
		return "KeyValue{" +
			"key=" + key +
			", value='" + value + '}';
	}
}
