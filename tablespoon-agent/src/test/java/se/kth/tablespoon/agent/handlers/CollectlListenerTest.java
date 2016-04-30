package se.kth.tablespoon.agent.handlers;


import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import se.kth.tablespoon.agent.events.EventLayout;
import se.kth.tablespoon.agent.events.Format;
import se.kth.tablespoon.agent.listeners.CollectlListener;
import se.kth.tablespoon.agent.util.Sleep;

public class CollectlListenerTest {
	
	private static final String DEFAULT_COLLECTl_CONFIG = "collectl -P -o U";

	@Test
	public void test() {
		CollectlListener cl = new CollectlListener();
		cl.setCollectlConfig(DEFAULT_COLLECTl_CONFIG);
		Thread t = new Thread(cl);
		t.start();
		while(cl.queueIsEmpty()) {
			System.out.println("waiting for queue to fill...");
			Sleep.now(1000);
		}
		assertEquals("User", cl.getEventLayouts()[0].getName());
		assertEquals(Format.PERSEC, cl.getEventLayouts()[9].getFormat());
		int i = 0;
		for (EventLayout el : cl.getEventLayouts()) {
			assertNotNull("Iteration " + i + " failed.", el.getType());
			i++;
		}
		cl.requestInterrupt();
		
		while(cl.isRunning()) {
			System.out.println("waiting for thread to die...");	
			Sleep.now(1000);
		}
		
		assertFalse(t.isAlive());
	}
	
	@Test
	public void restartTest() {
		CollectlListener cl = new CollectlListener();
		cl.setCollectlConfig(DEFAULT_COLLECTl_CONFIG);
		Thread t = new Thread(cl);
		t.start();
		
		while(cl.queueIsEmpty()) {
			System.out.println("waiting for queue to fill...");	
			Sleep.now(1000);
		}
	
		try { cl.restartCollectl(); } catch (IOException e) { System.out.println(e.getMessage()); }
	
		
		while(cl.isRunning()) {
			System.out.println("waiting for thread to die...");	
			Sleep.now(1000);
		}
		
		cl.requestInterrupt();
		try { Thread.sleep(1000); } catch(InterruptedException e) { e.printStackTrace(); }
		assertFalse(t.isAlive());
	}
	
	

}
