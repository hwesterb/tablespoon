/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.events;

import com.aphyr.riemann.Proto.Event;
import se.kth.tablespoon.client.api.TablespoonEvent;
import se.kth.tablespoon.client.topics.Topic;
import se.kth.tablespoon.client.topics.TopicFactory;

/**
 *
 * @author henke
 */
public class EventConverter {
  
  public static TablespoonEvent changeFormat(Event event, Topic topic) {
    ResourceType resourceType = CollectlMapping.getInstance().getResourceType(topic.getIndex());
    TablespoonEvent tablespoonEvent = new TablespoonEvent(topic.getGroupId(),
        event.getHost(),
        event.getMetricD(),
        topic.getType(),
        resourceType,
        topic.getHigh(),
        topic.getLow());
    return tablespoonEvent;
  }
  
}
