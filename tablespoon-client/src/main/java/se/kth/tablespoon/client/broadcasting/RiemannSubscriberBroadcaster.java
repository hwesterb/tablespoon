package se.kth.tablespoon.client.broadcasting;


import io.riemann.riemann.client.RiemannClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.client.api.Subscriber;
import se.kth.tablespoon.client.topics.Topic;
import se.kth.tablespoon.client.topics.TopicStorage;
import se.kth.tablespoon.client.util.Time;

public class RiemannSubscriberBroadcaster implements Runnable, SubscriberBroadcaster {
  
  private final static Logger slf4jLogger = LoggerFactory.getLogger(SubscriberBroadcaster.class);
  private final ThreadPoolExecutor tpe = new ThreadPoolExecutor(16, 16, 60, TimeUnit.SECONDS,
      new LinkedBlockingQueue<Runnable>());
  private final TopicStorage storage;
  RiemannClient riemannClient;
  private final String host;
  private final int port;
  private final int RECONNECTION_TIME = 5000;
  private final int RECONNECTION_TRIES = 100;
  private int tries = 0;
  
  public RiemannSubscriberBroadcaster(String host, int port, TopicStorage storage) {
    this.host = host;
    this.port = port;
    this.storage = storage;
  }
  
  private void broadcastEvents() throws IOException {
    while(true) {
      for (Topic topic : storage.getTopics()) {
        topic.fetch(tpe);
        Time.sleep(10);
      }
    }
  }
  
  @Override
  public void run() {
    broadcast();
  }
  
  @Override
  public void broadcast() {
    try {
      connect();
      //resetting number of tries if connection was established
      tries = 0;
      broadcastEvents();
    } catch (IOException e) {
      if (reconnect(tries)) {
        tries++;
        broadcast();
      }
    }
  }
  
  
  private void connect() throws IOException {
    riemannClient = RiemannClient.tcp(host, port);
    riemannClient.connect();
    slf4jLogger.info("Established connection with host:"
        + host + " port:" + port);
  }
  
  private boolean reconnect(int tries) {
    riemannClient.close();
    slf4jLogger.info("Connection with server could not be established.");
    if (tries < RECONNECTION_TRIES) {
      slf4jLogger.info("Waiting for "
          + Math.round(RECONNECTION_TIME / 1000)
          + " seconds and attempting to connect again...");
      Time.sleep(RECONNECTION_TRIES);
      return true;
    }
    return false;
  }
  
  public void closeRiemannClient() {
    if (riemannClient != null) {
      if (riemannClient.isConnected()) {
        riemannClient.close();
        slf4jLogger.info("Closed the connection with the Riemann client.");
      }
    }
  }
  
  public RiemannClient getRiemannClient() {
    return riemannClient;
  }
  
  @Override
  public void registerSubscriber(Subscriber subscriber, Topic topic) {
    topic.createFetcher(subscriber, riemannClient);
  }
  
}
