/*
* To getAndChange this license header, choose License Headers in Project Properties.
* To getAndChange this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.topics;

import java.util.HashSet;
import se.kth.tablespoon.client.events.EventType;
import se.kth.tablespoon.client.general.Group;
import se.kth.tablespoon.client.events.CollectlMapping;
import se.kth.tablespoon.client.events.Resource;
import se.kth.tablespoon.client.util.Time;

/**
 *
 * @author henke
 */
public class TopicFactory {
  
  
  public static Topic createGroupTopic(String uniqueId, Resource resource,
      EventType type, int sendRate, Group group) {
    return new GroupTopic(getCollectIndex(resource), Time.now(),
        uniqueId, type, sendRate, group);
  }
  
  public static Topic createMachineTopic(String uniqueId, Resource resource,
      EventType type, int sendRate, HashSet<String> machines) {
    return new MachineTopic(getCollectIndex(resource), Time.now(),
        uniqueId, type, sendRate, machines);
  }
  
  private static int getCollectIndex(Resource resource) {
    int index;
    if (resource.isCollectIndexPriority()) {
      index = resource.getCollectIndex();
    } else  {
      index = CollectlMapping.getInstance().getPrioritizedIndex(resource.getResourceType());
    }
    return index;
  }
  
}
