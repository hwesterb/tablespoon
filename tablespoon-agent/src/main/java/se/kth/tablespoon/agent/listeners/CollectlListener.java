package se.kth.tablespoon.agent.listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.kth.tablespoon.agent.metrics.MetricFactory;

import se.kth.tablespoon.agent.metrics.Metric;
import se.kth.tablespoon.agent.general.CollectlStringParser;
import se.kth.tablespoon.agent.general.Configuration;

public class CollectlListener extends MetricListener {

  public CollectlListener(Configuration config) {
    super(config);
  }

  @Override
  public void collectCycle() {
    try {
      String[] cmd = {
        "/bin/sh",
        "-c",
        "collectl -P -o U -i " + config.getCollectlCollectionRate()
      };
      process = Runtime.getRuntime().exec(cmd);
      br = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      while ((line = br.readLine()) != null) {
        slf4jLogger.info("Reading from collectl.");
        // define the headers
        if (!headersDefined) {
          if (line.startsWith("#")) {
            mls = CollectlStringParser.handleHeaders(line);
            headersDefined = true;
          }
          continue;
        }

        // add events to the queue
        Metric[] events = MetricFactory.createMetrics(line, mls, config);
        for (Metric event : events) {
          metricQueue.add(event);
        }
        
        emptyOld();
        
        if (interruptRequest) {
          break;
        }
        if (restartRequest) {
          break;
        }
      }
      stopCollecting();
      if (restartRequest) {
        restartRequest = false;
        slf4jLogger.info("Collectl is now restarted.");
        collectCycle();
      }
    } catch (IOException e) {
      slf4jLogger.error(e.getMessage());
    }
  }

  private void stopCollecting() throws IOException {
    br.close();
    process.destroy();
    headersDefined = false;
    slf4jLogger.info("Ending collectl-process.");
  }

}
