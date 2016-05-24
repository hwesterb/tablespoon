package se.kth.tablespoon.agent.events;

import io.riemann.riemann.client.EventDSL;
import io.riemann.riemann.client.RiemannClient;
import java.io.IOException;
import java.util.ArrayList;

public class RiemannEvent {
  private final String service;
  private final String state;
  private final String description;
  private final double metric;
  private final ArrayList<String> tags = new ArrayList<>();
  private final double time;
  private final float ttl;
  
  public RiemannEvent(String service, String state, String description, double metric, double time, float ttl) {
    this.service = service;
    this.state = state;
    this.description = description;
    this.metric = metric;
    this.time = time;
    this.ttl = ttl;
  }
  
  public void addTag(String tag) {
    tags.add(tag);
  }
  
  public void sendMe(RiemannClient rClient, int dereferenceTime) throws IOException {
    EventDSL event = rClient.event();
    event.service(service).
        description(description).
        metric(metric).
        state(state).
        time(time).
        ttl(ttl);
    for (String tag : tags) {
      event.tag(tag);
    }
    event.send().deref(dereferenceTime, java.util.concurrent.TimeUnit.MILLISECONDS);
  }
  
  public double getValue() {
    return metric;
  }
  
  
}
