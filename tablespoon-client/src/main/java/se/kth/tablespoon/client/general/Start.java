/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.client.general;

import se.kth.tablespoon.client.api.TablespoonAPI;
import se.kth.tablespoon.client.broadcasting.AgentBroadcasterAssistant;
import se.kth.tablespoon.client.broadcasting.Broadcaster;
import se.kth.tablespoon.client.broadcasting.SubscriberBroadcaster;
import se.kth.tablespoon.client.topics.TopicStorage;

/**
 *
 * @author henke
 */
public class Start {
  
  
  public static void main(String args[]) {
//    setUp(null, null, "localhost", 5555);
  }
  
  public static void setUp(Groups groups, Broadcaster broadcaster, String riemannHost, int riemannPort) {
    TopicStorage storage = new TopicStorage(groups);
    AgentBroadcasterAssistant aba = new AgentBroadcasterAssistant(storage);
    SubscriberBroadcaster sb = new SubscriberBroadcaster(riemannHost, riemannPort);
    TablespoonAPI api = TablespoonAPI.getInstance();
    api.prepareAPI(storage, groups, sb);
    Thread abaThread = new Thread(aba);
    abaThread.start();
    aba.registerBroadcaster(broadcaster);
    Thread sbThread = new Thread(sb);
    sbThread.start();
  }
  
}
