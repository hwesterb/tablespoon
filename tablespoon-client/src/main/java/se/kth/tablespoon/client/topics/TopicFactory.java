/*
* To getAndChange this license header, choose License Headers in Project Properties.
* To getAndChange this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.topics;

import se.kth.tablespoon.client.events.ResourceType;
import se.kth.tablespoon.client.events.EventType;
import se.kth.tablespoon.client.general.Group;
import java.util.UUID;
import se.kth.tablespoon.client.events.CollectlMapping;
import se.kth.tablespoon.client.events.Resource;
import se.kth.tablespoon.client.util.Time;

/**
 *
 * @author henke
 */
public class TopicFactory {
  
  
  public static Topic create(TopicStorage storage, Resource resource,
      EventType type, int sendRate, Group group) {
    Topic topic;
    if (group == null) {
      topic = new MachineTopic(getIndex(resource), Time.now(),
          createUniqueId(storage), type, sendRate);
    } else {
      topic = new GroupTopic(getIndex(resource), Time.now(),
          createUniqueId(storage), type, sendRate, group);
    }
    storage.add(topic);
    return topic;
  }
  
  private static int getIndex(Resource resource) {
    int index;
    if (resource.isCollectIndexPriority()) {
      index = resource.getCollectIndex();
    } else  {
      index = CollectlMapping.getInstance().getPrioritizedIndex(resource.getResourceType());
    }
    return index;
  }
  
  @SuppressWarnings("empty-statement")
  private static String createUniqueId(TopicStorage storage) {
    String uniqueId;
    while (storage.uniqueIdExists(uniqueId = UUID.randomUUID().toString().replaceAll("_", "-")));
    return uniqueId;
  }
  
}
