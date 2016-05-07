package se.kth.tablespoon.agent.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sleep {

   private final static Logger slf4jLogger = LoggerFactory.getLogger(Sleep.class);
  
	public static void now(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			slf4jLogger.debug("Sleep interrupted.");
		}
	}
}
