package se.kth.tablespoon.agent.file;

import com.fasterxml.jackson.jr.ob.JSON;
import java.io.IOException;
import java.util.HashMap;
import se.kth.tablespoon.agent.events.TopicDefinition;

public class Configuration {
  
  
  private String riemannHost;
  private int riemannPort;
  private int riemannReconnectionTries;
  private int riemannReconnectionTime;
  private int riemannSendRate;
  private int riemannEventTtl;
  private int riemannDereferenceTime;
  private boolean collectlRestart;
  private double collectlCollectionRate;
  private HashMap<Integer, TopicDefinition> subscriptions = new HashMap<Integer, TopicDefinition>();
  private int[] filter;
  
  public Configuration() {
    filter = new int[subscriptions.size()];
    for (int i = 0; i < subscriptions.size(); i++) {
      filter[i] = subscriptions.get(i).getCollectlIndex();
    }
  }
  
  // this constructor is for creating testconfiguration
  public Configuration(String riemannHost, int riemannPort, int riemannReconnectionTries, int riemannReconnectionTime, int riemannSendRate, int riemannEventTtl, int riemannDereferenceTime, boolean collectlRestart, double collectlCollectionRate) {
    this.riemannHost = riemannHost;
    this.riemannPort = riemannPort;
    this.riemannReconnectionTries = riemannReconnectionTries;
    this.riemannReconnectionTime = riemannReconnectionTime;
    this.riemannSendRate = riemannSendRate;
    this.riemannEventTtl = riemannEventTtl;
    this.riemannDereferenceTime = riemannDereferenceTime;
    this.collectlRestart = collectlRestart;
    this.collectlCollectionRate = collectlCollectionRate;
  }
  
  
  public HashMap<Integer, TopicDefinition> getSubscriptions() {
    return subscriptions;
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
  
  public double getCollectlCollectionRate() {
    return collectlCollectionRate;
  }
  
  public int[] getFilter() {
    return filter;
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




