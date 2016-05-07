package se.kth.tablespoon.client.broadcasting;

/**
 *
 * @author henke
 */
public interface Broadcaster {
 
  public void sendToMachine(String machine, String json);
  
}
