package se.kth.tablespoon.client.broadcasting;

import java.io.IOException;
import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.client.topics.Topic;
import se.kth.tablespoon.client.topics.TopicStorage;

public class AgentBroadcasterAssistant implements Runnable {
  
  private AgentBroadcaster broadcaster;
  private final TopicStorage storage;
  private final static Logger slf4jLogger = LoggerFactory.getLogger(AgentBroadcasterAssistant.class);
  
  public AgentBroadcasterAssistant(TopicStorage storage) {
    this.storage = storage;
  }
  
  private void broadcastRemoval() {
    try {
      storage.broadcastRemoval(broadcaster);
    } catch (IOException | BroadcastException   ex) {
      slf4jLogger.debug(ex.getMessage());
    }
  }
  
  private void broadcastTopics() {
    for (Topic topic : storage.getTopics()) {
      HashSet<String> machinesToNotify = topic.getMachinesToNotify();
      if (machinesToNotify.size() > 0) {
        try {
          broadcaster.sendToMachines(machinesToNotify, topic.getJson(), topic.getUniqueId());
          topic.addToNotifiedMachines(machinesToNotify);
        } catch (BroadcastException ex) {
          //TODO: handle this
          slf4jLogger.debug(ex.getMessage());
        }
      }
    }
  }
  
  public void registerBroadcaster(AgentBroadcaster broadcaster) {
    this.broadcaster = broadcaster;
  }
  
  @Override
  public void run() {
    while (true) {
      storage.clean();
      broadcastRemoval();
      broadcastTopics();
      waitForChange();
    }
  }
 
  private void waitForChange() {
    synchronized (storage) {
      try {
        while (!storage.isStorageChanged()) {
          storage.wait();
        }
        storage.stateHasChanged(false);
      } catch (InterruptedException ex) {
        slf4jLogger.debug(ex.getMessage());
      }
    }
  }
  
  
  
  
}

