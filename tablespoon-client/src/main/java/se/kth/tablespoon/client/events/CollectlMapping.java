package se.kth.tablespoon.client.events;

import java.util.HashMap;
import static se.kth.tablespoon.client.events.ResourceType.CPU;
import static se.kth.tablespoon.client.events.ResourceType.DSK;
import static se.kth.tablespoon.client.events.ResourceType.MEM;
import static se.kth.tablespoon.client.events.ResourceType.NET;

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
    for (int i = 0; i <= 20; i++) {
      mapping.put(i, CPU);
    }
    for (int i = 21; i <= 49; i++) {
      mapping.put(i, MEM);
    }
    for (int i = 50; i <= 58; i++) {
      mapping.put(i, NET);
    }
    for (int i = 59; i <= 67; i++) {
      mapping.put(i, DSK);
    }
    mapping.put(68, MEM);
  }
  
  
  public ResourceType getResourceType(int collectIndex) {
    return mapping.get(collectIndex);
  }
  
  public int getPrioritizedIndex(ResourceType resourceType) {
    switch (resourceType) {
      case CPU:
        return 8;  // 8 - [CPU]Totl%
      case NET:
        return 52; // 52 - [NET]RxKBTot
      case DSK:
        return 59; // 59 - [DSK]ReadTot
      case MEM:
        return 68; // 68 - [MEM]MemoryUsed%
    }
    return 0;
  }
  
  
  
}
