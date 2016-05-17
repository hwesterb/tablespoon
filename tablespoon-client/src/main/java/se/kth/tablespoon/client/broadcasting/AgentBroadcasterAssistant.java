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
public class AgentBroadcasterAssistant implements Runnable {
  
  private Broadcaster broadcaster;
  private final TopicStorage storage;
  private final static Logger slf4jLogger = LoggerFactory.getLogger(AgentBroadcasterAssistant.class);
  
  public AgentBroadcasterAssistant(TopicStorage storage) {
    this.storage = storage;
  }
  
  private void broadcastTopics() {
    for (Topic topic : storage.getTopics()) {
      topic.lock();
      //
      ArrayList<String> machinesToNotify = topic.getMachinesToNotify();
      if (machinesToNotify.size() > 0) {
        try {
          topic.generateJson();
        } catch (IOException ex) {
          slf4jLogger.debug(ex.getMessage());
        }
        for (String machine : machinesToNotify) {
          broadcaster.sendToMachine(machine, topic.getJson());
          topic.addToNotifiedMachines(machine);
        }
      }
      topic.unlock();
    }
  }
  
  public void registerBroadcaster(Broadcaster broadcaster) {
    this.broadcaster = broadcaster;
  }
  
  @Override
  public void run() {
    while (true) {
      broadcastTopics();
      storage.clean();
      waitForChange();
    }
  }
  
  
  private void waitForChange() {
    synchronized (storage) {
      try {
        while (!storage.isStorageChanged()) {
          storage.wait();
        }
        storage.storageHasChanged(false);
      } catch (InterruptedException ex) {
        slf4jLogger.debug(ex.getMessage());
      }
    }
  }
  
  
  
  
}
