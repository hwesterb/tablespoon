package se.kth.tablespoon.agent.handlers;

import java.io.IOException;
import java.util.Properties;

public class Configuration {
  
  private String riemannHost;
  private int riemannPort;
  private int riemannReconnectionTries;
  private int riemannReconnectionTime;
  private int riemannSendRate;
  private String collectlConfig;
  private int collectlCollectionRate;
  
  
  public Configuration() throws MissingPropertyException, NumberFormatException, MissingConfigurationException {
    reload();
  }
  
  public void reload() throws MissingPropertyException, MissingConfigurationException, NumberFormatException {
    Properties prop = new Properties();
    try {
      prop.load(getClass().getResourceAsStream("/config.properties"));
    } catch (IOException ex) {
     throw new MissingConfigurationException("The configuration file could not be located");
    }
    readProperties(prop);
  }
  
  private void readProperties(Properties prop) throws MissingPropertyException, NumberFormatException {
    riemannHost = assignString(prop, "riemann.host");
    riemannPort = assignInt(prop, "riemann.port");
    riemannReconnectionTries =  assignInt(prop, "riemann.reconnection.tries");
    riemannReconnectionTime = assignInt(prop, "riemann.reconnection.tries");
    riemannSendRate = assignInt(prop, "riemann.collection.rate");
    collectlConfig = assignString(prop, "collectl.config");
    collectlCollectionRate =  assignInt(prop, "collectl.collection.rate");    
  }
  
  private int assignInt(Properties prop, String propertyName) throws MissingPropertyException, NumberFormatException {
    String propertyString = prop.getProperty(propertyName);
    if (propertyString == null) throw new MissingPropertyException(propertyName);
    if (!propertyString.matches("^-?\\d+$")) throw new NumberFormatException("The property " + propertyName + " is not an Integer.");
    return Integer.parseInt(propertyString);
  }
  
  private String assignString(Properties prop, String propertyName) throws MissingPropertyException {
    String propertyString = prop.getProperty(propertyName);
    if (propertyString == null) throw new MissingPropertyException(propertyName);
    return propertyString;
  }
  
  
  
  
  
}
