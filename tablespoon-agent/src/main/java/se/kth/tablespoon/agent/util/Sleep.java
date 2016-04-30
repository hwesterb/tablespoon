package se.kth.tablespoon.agent.util;

public class Sleep {

	public static void now(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			System.out.println("Sleep interrupted.");
		}
	}
}
