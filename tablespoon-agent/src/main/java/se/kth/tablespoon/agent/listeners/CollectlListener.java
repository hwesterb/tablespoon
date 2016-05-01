package se.kth.tablespoon.agent.listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.kth.tablespoon.agent.events.EventLayout;
import se.kth.tablespoon.agent.general.CollectlStringParser;
import se.kth.tablespoon.agent.general.Configuration;
import se.kth.tablespoon.agent.main.Start;

public class CollectlListener implements Runnable {
  
  
  private final Queue<String> rowQueue = new LinkedList<>();
  private Process process;
  private BufferedReader br;
  private EventLayout[] els;
  private boolean headersDefined = false;
  private boolean interruptRequest = false;
  private boolean restartRequest = false;
  private Configuration config;
  private final Logger slf4jLogger = LoggerFactory.getLogger(Start.class);
  
  public CollectlListener(Configuration config) {
    this.config = config;
  }
  
  public void requestInterrupt()  {
    restartRequest = false;
    interruptRequest = true;
  }
  
  @Override
  public void run() {
    try {
      startCollectl();
    } catch (IOException e) {
      slf4jLogger.error(e.getMessage());
    }
    slf4jLogger.info("Ending collectl-thread.");
  }
  
  private void startCollectl() throws IOException {
    String[] cmd = {
      "/bin/sh",
      "-c",
      "collectl " + config.getCollectlConfig()
    };
    process = Runtime.getRuntime().exec(cmd);
    br = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line;
    while ((line = br.readLine()) != null) {
      slf4jLogger.info("Reading from collectl.");
      // define the headers
      if (!headersDefined) {
        if (line.startsWith("#")) {
          // To prevent this from happening while a message is being sent.
          synchronized (rowQueue) {
            els = CollectlStringParser.handleHeaders(line);
            rowQueue.clear();
          }
          headersDefined = true;
        }
        continue;
      }
      // add a row to queue
      rowQueue.add(line);
      if (interruptRequest) break;
      if (restartRequest) break;
    }
    stopCollectl();
    if (restartRequest) {
      restartRequest = false;
      slf4jLogger.info("Collectl is now restarted.");
      startCollectl();
    }
  }
  
  public void stopCollectl() throws IOException {
    br.close();
    process.destroy();
    headersDefined = false;
    slf4jLogger.info("Ending collectl-process.");
  }
  
  public void restartCollectl() {
    restartRequest = true;
    slf4jLogger.info("Attempting to restart collectl.");
  }
  
  public Queue<String> getRowQueue() {
    return rowQueue;
  }
  
  public EventLayout[] getEventLayouts() {
    return els;
  }
  
  public boolean queueIsEmpty() {
    return rowQueue.isEmpty();
  }
  
  public boolean isRestarting() {
    return restartRequest;
  }
}
