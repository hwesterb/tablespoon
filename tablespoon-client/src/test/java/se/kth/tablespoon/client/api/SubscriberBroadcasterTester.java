/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.tablespoon.client.api;

import se.kth.tablespoon.client.broadcasting.SubscriberBroadcaster;
import se.kth.tablespoon.client.topics.Topic;

/**
 *
 * @author te27
 */
public class SubscriberBroadcasterTester implements SubscriberBroadcaster{

  @Override
  public void broadcast() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void registerSubscriber(Subscriber subscriber, Topic topic) {
    System.out.println("new subscriber was registered");
  }
  
}
