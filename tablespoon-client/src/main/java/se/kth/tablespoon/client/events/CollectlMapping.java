/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.events;

import java.util.HashMap;
import static se.kth.tablespoon.client.events.ResourceType.CPU;
import static se.kth.tablespoon.client.events.ResourceType.DSK;
import static se.kth.tablespoon.client.events.ResourceType.MEM;
import static se.kth.tablespoon.client.events.ResourceType.NET;

/**
 *
 * @author henke
 */
public class CollectlMapping {
  
  HashMap<Integer, ResourceType> mapping = new HashMap();
  
  private static CollectlMapping instance = null;
  
  
  public static CollectlMapping getInstance() {
    if(instance == null) {
      instance = new CollectlMapping();
    }
    return instance;
  }
  
  protected CollectlMapping() {
    for (int i = 0; i <= 18; i++) {
      mapping.put(i, CPU);
    }
    for (int i = 19; i <= 46; i++) {
      mapping.put(i, MEM);
    }
    for (int i = 47; i <= 55; i++) {
      mapping.put(i, NET);
    }
    for (int i = 56; i <= 64; i++) {
      mapping.put(i, DSK);
    }
    mapping.put(65, MEM);
  }
  
  
  public ResourceType getResourceType(int collectIndex) {
    return mapping.get(collectIndex);
  }
  
  public int getPrioritizedIndex(ResourceType resourceType) {
    switch (resourceType) {
      case CPU:
        return 0;  // 0 = [CPU]User%
      case DSK:
        return 49; // 49 = [NET]RxKBTot
      case NET:
        return 56; // 56 = [DSK]ReadTot
      case MEM:
        return 65; // 64 = [MEM]MemoryUsed%
    }
    return 0;
  }
  
  
  
}
