package se.kth.tablespoon.agent.listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import se.kth.tablespoon.agent.general.CollectlStringParser;
import se.kth.tablespoon.agent.metrics.Metric;
import se.kth.tablespoon.agent.metrics.MetricFormat;
import se.kth.tablespoon.agent.events.ResourceType;

public class CollectlListener extends MetricListener {
  
  private String[] generateCollectlCommand() {
    return new String[]{
      "/bin/sh",
      "-c",
      "collectl -P -o U -s +m"
    };
  }
  
  @Override
  public void collectCycle() {
    try {
      String[] cmd = generateCollectlCommand();
      process = Runtime.getRuntime().exec(cmd);
      br = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      slf4jLogger.info("Starting to read from collectl.");
      while ((line = br.readLine()) != null) {
        if (!headersDefined) {
          defineTheHeaders(line);
          continue;
        }
        addMetricToGlobal(line);
        expireOldMetrics();
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
      // printCollectlList(line) will print all columns.
      mls = CollectlStringParser.handleHeaders(line);
      headersDefined = true;
    }
  }
  
  @Override
  protected void createCustomMetrics(ArrayList<Metric> metrics) {
    double total = metrics.get(21).getValue();
    double used = metrics.get(22).getValue();
    long timeStamp = metrics.get(0).getTimeStamp();
    Metric metric = new Metric(metrics.size(),
        ResourceType.MEM,
        MetricFormat.PERCENTAGE,
        timeStamp,
        "MemoryUsed",
        (used/total) * 100);
    metrics.add(metric);
  }
  
  private void printCollectlList(String unsplitHeader) {
    unsplitHeader = unsplitHeader.substring(5, unsplitHeader.length());
    String[] headers = CollectlStringParser.split(unsplitHeader);
    int i = 0;
    for (String header : headers) {
      System.out.println(i + " - " + header);
      i++;
    }
  }
  
}
