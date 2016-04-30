package se.kth.tablespoon.agent.handlers;

import java.io.IOException;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aphyr.riemann.client.RiemannClient;

import se.kth.tablespoon.agent.events.EventLayout;

public class EventSender {

	private Queue<String> rowQueue;
	private EventLayout[] els;
	private RiemannClient rClient;
	private final Logger slf4jLogger = LoggerFactory.getLogger(Main.class);

	public EventSender(Queue<String> rowQueue, EventLayout[] els, RiemannClient rClient) {
		this.rowQueue = rowQueue;
		this.els = els;
		this.rClient = rClient;
	}

	public void sendMetrics() throws IOException {
		synchronized (rowQueue) {
			String line = rowQueue.poll();
			String[] rowArray = CollectlStringParser.split(line);
			long timeStamp = Math.round(Double.parseDouble(rowArray[0]));
			double[] numericRowArray =  new double[els.length];
			// The first column is a time stamp, and shall be included in every
			// message. Therefore it should not be part of the numericRowArray.
			for (int i = 1; i <  rowArray.length; i++) {
				numericRowArray[i-1] = Double.parseDouble(rowArray[i]);
			}
			matchWithLayoutAndSend(numericRowArray, timeStamp);
		}
	}

	private void matchWithLayoutAndSend(double[] row, long timeStamp) throws IOException {
		long ttl = 5;
		long now = System.currentTimeMillis() / 1000L;
		if (now - timeStamp > ttl) {
			slf4jLogger.info("Metric was too old and discarded.");
			return;
		}
		for (int i = 0; i < els.length; i++) {
			rClient.event().
			service(els[i].getType().toString()).
			description(els[i].getName()).
			tag(els[i].getFormat().toString()).
			metric(row[i]).
			time(timeStamp).
			ttl(ttl).
			send().
			deref(5000, java.util.concurrent.TimeUnit.MILLISECONDS);
		}
		slf4jLogger.info("Sent metrics.");
	}


}
