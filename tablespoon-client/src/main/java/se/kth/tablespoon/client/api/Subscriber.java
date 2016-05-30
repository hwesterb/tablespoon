package se.kth.tablespoon.client.api;

public interface Subscriber {

  public void onEventArrival(TablespoonEvent event);
  
}
