package handlers;

import java.io.IOException;

import com.aphyr.riemann.client.RiemannClient;

public class Main {
	
	private static final int TRIES = 3;
	private static final int RECONNECTION_TIME = 5000;
	private static final int WAIT_FOR_QUEUE_TO_FILL_TIME = 1000;
	private static final int WAIT_FOR_THREAD_TO_DIE_TIME = 500;
	private static final int READ_QUEUE_TIME = 100;
	private static String host;
	private static int port;
	static RiemannClient rClient;
	static CollectlHandler ch;
	static Thread t;

	public static void main(String[] args) throws InterruptedException {
		host = args[0];
		port = Integer.parseInt(args[1]);
		setUpHook();
		ch = new CollectlHandler();
		t = new Thread(ch);
		t.start();
		while(ch.queueIsEmpty()) {
			System.out.println("waiting...");
			Thread.sleep(WAIT_FOR_QUEUE_TO_FILL_TIME); 
		}
		connect(0);
		System.exit(0); //will trigger the exit handler which kills the thread
	}

	private static void connect(int tries) throws InterruptedException {
		EventSender es;
		try {
			rClient = RiemannClient.tcp(host, port);
			rClient.connect();
			System.out.println("Established connection with host:" + host + " port:" + port);
			tries = 0; //resetting number of tries
			es = new EventSender(ch.getRowQueue(), ch.getEventLayouts(), rClient);

			while (true) {
				// it apparently needs some headroom.
				Thread.sleep(READ_QUEUE_TIME);
				if (!ch.queueIsEmpty()) es.sendMetrics();
			}
		} catch (IOException e) {
			System.out.println("Connection with server could not be established.");
			if (tries < TRIES) {
				System.out.println("Waiting for " + Math.round(RECONNECTION_TIME/1000) + " seconds and attempting to connect again...");
				Thread.sleep(RECONNECTION_TIME);
				connect(++tries);
			}
		}
	}


	private static void setUpHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				if (ch != null) {    
					ch.requestInterrupt();
					while(t.isAlive()) {
						System.out.println("waiting for thread to die...");
						try { Thread.sleep(WAIT_FOR_THREAD_TO_DIE_TIME); } catch (InterruptedException e) { e.printStackTrace();}
					}
				}
				if (rClient != null) {
					if (rClient.isConnected()) {
						rClient.close(); 
						System.out.println("Closed the connection with the Riemann client.");
					}
				}
			}
		}));
	}

}
