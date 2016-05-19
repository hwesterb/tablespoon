package se.kth.tablespoon.agent.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Time {
  
  private final static Logger slf4jLogger = LoggerFactory.getLogger(Time.class);
  
  public static void sleep(int ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      slf4jLogger.debug("Sleep interrupted.");
    }
  }
  
  public static long now() {
    return System.currentTimeMillis() / 1000L;
  }
}
