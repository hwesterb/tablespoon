/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.client.main;

import se.kth.tablespoon.client.api.Subscriber;
import se.kth.tablespoon.client.api.TablespoonAPI;
import se.kth.tablespoon.client.api.TablespoonEvent;
import se.kth.tablespoon.client.broadcasting.AgentBroadcasterAssistant;
import se.kth.tablespoon.client.broadcasting.SubscriberBroadcaster;
import se.kth.tablespoon.client.topics.TopicFactory;
import se.kth.tablespoon.client.topics.TopicStorage;

/**
 *
 * @author henke
 */
public class Start {
  
  
  
  public static void main(String args[]) {
//    Groups groups = new Groups();
//    TopicStorage storage = new TopicStorage(groups);
//    AgentBroadcasterAssistant aba = new AgentBroadcasterAssistant(storage);
//    TablespoonAPI api = new TablespoonAPI(storage, groups);
//    Thread abaThread = new Thread(aba);
//    abaThread.start();
//    aba.registerBroadcaster(null);
    SubscriberBroadcaster sb = new SubscriberBroadcaster("localhost", 5555);
    Thread sbThread = new Thread(sb);
    sbThread.start();
    sb.registerSubscriber(new Subscriber() {
      @Override
      public void onEventArrival(TablespoonEvent event) {
        System.out.println("new event baby!");
      }
    });
  }
  
  
  public void startClient() {
    
  }
  
}
