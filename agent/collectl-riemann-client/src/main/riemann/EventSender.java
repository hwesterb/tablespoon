package riemann;

import java.io.IOException;
import java.util.Queue;

import com.aphyr.riemann.client.RiemannClient;

import events.EventLayout;

public class EventSender {

	private Queue<String> rowQueue;
	private EventLayout[] els;
	private RiemannClient rClient;

	public EventSender(Queue<String> rowQueue, EventLayout[] els, RiemannClient rClient) {
		this.rowQueue = rowQueue;
		this.els = els;
		this.rClient = rClient;
	}
	
	public void sendMetrics() throws IOException {
		String line = rowQueue.poll();
		String[] rowArray = StringParser.split(line);
		double timeStamp = Double.parseDouble(rowArray[0]);
		double[] numericRowArray =  new double[els.length];
		// The first column is a time stamp, and shall be included in every
		// message. Therefore it should not be part of the numericRowArray.
		for (int i = 1; i <  rowArray.length; i++) {
			numericRowArray[i-1] = Double.parseDouble(rowArray[i]);
		}
		matchWithLayoutAndSend(numericRowArray, timeStamp);
	}

	private void matchWithLayoutAndSend(double[] row, double timeStamp) throws IOException {
		for (int i = 0; i < els.length; i++) {
			rClient.event().
			service(els[i].getType().toString()).
			metric(row[i]).
			time(timeStamp).
			tags(els[i].getName(), els[i].getFormat().toString()).
			send().
			deref(5000, java.util.concurrent.TimeUnit.MILLISECONDS);
			rClient.query("tagged \"" + els[i].getName() + "\"").deref();
		}
		System.out.println("Sended metrics.");
	}


}
