package se.kth.tablespoon.client.broadcasting;

/**
 *
 * @author henke
 */
public interface AgentBroadcaster {
 
  public void sendToMachine(String machine, String json);
  
}
