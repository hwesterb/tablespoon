package se.kth.tablespoon.client.api;

public class TestSubscriber implements Subscriber {

  public String uniqueId;
  public int counter = 0;
  public TablespoonEvent event;
  
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