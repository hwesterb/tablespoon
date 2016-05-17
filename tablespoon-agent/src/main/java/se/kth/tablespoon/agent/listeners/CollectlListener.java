package se.kth.tablespoon.agent.listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import se.kth.tablespoon.agent.general.CollectlStringParser;
import se.kth.tablespoon.agent.events.RaterInterpreter;
import se.kth.tablespoon.agent.metrics.Metric;
import se.kth.tablespoon.agent.metrics.MetricFormat;
import se.kth.tablespoon.agent.metrics.MetricSource;

public class CollectlListener extends MetricListener {
  
  
  private String[] generateCollectlCommand() {
    return new String[]{
      "/bin/sh",
      "-c",
      "collectl -P -o U -s +m -i " +
        RaterInterpreter.getCorrespondingNumber(config.getCollectlCollectionRate())
    };
  }
  
  
  
  @Override
  public void collectCycle() {
    try {
      String[] cmd = generateCollectlCommand();
      process = Runtime.getRuntime().exec(cmd);
      br = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      while ((line = br.readLine()) != null) {
        slf4jLogger.info("Reading from collectl.");
        if (!headersDefined) {
          defineTheHeaders(line);
          continue;
        }
        addMetricToQueue(line);
        expireOldMetrics(0);
        if (interruptRequest || restartRequest) break;
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
  
  private void defineTheHeaders(String line) {
    if (line.startsWith("#")) {
      mls = CollectlStringParser.handleHeaders(line);
      headersDefined = true;
    }
  }
  
  @Override
  protected void createCustomMetrics(ArrayList<Metric> metrics) {
    double total = metrics.get(19).getValue();
    double used = metrics.get(20).getValue();
    long timeStamp = metrics.get(0).getTimeStamp();
    Metric metric = new Metric(metrics.size(),
        MetricSource.MEM,
        MetricFormat.PERCENTAGE,
        timeStamp,
        "MemoryUsed",
        used/total);
    metrics.add(metric);
  }
  
}
