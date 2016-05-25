/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.client.api;

import java.util.ArrayList;
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
  public void sendToMachines(ArrayList<String> machines, String json) throws BroadcastException {
    recievedRequests += machines.size();
  }
 
  
}
