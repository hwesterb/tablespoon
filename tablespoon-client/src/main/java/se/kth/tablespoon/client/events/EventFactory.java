/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.events;

import java.util.UUID;

/**
 *
 * @author henke
 */
public class EventFactory {
  
  
  public static EventDefinition create(EventRegistry registry, ResourceType rt) {
    int index = collectlMapping(rt);
    return makeEvent(registry, index);
  }
  
  public static EventDefinition create(EventRegistry registry, int index) {
    return makeEvent(registry, index);
  }
  
  private static EventDefinition makeEvent(EventRegistry registry, int index) {
    String uniqueId = createUniqueId(registry);
    long now = System.currentTimeMillis() / 1000L;
    return new EventDefinition(index, now, uniqueId);
  }
  
  
  private static int collectlMapping(ResourceType rt) {
    if (rt == ResourceType.CPU_PERCENTAGE) return 0;
    else return 10;
  }
  
  
  @SuppressWarnings("empty-statement")
  private static String createUniqueId(EventRegistry registry) {
    String uniqueId;
    while (registry.uniqueIdExists(uniqueId = UUID.randomUUID().toString()));
    return uniqueId;
  }
  
}
