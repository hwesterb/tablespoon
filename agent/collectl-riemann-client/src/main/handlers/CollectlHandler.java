package handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

import events.EventLayout;

public class CollectlHandler implements Runnable {

	
	private Queue<String> rowQueue = new LinkedList<String>();
	private EventLayout[] els;
	private boolean headersDefined = false;
	private boolean interruptRequest = false;


	private void collect() throws IOException {
		String[] cmd = {
				"/bin/sh",
				"-c",
				"collectl -P -o U"
		};
		Process process;
		BufferedReader output;
		process = Runtime.getRuntime().exec(cmd);
		output = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		while ((line = output.readLine()) != null) {
			System.out.println("Reading from collectl.");
			// define the headers
			if (!headersDefined) {
				if (line.startsWith("#")) {
					els = StringParser.handleHeaders(line);
					headersDefined = true;
				}
				continue;
			}
			// add a row to queue
			rowQueue.add(line);
			if (interruptRequest) break;
		}
		output.close();
		process.destroy();
	}

	public void requestInterrupt()  {
		interruptRequest = true;
	}

	@Override
	public void run() {
		try {
			collect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Ending collectl-thread.");
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

}
