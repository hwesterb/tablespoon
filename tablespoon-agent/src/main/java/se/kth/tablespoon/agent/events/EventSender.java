package se.kth.tablespoon.agent.events;

import se.kth.tablespoon.agent.main.Start;
import java.io.IOException;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aphyr.riemann.client.RiemannClient;

import se.kth.tablespoon.agent.general.CollectlStringParser;
import se.kth.tablespoon.agent.general.Configuration;

public class EventSender {
  
  private final Queue<String> rowQueue;
  private final EventLayout[] els;
  private final RiemannClient rClient;
  private final Logger slf4jLogger = LoggerFactory.getLogger(Start.class);
  private final Configuration config;
  
  public EventSender(Queue<String> rowQueue, EventLayout[] els, RiemannClient rClient, Configuration config) {
    this.config = config;
    this.rowQueue = rowQueue;
    this.els = els;
    this.rClient = rClient;
  }
  
  public void sendMetrics() throws IOException {
    // To prevent event from being sent while collectl is reconfiguring.
    synchronized (rowQueue) {
      String line = rowQueue.poll();
      String[] rowArray = CollectlStringParser.split(line);
      long timeStamp = Math.round(Double.parseDouble(rowArray[0]));
      double[] numericRowArray =  new double[els.length];
      // The first column is a time stamp, and shall be included in every
      // message. Therefore it should not be part of the numericRowArray.
      for (int i = 1; i <  rowArray.length; i++) {
        numericRowArray[i-1] = Double.parseDouble(rowArray[i]);
      }
      matchWithLayoutAndSend(numericRowArray, timeStamp);
    }
  }
  
  private void matchWithLayoutAndSend(double[] row, long timeStamp) throws IOException {
    long ttl = config.getRiemannEventTtl();
    long now = System.currentTimeMillis() / 1000L;
    if (now - timeStamp > ttl) {
      slf4jLogger.info("Metric was too old and discarded.");
      return;
    }
    for (int i = 0; i < els.length; i++) {
      rClient.event().
          service(els[i].getType().toString()).
          description(els[i].getName()).
          tag(els[i].getFormat().toString()).
          metric(row[i]).
          time(timeStamp).
          ttl(ttl).
          send().
          deref(config.getRiemannDereferenceTime(), java.util.concurrent.TimeUnit.MILLISECONDS);
    }
    slf4jLogger.info("Sent metrics.");
  }
  
  
}
