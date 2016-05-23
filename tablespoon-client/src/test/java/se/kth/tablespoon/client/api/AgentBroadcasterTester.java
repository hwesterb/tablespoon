/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.client.api;

import se.kth.tablespoon.client.broadcasting.AgentBroadcaster;

/**
 *
 * @author henke
 */
public class AgentBroadcasterTester implements AgentBroadcaster {
  
  private int recievedRequests;

  @Override
  public void sendToMachine(String machine, String json) {
    System.out.println("Sending to machine: " + machine);
    System.out.println("Information (json): " + json);
    recievedRequests++;
  }

  public int getRecievedRequests() {
    return recievedRequests;
  }
 
  
}
