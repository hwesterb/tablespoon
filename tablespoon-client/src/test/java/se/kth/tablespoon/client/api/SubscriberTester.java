package se.kth.tablespoon.client.api;

public class SubscriberTester implements Subscriber {

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
    this.event = event;
    counter++;
  }

  public int getCounter() {
    return counter;
  }

  public TablespoonEvent getEvent() {
    return event;
  }

}
