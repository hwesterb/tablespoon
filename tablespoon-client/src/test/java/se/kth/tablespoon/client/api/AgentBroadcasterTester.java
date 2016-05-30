package se.kth.tablespoon.client.api;

import java.util.Set;
import se.kth.tablespoon.client.broadcasting.AgentBroadcaster;
import se.kth.tablespoon.client.broadcasting.BroadcastException;


public class AgentBroadcasterTester implements AgentBroadcaster {
  
  private int recievedRequests;

  public int getRecievedRequests() {
    return recievedRequests;
  }

  @Override
  public void sendToMachines(Set<String> machines, String json, String topicId) throws BroadcastException {
    System.out.println(json);
    recievedRequests += machines.size();
  }
 
  
}
