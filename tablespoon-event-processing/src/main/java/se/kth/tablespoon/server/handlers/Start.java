package se.kth.tablespoon.server.handlers;

import se.kth.tablespoon.server.communication.RiemannListener;

public class Start {

	public static void main(String[] args) {
		RiemannListener el = new RiemannListener(6667);
		Thread t = new Thread(el);
		t.start();
	}
	
}
