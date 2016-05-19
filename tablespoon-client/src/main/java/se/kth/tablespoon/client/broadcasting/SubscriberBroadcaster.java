/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.client.broadcasting;

import com.aphyr.riemann.client.RiemannClient;
import java.io.IOException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.client.api.Subscriber;
import se.kth.tablespoon.client.topics.Topic;
import se.kth.tablespoon.client.util.Sleep;

/**
 *
 * @author henke
 */
public class SubscriberBroadcaster implements Runnable {
  
  private final static Logger slf4jLogger = LoggerFactory.getLogger(SubscriberBroadcaster.class);
  ArrayList<EventFetcher> fetchers = new ArrayList<>();
  RiemannClient riemannClient;
  private final String host;
  private final int port;
  private final int RECONNECTION_TIME = 5000;
  private final int RECONNECTION_TRIES = 100;
  
  public SubscriberBroadcaster(String host, int port) {
    this.host = host;
    this.port = port;
  }
  
  private void broadcastEvents() throws IOException {
    while(true) {
      for (EventFetcher fetcher : fetchers) {
        fetcher.queryRiemann(riemannClient);
         Sleep.now(10000);
      }
    }
  }
  
  public void registerSubscriber(Subscriber subscriber, Topic topic) {
    fetchers.add(new EventFetcher(subscriber, topic));
  }
  
  public void registerSubscriber(Subscriber subscriber) {
    fetchers.add(new EventFetcher(subscriber, null));
  }
  
  @Override
  public void run() {
    broadcasterCycle(0);
  }
  
  private void broadcasterCycle(int tries) {
    try {
      connect();
      //resetting number of tries if connection was established
      tries = 0;
      broadcastEvents();
    } catch (IOException e) {
      if (reconnect(tries)) {
        broadcasterCycle(++tries);
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
      Sleep.now(RECONNECTION_TRIES);
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
  
}
