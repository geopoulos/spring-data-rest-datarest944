package org.springframework.sdr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories
public class Datarest944Application {

  public static void main(String[] args) {
    SpringApplication.run(Datarest944Application.class, args);
  }
  
}
