package se.kth.tablespoon.agent.listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import se.kth.tablespoon.agent.general.CollectlStringParser;
import se.kth.tablespoon.agent.events.Configuration;
import se.kth.tablespoon.agent.events.Rate;
import se.kth.tablespoon.agent.events.RaterInterpreter;

public class CollectlListener extends MetricListener {
  
  
  private String[] generateCollectlCommand() {
    return new String[]{
      "/bin/sh",
      "-c",
      "collectl -P -o U -i " + RaterInterpreter.getNumber(config.getCollectlCollectionRate())
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
        emptyOldMetrics();
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
  
}
