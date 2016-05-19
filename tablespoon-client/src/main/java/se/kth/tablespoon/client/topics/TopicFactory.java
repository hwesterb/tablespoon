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
import se.kth.tablespoon.client.util.Time;

/**
 *
 * @author henke
 */
public class TopicFactory {
  
  
   public static Topic create(TopicStorage storage, EventType type, int index, Group group) {
    return makeGroupTopic(storage, index, type, group);
  }
  
  public static Topic create(TopicStorage storage, EventType type, ResourceType rt, Group group) {
    int index = collectlMapping(rt);
    return makeGroupTopic(storage, index, type, group);
  }
  
  public static Topic create(TopicStorage storage, EventType type, int index) {
    return makeMachineTopic(storage, index, type);
  }
  
   public static Topic create(TopicStorage storage, EventType type, ResourceType rt) {
    int index = collectlMapping(rt);
    return makeMachineTopic(storage, index, type);
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
  
  public static ResourceType collectlMapping(int index) {
    if (index == 0) return ResourceType.CPU;
    else return ResourceType.RAM; 
  }
  
  public static int collectlMapping(ResourceType rt) {
    if (rt == ResourceType.CPU) return 0;
    else return 65;
  }
  
  @SuppressWarnings("empty-statement")
  private static String createUniqueId(TopicStorage storage) {
    String uniqueId;
    while (storage.uniqueIdExists(uniqueId = UUID.randomUUID().toString().replaceAll("_", "-")));
    return uniqueId;
  }
  
}
