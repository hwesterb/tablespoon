package se.kth.tablespoon.client.broadcasting;

import java.util.ArrayList;

/**
 *
 * @author henke
 */
public interface AgentBroadcaster {
 
  public void sendToMachines(ArrayList<String> machines, String json, String topicId, int version) throws BroadcastException;
  
}
