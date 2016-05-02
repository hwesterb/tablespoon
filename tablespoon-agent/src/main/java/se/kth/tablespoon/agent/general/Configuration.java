package se.kth.tablespoon.agent.general;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import se.kth.tablespoon.agent.events.EventDefinition;

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
  private HashMap<Integer, EventDefinition> subscriptions = new HashMap<Integer, EventDefinition>();
  private int[] filter;
  
  public Configuration() {
    filter = new int[subscriptions.size()];
    for (int i = 0; i < subscriptions.size(); i++) {
      filter[i] = subscriptions.get(i).getCollectlIndex();
    }
  }
  
  public HashMap<Integer, EventDefinition> getSubscriptions() {
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
    Gson gson =  new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(this);
  }
  
  
  
  
}




