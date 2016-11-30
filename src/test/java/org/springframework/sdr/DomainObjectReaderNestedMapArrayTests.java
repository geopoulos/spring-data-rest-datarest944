package org.springframework.sdr;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.keyvalue.core.mapping.context.KeyValueMappingContext;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.webmvc.json.DomainObjectReader;
import org.springframework.data.rest.webmvc.mapping.Associations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RunWith(MockitoJUnitRunner.class)
public class DomainObjectReaderNestedMapArrayTests {

  @Mock
  ResourceMappings mappings;

  DomainObjectReader reader;

  ObjectMapper mapper;

  @Before
  public void setUp() {

    KeyValueMappingContext mappingContext = new KeyValueMappingContext();
    mappingContext.getPersistentEntity(Parent.class);
    mappingContext.afterPropertiesSet();

    PersistentEntities entities = new PersistentEntities(Collections.singleton(mappingContext));

    this.reader = new DomainObjectReader(entities,
        new Associations(mappings, mock(RepositoryRestConfiguration.class)));

    this.mapper = new ObjectMapper();

  }

  @Test
  @SuppressWarnings("unchecked")
  public void readPutWithStringArray() throws Exception {

    Map<String, Object> map = new HashMap<>();
    map.put("colors", new ArrayList<>(Arrays.asList("red", "blue")));

    Parent parent = new Parent();
    parent.setMap(map);

    ObjectNode payload = (ObjectNode) mapper.readTree(
        "{ \"map\": { \"colors\": [ \"black\", \"white\" ] } }");
    
    Parent result = reader.readPut(payload, parent, mapper);

    List<String> colors = as(result.getMap().get("colors"), List.class);
    assertThat(colors.size(), is(2));
    assertThat(colors.get(0), is("black"));
    assertThat(colors.get(1), is("white"));

  }

  @Test
  @SuppressWarnings("unchecked")
  public void readPutWithSameSizeObjectArray() throws Exception {

    Map<String, Object> john = new HashMap<>();
    john.put("name", "John");

    Map<String, Object> map = new HashMap<>();
    map.put("persons", new ArrayList<>(Arrays.asList(john)));

    Parent parent = new Parent();
    parent.setMap(map);

    ObjectNode payload = (ObjectNode) mapper.readTree(
        "{ \"map\": { \"persons\": [ { \"name\": \"Jane\" } ] } }");
    
    Parent result = reader.readPut(payload, parent, mapper);
    
    Map<String, Object> jane = new HashMap<>();
    jane.put("name", "Jane");

    List<Map<String, Object>> persons = as(result.getMap().get("persons"), List.class);
    assertThat(persons.size(), is(1));
    assertThat(persons.get(0), is(jane));

  }
  
  @Test
  @SuppressWarnings("unchecked")
  public void readPutWithLargerObjectArray() throws Exception {

    Map<String, Object> john = new HashMap<>();
    john.put("name", "John");

    Map<String, Object> map = new HashMap<>();
    map.put("persons", new ArrayList<>(Arrays.asList(john)));

    Parent parent = new Parent();
    parent.setMap(map);

    ObjectNode payload = (ObjectNode) mapper.readTree(
        "{ \"map\": { \"persons\": [ { \"name\": \"Jane\" }, { \"name\": \"Dave\" } ] } }");
    
    Parent result = reader.readPut(payload, parent, mapper);
        
    Map<String, Object> jane = new HashMap<>();
    jane.put("name", "Jane");
    
    Map<String, Object> dave = new HashMap<>();
    dave.put("name", "Dave");
    
    List<Map<String, Object>> persons = as(result.getMap().get("persons"), List.class);
    assertThat(persons.size(), is(2));
    assertThat(persons.get(0), is(jane));    
    assertThat(persons.get(1), is(dave));
    
  }
  
  @Test
  @SuppressWarnings("unchecked")
  public void readPutWithEmptyObjectArray() throws Exception {

    Map<String, Object> john = new HashMap<>();
    john.put("name", "John");

    Map<String, Object> map = new HashMap<>();
    map.put("persons", new ArrayList<>(Arrays.asList(john)));

    Parent parent = new Parent();
    parent.setMap(map);

    ObjectNode payload = (ObjectNode) mapper.readTree(
        "{ \"map\": { \"persons\": [ ] } }");
    
    Parent result = reader.readPut(payload, parent, mapper);

    List<Map<String, Object>> persons = as(result.getMap().get("persons"), List.class);
    assertThat(persons.size(), is(0));

  }

  @SuppressWarnings("unchecked")
  private static <T> T as(Object source, Class<T> type) {
    assertThat(source, is(instanceOf(type)));
    return (T) source;
  }

}
