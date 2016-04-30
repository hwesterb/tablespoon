package se.kth.tablespoon.event.processing.handlers;

import se.kth.tablespoon.event.processing.communication.RiemannListener;

public class Start {

	public static void main(String[] args) {
		RiemannListener el = new RiemannListener(6667);
		Thread t = new Thread(el);
		t.start();
	}
	
}
