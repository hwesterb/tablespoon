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
  
  
  public static Topic create(TopicStorage storage, EventType type, Resource resource, Group group) {
    return makeGroupTopic(storage, getIndex(resource), type, group);
  }
  
   public static Topic create(TopicStorage storage, EventType type, Resource resource) {
    return makeMachineTopic(storage,  getIndex(resource), type);
  }
  
  private static Topic makeMachineTopic(TopicStorage storage, int index, EventType type) {
    Topic topic = new MachineTopic(index, Time.now(), createUniqueId(storage), type);
    storage.add(topic);
    return topic;
  }
  
  private static Topic makeGroupTopic(TopicStorage storage, int index, EventType type, Group group) {
    Topic topic = new GroupTopic(index, Time.now(), createUniqueId(storage), type, group);
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
