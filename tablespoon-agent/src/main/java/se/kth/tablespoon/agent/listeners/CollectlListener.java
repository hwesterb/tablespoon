package se.kth.tablespoon.agent.listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.kth.tablespoon.agent.events.EventLayout;
import se.kth.tablespoon.agent.handlers.CollectlStringParser;
import se.kth.tablespoon.agent.main.Start;

public class CollectlListener implements Runnable {


	private Queue<String> rowQueue = new LinkedList<String>();
	private Process process;
	private BufferedReader br;
	private EventLayout[] els;
	private boolean headersDefined = false;
	private boolean interruptRequest = false;
	private boolean restartRequest = false;
	private boolean running = true;
	private String collectlConfig;
	private final Logger slf4jLogger = LoggerFactory.getLogger(Start.class);


	public void requestInterrupt()  {
		interruptRequest = true;
	}

	@Override
	public void run() {
		try {
			startCollectl();
		} catch (IOException e) {
			e.printStackTrace();
		}
		running = false;
		slf4jLogger.info("Ending collectl-thread.");
	}

	private void startCollectl() throws IOException {
		String[] cmd = {
				"/bin/sh",
				"-c",
				collectlConfig
		};
		process = Runtime.getRuntime().exec(cmd);
		br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		while ((line = br.readLine()) != null) {
			slf4jLogger.info("Reading from collectl.");
			// define the headers
			if (!headersDefined) {
				if (line.startsWith("#")) {
					synchronized (rowQueue) {
						els = CollectlStringParser.handleHeaders(line);
						rowQueue = new LinkedList<String>();
					}
					headersDefined = true;
				}
				continue;
			}
			// add a row to queue
			rowQueue.add(line);
			if (interruptRequest) break;
		}
		stopCollectl();
		synchronized (this) {
			if (restartRequest) {
				restartRequest = false;
				startCollectl();
			}
		}

	}

	public void stopCollectl() throws IOException {
		br.close();
		process.destroy();
		headersDefined = false;
		slf4jLogger.info("Ending collectl-process.");
	}

	public void restartCollectl() throws IOException {
		synchronized (this) {
			requestInterrupt();
			restartRequest = true;
		}
	}

	public String getCollectlConfig() {
		return collectlConfig;
	}

	public void setCollectlConfig(String collectlConfig) {
		this.collectlConfig = collectlConfig;
	}

	public Queue<String> getRowQueue() {
		return rowQueue;
	}	

	public EventLayout[] getEventLayouts() {
		return els;
	}

	public boolean queueIsEmpty() {
		return rowQueue.isEmpty();
	}
	
	public boolean isRunning() {
		return running;
	}
}
