package se.kth.tablespoon.client.broadcasting;

import java.util.HashSet;

/**
 *
 * @author henke
 */
public interface AgentBroadcaster {
 
  public void sendToMachines(HashSet<String> machines, String json, String topicId) throws BroadcastException;

  
}
