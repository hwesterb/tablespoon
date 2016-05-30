package se.kth.tablespoon.agent.events;

import com.fasterxml.jackson.jr.ob.JSON;
import java.io.IOException;
import java.util.Map;
import se.kth.tablespoon.agent.file.JsonException;

public class Configuration {
  
  private int counter = 0;
  private String riemannHost;
  private int riemannPort;
  private int riemannReconnectionTries;
  private int riemannReconnectionTime;
  private int riemannEventTtl;
  private int riemannDereferenceTime;
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
  
  public void incrementCounter() {
    counter++;
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

  public int getRiemannEventTtl() {
    return riemannEventTtl;
  }
  
  public int getRiemannDereferenceTime() {
    return riemannDereferenceTime;
  }
  
  public int currentCounterValue() {
    return counter;
  }
  
  public void interpretJson(String json) throws IOException, JsonException {
    Map<String,Object> map = JSON.std.mapFrom(json);
    
    if (map.get("riemannHost") == null) throw new JsonException("riemannHost");
    else riemannHost = (String) map.get("riemannHost");
    
    if (map.get("riemannPort") == null) throw new JsonException("riemannPort");
    else riemannPort = (int) map.get("riemannPort");
    
    if (map.get("riemannReconnectionTries") == null) throw new JsonException("riemannReconnectionTries");
    else riemannReconnectionTries = (int) map.get("riemannReconnectionTries");
    
    if (map.get("riemannReconnectionTime") == null) throw new JsonException("riemannReconnectionTime");
    else riemannReconnectionTime = (int) map.get("riemannReconnectionTime");
    
    if (map.get("riemannReconnectionTime") == null) throw new JsonException("riemannReconnectionTime");
    else riemannReconnectionTime = (int) map.get("riemannReconnectionTime");
    
    if (map.get("riemannEventTtl") == null) throw new JsonException("riemannEventTtl");
    else riemannEventTtl = (int) map.get("riemannEventTtl");
    
    if (map.get("riemannDereferenceTime") == null) throw new JsonException("riemannDereferenceTime");
    else riemannDereferenceTime = (int) map.get("riemannDereferenceTime");
    
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




