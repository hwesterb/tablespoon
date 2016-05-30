package se.kth.tablespoon.client.api;

import se.kth.tablespoon.client.broadcasting.SubscriberBroadcaster;
import se.kth.tablespoon.client.topics.Topic;


public class SubscriberBroadcasterTester implements SubscriberBroadcaster{
  
  @Override
  public void broadcast() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  @Override
  public void registerSubscriber(Subscriber subscriber, Topic topic) {
    // New subscriber was registered.
  }
  
}
