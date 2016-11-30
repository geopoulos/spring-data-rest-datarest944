package org.springframework.sdr;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Data;

@Data
@Document(indexName = "parent")
public class Parent {

  @Id
  private String id;
  
  private Map<String, Object> map;

}
