/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author henke
 */
public class EventRegistry {
  
  private final TreeMap<String, EventDefinition> registry = new TreeMap<>();
  
  public void add(EventDefinition event) {
    registry.put(event.getUniqueId(), event);
  }
  
  public boolean uniqueIdExists(String uniqueId) {
    return registry.containsKey(uniqueId);
  }
  
  public void clean(ArrayList<String> liveMachines) {
    Iterator<Entry<String, EventDefinition>> entries = registry.entrySet().iterator();
    long now = System.currentTimeMillis() / 1000L;
    while (entries.hasNext()) {
      Entry<String, EventDefinition> entry = entries.next();
      EventDefinition ed = entry.getValue();
      if (ed.getDuration() > 0 &&
          (ed.getStartTime() + ed.getDuration()) < now) {
        entries.remove();
        continue;
      }
      ed.removeDeadMachines(liveMachines);
      if (ed.hasNoLiveMachines()) {
        entries.remove();
        continue;
      }
    }
  }
  
  public boolean isEmpty() {
    return registry.isEmpty();
  }
  
  
}
