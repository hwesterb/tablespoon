package se.kth.tablespoon.client.api;

import se.kth.tablespoon.client.util.Time;

public class SubscriberTester implements Subscriber {

  private final boolean LATENCY_EXPERIMENT = true;
  private String uniqueId;
  private int counter = 0;
  private TablespoonEvent event;
  
  
  
  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public String getUniqueId() {
    return uniqueId;
  }
  
  @Override
  public void onEventArrival(TablespoonEvent event) {
    latencyExperiment(event);
    this.event = event;
    counter++;
  }
  
   private void latencyExperiment(TablespoonEvent event) {
    if (LATENCY_EXPERIMENT) {
      System.out.println(event.getTimeStamp() + " " + Time.nowMs());
    }
  }

  public int getCounter() {
    return counter;
  }

  public TablespoonEvent getEvent() {
    return event;
  }

}
