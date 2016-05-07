package se.kth.tablespoon.client.util;

public class Sleep {

	public static void now(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			System.out.println("Sleep interrupted.");
		}
	}
}
