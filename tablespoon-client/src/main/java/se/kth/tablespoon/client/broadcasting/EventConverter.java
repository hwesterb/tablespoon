package se.kth.tablespoon.client.broadcasting;


import io.riemann.riemann.Proto.Event;
import se.kth.tablespoon.client.api.TablespoonEvent;
import se.kth.tablespoon.client.events.CollectlMapping;
import se.kth.tablespoon.client.events.ResourceType;
import se.kth.tablespoon.client.topics.Topic;

/**
 *
 * @author henke
 */
class EventConverter {
  
  static TablespoonEvent changeFormat(Event event, Topic topic) {
    ResourceType resourceType = CollectlMapping.getInstance().getResourceType(topic.getCollectIndex());
    TablespoonEvent tablespoonEvent = new TablespoonEvent(topic.getGroupId(),
        event.getHost(),
        event.getMetricD(),
        topic.getEventType(),
        resourceType,
        topic.getHigh(),
        topic.getLow());
    return tablespoonEvent;
  }
  
}
