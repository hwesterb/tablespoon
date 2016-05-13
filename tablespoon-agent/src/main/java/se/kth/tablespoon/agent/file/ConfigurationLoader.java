/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.agent.file;

import com.fasterxml.jackson.jr.ob.JSON;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.tablespoon.agent.listeners.MetricListener;

/**
 *
 * @author henke
 */
public class ConfigurationLoader {
  
  private Configuration config;
  private static boolean hasLoadedConfig = false;
  private MetricListener metricListener;
  private final static Logger slf4jLogger = LoggerFactory.getLogger(ConfigurationLoader.class);
  
  public void loadNewConfiguration() {
    try {
      load();
      // If configuration has been loaded before it needs to be reconfigured.
      if (hasLoadedConfig) reconfigure();
      hasLoadedConfig = true;
    } catch (MissingConfigurationException e) {
      slf4jLogger.error(e.getMessage());
      if (hasLoadedConfig) slf4jLogger.debug("Configuration could not be"
          + " loaded, continues with old configuration.");
      else {
        slf4jLogger.debug("Configuration could not be loaded, exiting agent.");
        System.exit(0);
      }
    }
  }
  
  
  private void load() throws MissingConfigurationException {
    try {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      InputStream is = loader.getResourceAsStream("configuration/config.json");
      try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
        config = JSON.std.beanFrom(Configuration.class, br);
        br.close();
      }
    } catch (NullPointerException | IOException e) {
      throw new MissingConfigurationException("The configuration file could not be located.");
    }
  }
  
  
  public void setMetricListener(MetricListener metricListener) {
    this.metricListener = metricListener;
  }
  
  
  private void reconfigure() {
    if (config.isCollectlRestart()) {
      metricListener.requestRestart();
    }
  }
  
  public Configuration getConfig() {
    return config;
  }
  
}
