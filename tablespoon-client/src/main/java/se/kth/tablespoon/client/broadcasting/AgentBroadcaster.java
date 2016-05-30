package se.kth.tablespoon.client.broadcasting;

import java.util.Set;

public interface AgentBroadcaster {
 
  public void sendToMachines(Set<String> machines, String json, String topicId) throws BroadcastException;

  
}
