package se.kth.tablespoon.client.broadcasting;

import java.util.HashSet;

/**
 *
 * @author henke
 */
public interface AgentBroadcaster {
 
<<<<<<< Updated upstream
  public void sendToMachines(ArrayList<String> machines, String json) throws BroadcastException;
||||||| merged common ancestors
  public void sendToMachines(ArrayList<String> machines, String json, String topicId, int version) throws BroadcastException;
=======
  public void sendToMachines(HashSet<String> machines, String json, String topicId) throws BroadcastException;
>>>>>>> Stashed changes
  
}
