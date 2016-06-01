package se.kth.tablespoon.client.broadcasting;

import java.io.IOException;
import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.client.topics.Topic;
import se.kth.tablespoon.client.topics.TopicStorage;

/**
 *
 * @author henke
 */
public class AgentBroadcasterAssistantTester{
  
  private AgentBroadcaster broadcaster;
  private final TopicStorage storage;
  private final static Logger slf4jLogger = LoggerFactory.getLogger(AgentBroadcasterAssistant.class);
  
  public AgentBroadcasterAssistantTester(TopicStorage storage) {
    this.storage = storage;
  }
  
  private void broadcastTopics() {
    for (Topic topic : storage.getTopics()) {
      HashSet<String> machinesToNotify = topic.getMachinesToNotify();
      if (machinesToNotify.size() > 0) {
        try {
          topic.generateJson();
        } catch (IOException ex) {
          slf4jLogger.debug(ex.getMessage());
        }
        try {
          broadcaster.sendToMachines(machinesToNotify, topic.getJson(), topic.getUniqueId());
          topic.addToNotifiedMachines(machinesToNotify);
        } catch (Exception ex) {
          //TODO: handle this
          slf4jLogger.debug(ex.getMessage());
        }
      }
    }
  }
  
  public void registerBroadcaster(AgentBroadcaster broadcaster) {
    this.broadcaster = broadcaster;
  }
  
}

