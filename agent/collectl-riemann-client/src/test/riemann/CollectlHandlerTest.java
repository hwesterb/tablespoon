package riemann;


import static org.junit.Assert.*;

import org.junit.Test;

import events.EventLayout;
import events.Format;
import handlers.CollectlHandler;

public class CollectlHandlerTest {

	@Test
	public void test() {
		CollectlHandler ch = new CollectlHandler();
		Thread t = new Thread(ch);
		t.start();
		while(ch.queueIsEmpty()) {
			System.out.println("waiting...");
			try {
				Thread.sleep(1000); 
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
		assertEquals("User", ch.getEventLayouts()[0].getName());
		assertEquals(Format.PERSEC, ch.getEventLayouts()[9].getFormat());
		int i = 0;
		for (EventLayout el : ch.getEventLayouts()) {
			assertNotNull("Iteration " + i + " failed.", el.getType());
			i++;
		}
		ch.requestInterrupt();
		try {
			Thread.sleep(1000); 
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		assertFalse(t.isAlive());
	}

}
