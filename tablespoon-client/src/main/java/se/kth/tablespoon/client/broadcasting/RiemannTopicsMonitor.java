package se.kth.tablespoon.client.broadcasting;

import io.riemann.riemann.client.RiemannClient;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.client.api.Subscriber;
import se.kth.tablespoon.client.topics.Topic;
import se.kth.tablespoon.client.topics.TopicStorage;
import se.kth.tablespoon.client.util.Time;

public class RiemannTopicsMonitor implements Runnable, SubscriberBroadcaster {

  private final static Logger logger = LoggerFactory.getLogger(RiemannTopicsMonitor.class);
  private final ExecutorService tpe = Executors.newCachedThreadPool();
  private final TopicStorage storage;
  RiemannClient riemannClient;
  private final String host;
  private final int port;
  private final int RECONNECTION_SLEEP_TIME = 1000;
  private final int RIEMANN_MAX_RECONNECTION_TRIES = 10;

  public RiemannTopicsMonitor(String host, int port, TopicStorage storage) {
    this.host = host;
    this.port = port;
    this.storage = storage;
  }

  @Override
  public void run() {
    logger.info("RiemannTopicsMonitor is starting..");
    while (true) {
      try {
        connect();
        broadcast();
      } catch (Exception ex) {
        logger.error("", ex);
      }
    }
  }

  @Override
  public void broadcast() {
    logger.debug("Going to broadcast all topics ..");
    for (Topic topic : storage.getTopics()) {
      logger.debug("fetching topic: " + topic.toString());
      topic.fetch(tpe);
      logger.debug("topic " + topic.toString() + " was fetched successfully");
      Time.sleep(50);
    }
  }

  private synchronized void connect() throws IOException {
    int tries = 0;
    while (tries < RIEMANN_MAX_RECONNECTION_TRIES && (riemannClient == null || !riemannClient.isConnected())) {
      try {
        logger.debug(tries + " try to connect to the riemann server ...");
        tries++;
        riemannClient = RiemannClient.tcp(host, port);
        riemannClient.connect();
        logger.info("established connection to riemann :) host:" + host + " port:" + port);
      } catch (IOException e) {
        if (tries >= RIEMANN_MAX_RECONNECTION_TRIES) {
          logger.error("exhausted retries to connect to Riemann Server :(", e);
          throw e;
        } else {
          logger.info("Waiting for " + RECONNECTION_SLEEP_TIME
              + " mil-sec and attempting reiemann connection again...");
          Time.sleep(RECONNECTION_SLEEP_TIME);
        }
      }
    }
  }

  public synchronized void closeRiemannClient() {
    if (riemannClient != null) {
      if (riemannClient.isConnected()) {
        riemannClient.close();
        logger.info("Closed the connection with the Riemann client.");
      }
    }
  }

  @Override
  public synchronized void subscribe(Subscriber subscriber, Topic topic) {
    topic.createFetcher(subscriber, riemannClient);
  }

}
