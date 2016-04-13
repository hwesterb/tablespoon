package riemann;

import java.io.IOException;

import com.aphyr.riemann.client.RiemannClient;

public class Main {

	static RiemannClient rClient;
	static CollectlHandler ch;
	static Thread t;

	public static void main(String[] args) throws InterruptedException {	
		setUpHook();
		ch = new CollectlHandler();
		t = new Thread(ch);
		t.start();
		while(ch.queueIsEmpty()) {
			System.out.println("waiting...");
			Thread.sleep(1000); 
		}
		EventSender es;
		try {
			rClient = RiemannClient.tcp("localhost", 5555);
			rClient.connect();
			es = new EventSender(ch.getRowQueue(), ch.getEventLayouts(), rClient);
			while (true) {
				// it apparently needs some headroom.
				Thread.sleep(100);
				if (!ch.queueIsEmpty()) es.sendMetrics();
			}
		} catch (IOException e) {
			System.out.println("Connection with server could not be established.");
		}
	}


	private static void setUpHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				if (ch != null) {    
					ch.requestInterrupt();
					while(t.isAlive()) {
						System.out.println("waiting for thread to die...");
						try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace();}
					}
				}
				if (rClient != null) {
					if (rClient.isConnected()) rClient.close(); 
				}
			}
		}));
	}

}
