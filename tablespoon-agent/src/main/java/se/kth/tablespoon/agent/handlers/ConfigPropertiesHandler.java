package se.kth.tablespoon.agent.handlers;

import java.util.Properties;

public class ConfigPropertiesHandler {

	private Properties prop = new Properties();;

	public void reload() throws Exception {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		prop.load(loader.getResourceAsStream("resources/config.properties"));
	}

	public Properties getProp() {
		return prop;
	}


}
