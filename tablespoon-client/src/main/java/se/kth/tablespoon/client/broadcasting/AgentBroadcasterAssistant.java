package se.kth.tablespoon.client.broadcasting;

import java.io.IOException;
import java.util.HashSet;

import com.fasterxml.jackson.jr.ob.JSON;
import com.fasterxml.jackson.jr.ob.JSONComposer;
import com.fasterxml.jackson.jr.ob.comp.ObjectComposer;
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
    slf4jLogger.info("Broadcasting removal of topics. ");
    try {
      storage.broadcastRemoval(broadcaster);
    } catch (IOException | BroadcastException   ex) {
      slf4jLogger.debug(ex.getMessage());
    }
  }
  
  private void broadcastTopics() {
    slf4jLogger.info("Broadcasting available topics to machines. ");
    for (Topic topic : storage.getTopics()) {
      HashSet<String> machinesToNotify = topic.getMachinesToNotify();
      slf4jLogger.info("The number of machines to be notified for topic " + topic.getUniqueId() + " is: " + machinesToNotify.size() + ".");
      if (machinesToNotify.size() > 0) {
        try {
          broadcaster.sendToMachines(machinesToNotify, topic.getJson(), topic.getUniqueId());
          topic.addToNotifiedMachines(machinesToNotify);
          slf4jLogger.info("Topic " + topic.getUniqueId() + " was sent to AgentBroadcaster. ");
        } catch (BroadcastException ex) {
          //TODO: handle this
          slf4jLogger.debug(ex.getMessage());
        }
      }
    }
  }
  
  public void registerBroadcaster(AgentBroadcaster broadcaster) {
    this.broadcaster = broadcaster;
    slf4jLogger.info("AgentBroadcaster has been registered. ");
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
    slf4jLogger.info("AgentBroadcasterAssistant has begun to wait for changes in TopicStorage. ");
    synchronized (storage) {
      while (!storage.isStorageChanged()) {
        try {
          storage.wait();
        } catch (InterruptedException ex) {
          slf4jLogger.debug(ex.getMessage());
        }
      }
      slf4jLogger.info("A change in TopicStorage has been detected. ");
      storage.stateHasChanged(false);
    }
  }
  
  public String getAgentConfig(String ip,int port) throws IOException{

    JSONComposer<String> composer = JSON.std
            .with(JSON.Feature.PRETTY_PRINT_OUTPUT)
            .composeString();
    ObjectComposer obj = composer.startObject();
    obj.put("riemannHost", ip)
            .put("riemannPort", port)
            .put("riemannReconnectionTries", 10000)
            .put("riemannReconnectionTime", 5000)
            .put("riemannEventTtl", 60)
            .put("riemannDereferenceTime", 5000)
            .end();
    obj.end();
    return composer.finish();
  }
}

