package se.kth.tablespoon.client.broadcasting;

import java.io.IOException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.client.topics.Topic;
import se.kth.tablespoon.client.topics.TopicStorage;

/**
 *
 * @author henke
 */
public class AgentBroadcaster implements Runnable {
  
  private Broadcaster broadcaster;
  private final TopicStorage storage;
  private final static Logger slf4jLogger = LoggerFactory.getLogger(AgentBroadcaster.class);
  
  public AgentBroadcaster(TopicStorage storage) {
    this.storage = storage;
  }
  
  private void broadcastTopics() {
    for (Topic topic : storage.getTopics()) {
      topic.lock();
      ArrayList<String> machines = topic.getMachinesToNotify();
      if (machines.size() > 0) try {
        topic.generateJson();
      } catch (IOException ex) {
        slf4jLogger.debug(ex.getMessage());
      }
      for (String machine : machines) {
        broadcaster.sendToMachine(machine, topic.getJson());
      }
      topic.unlock();
    }
  }
  
  public void registerBroadcaster(Broadcaster broadcaster) {
    this.broadcaster = broadcaster;
  }
  
  @Override
  public void run() {
    broadcastTopics();
    storage.clean();
    waitForChange();
  }
  
  
  private void waitForChange() {
    synchronized (storage) {
      try {
        while (!storage.isChanged()) {
          wait();
        }
        storage.setChanged(false);
      } catch (InterruptedException ex) {
        slf4jLogger.debug(ex.getMessage());
      }
    }
  }
  
  
  
  
}

