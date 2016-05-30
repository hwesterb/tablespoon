package se.kth.tablespoon.client.broadcasting;

import se.kth.tablespoon.client.api.Subscriber;
import se.kth.tablespoon.client.topics.Topic;

public interface SubscriberBroadcaster{
  
  public void broadcast();
  
  public void registerSubscriber(Subscriber subscriber, Topic topic);
   
}
