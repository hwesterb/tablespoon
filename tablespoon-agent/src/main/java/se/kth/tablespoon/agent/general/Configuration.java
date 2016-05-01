package se.kth.tablespoon.agent.general;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Configuration {
  
  private String riemannHost;
  private int riemannPort;
  private int riemannReconnectionTries;
  private int riemannReconnectionTime;
  private int riemannSendRate;
  private int riemannEventTtl;
  private int riemannDereferenceTime;
  private boolean collectlRestart;
  private String collectlConfig;
  private int collectlCollectionRate;
  
  
  public Configuration() throws MissingPropertyException, MissingConfigurationException,
      NumberFormatException, BooleanFormatException {
    load();
  }
  
  public void load() throws MissingPropertyException,
      MissingConfigurationException, NumberFormatException, BooleanFormatException {
    Properties prop = new Properties();
    try {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      prop.load(loader.getResourceAsStream("configuration/config.properties"));
    } catch (IOException | NullPointerException e) {
      throw new MissingConfigurationException("The configuration file could not be located");
    } 
    readProperties(prop);
  }
  
  private void readProperties(Properties prop) throws MissingPropertyException, NumberFormatException, BooleanFormatException {
    riemannHost = assignString(prop, "riemann.host");
    riemannPort = assignInt(prop, "riemann.port");
    riemannReconnectionTries =  assignInt(prop, "riemann.reconnection.tries");
    riemannReconnectionTime = assignInt(prop, "riemann.reconnection.tries");
    riemannSendRate = assignInt(prop, "riemann.send.rate");
    riemannEventTtl = assignInt(prop, "riemann.event.ttl");
    riemannDereferenceTime = assignInt(prop, "riemann.dereference.time");
    collectlRestart = assignBoolean(prop, "collectl.restart");
    collectlConfig = assignString(prop, "collectl.config");
    collectlCollectionRate =  assignInt(prop, "collectl.collection.rate");
  }
  
  private int assignInt(Properties prop, String propertyName) throws MissingPropertyException, NumberFormatException {
    String propertyString = prop.getProperty(propertyName);
    if (propertyString == null) throw new MissingPropertyException(propertyName);
    if (!propertyString.matches("^-?\\d+$")) throw new NumberFormatException("The property " + propertyName + " is not an integer.");
    return Integer.parseInt(propertyString);
  }
  
  private String assignString(Properties prop, String propertyName) throws MissingPropertyException {
    String propertyString = prop.getProperty(propertyName);
    if (propertyString == null) throw new MissingPropertyException(propertyName);
    return propertyString;
  }
  
  private boolean assignBoolean(Properties prop, String propertyName) throws MissingPropertyException, BooleanFormatException {
    String propertyString = prop.getProperty(propertyName);
    if (propertyString == null) throw new MissingPropertyException(propertyName);
    if (false == (propertyString.equalsIgnoreCase("true") ||
        propertyString.equalsIgnoreCase("false")))
      throw new BooleanFormatException("The property " + propertyName + " is not a boolean.");
    return Boolean.parseBoolean(propertyString.toLowerCase());
  }
  
  public String getRiemannHost() {
    return riemannHost;
  }
  
  public int getRiemannPort() {
    return riemannPort;
  }
  
  public int getRiemannReconnectionTries() {
    return riemannReconnectionTries;
  }
  
  public int getRiemannReconnectionTime() {
    return riemannReconnectionTime;
  }
  
  public int getRiemannSendRate() {
    return riemannSendRate;
  }
  
  public String getCollectlConfig() {
    return collectlConfig;
  }
  
  public int getCollectlCollectionRate() {
    return collectlCollectionRate;
  }
  
  public int getRiemannEventTtl() {
    return riemannEventTtl;
  }
  
  public int getRiemannDereferenceTime() {
    return riemannDereferenceTime;
  }
  
  public boolean restartCollectl() {
    return collectlRestart;
  }
  
  
}
