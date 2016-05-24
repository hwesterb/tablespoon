/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.client.general;

import se.kth.tablespoon.client.api.TablespoonAPI;
import se.kth.tablespoon.client.broadcasting.AgentBroadcasterAssistant;
import se.kth.tablespoon.client.broadcasting.AgentBroadcaster;
import se.kth.tablespoon.client.broadcasting.RiemannSubscriberBroadcaster;
import se.kth.tablespoon.client.broadcasting.SubscriberBroadcaster;
import se.kth.tablespoon.client.topics.TopicStorage;

/**
 *
 * @author henke
 */
public class Start {
  
  
  public static void main(String args[]) {
     
  }
  
  public static void setUp(Groups groups, AgentBroadcaster broadcaster, String riemannHost, int riemannPort) {
    TopicStorage storage = new TopicStorage(groups);
    AgentBroadcasterAssistant aba = new AgentBroadcasterAssistant(storage);
    RiemannSubscriberBroadcaster rsb = new RiemannSubscriberBroadcaster(riemannHost, riemannPort);
    TablespoonAPI api = TablespoonAPI.getInstance();
    api.prepareAPI(storage, groups, rsb);
    Thread abaThread = new Thread(aba);
    abaThread.start();
    aba.registerBroadcaster(broadcaster);
    Thread sbThread = new Thread(rsb);
    sbThread.start();
  }
  
}
