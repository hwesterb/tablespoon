package se.kth.tablespoon.agent.events;

import com.fasterxml.jackson.jr.ob.JSON;
import java.io.IOException;
import java.util.TreeMap;

public class Configuration {
  
  private int counter = 0;
  private String riemannHost;
  private int riemannPort;
  private int riemannReconnectionTries;
  private int riemannReconnectionTime;
  private int riemannSendRate;
  private int riemannEventTtl;
  private int riemannDereferenceTime;
  private boolean collectlRestart;
  private Rate collectlCollectionRate;
  private final TreeMap<Integer, RelatedTopics> relatedTopics = new TreeMap<>();
  
  private static Configuration instance = null;
  
  protected Configuration() {
    // Exists only to defeat instantiation.
  }
  
  public static Configuration getInstance() {
    if(instance == null) {
      instance = new Configuration();
    }
    return instance;
  }
  
  public void addTopic(Topic topic) {
    RelatedTopics related = relatedTopics.get(topic.getIndex());
    if (related==null) {
      related = new RelatedTopics();
      relatedTopics.put(topic.getIndex(), related);
    }
    related.addTopic(topic);
  }
  
  public Topic findTopic(String uniqueId) {
    Topic topic = null;
    for (RelatedTopics rt : relatedTopics.values()) {
      if ((topic = rt.contains(uniqueId)) != null) return topic;
    }
    return null;
  }
  
  public void incrementCounter() {
    counter++;
  }
  
  public RelatedTopics getRelatedTopicsBeloningToIndex(int index) {
    return relatedTopics.get(index);
  }
  
  public int getRiemannPort() {
    return riemannPort;
  }
  
  public String getRiemannHost() {
    return riemannHost;
  }
  
  public int getRiemannReconnectionTries() {
    return riemannReconnectionTries;
  }
  
  public int getRiemannReconnectionTime() {
    return riemannReconnectionTime;
  }
  
  public int getRiemannSendRate() {
    return riemannSendRate;
  }
  
  public int getRiemannEventTtl() {
    return riemannEventTtl;
  }
  
  public int getRiemannDereferenceTime() {
    return riemannDereferenceTime;
  }
  
  public boolean isCollectlRestart() {
    return collectlRestart;
  }
  
  public Rate getCollectlCollectionRate() {
    return collectlCollectionRate;
  }
  
  public int currentCounterValue() {
    return counter;
  }
  
  @Override
  public String toString() {
    try {
      return JSON.std
          .with(JSON.Feature.PRETTY_PRINT_OUTPUT)
          .without(JSON.Feature.FAIL_ON_DUPLICATE_MAP_KEYS)
          .asString(this);
    } catch (IOException ex) {
      System.err.println(ex.getMessage());
    }
    return "Could not process json.";
  }
  
  
  
  
}




