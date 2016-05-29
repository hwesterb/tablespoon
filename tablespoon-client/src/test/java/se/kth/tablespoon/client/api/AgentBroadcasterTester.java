/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.client.api;

import java.util.HashSet;
import se.kth.tablespoon.client.broadcasting.AgentBroadcaster;
import se.kth.tablespoon.client.broadcasting.BroadcastException;

/**
 *
 * @author henke
 */
public class AgentBroadcasterTester implements AgentBroadcaster {
  
  private int recievedRequests;



  public int getRecievedRequests() {
    return recievedRequests;
  }

  @Override
<<<<<<< Updated upstream
  public void sendToMachines(ArrayList<String> machines, String json) throws BroadcastException {
    System.out.println("Sending to machines: " + machines);
    System.out.println("Information (json): " + json);
||||||| merged common ancestors
  public void sendToMachines(ArrayList<String> machines, String json, String topicId, int version) throws BroadcastException {
=======
  public void sendToMachines(HashSet<String> machines, String json, String topicId) throws BroadcastException {
>>>>>>> Stashed changes
    recievedRequests += machines.size();
  }
 
  
}
