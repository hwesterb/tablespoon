package se.kth.tablespoon.agent.general;

//package se.kth.tablespoon.agent.handlers;
//
//import static org.junit.Assert.assertFalse;
//
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.net.Socket;
//import java.nio.charset.StandardCharsets;
//
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import com.google.gson.Gson;
//
//import se.kth.tablespoon.agent.json.JsonMessage;
//import se.kth.tablespoon.agent.listeners.CollectlListener;
//import se.kth.tablespoon.agent.listeners.ConfigListener;
//import se.kth.tablespoon.agent.util.Sleep;
//
//public class ConfigListenerTest {
//
//	private static ConfigListener configListener;
//	private static CollectlListener collectlListener;
//	private static Thread t1;
//	private static Thread t2;
//	private static Gson gson;
//	private static final int PORT_NUMBER = 4444;
//
//	@BeforeClass
//	public static void beforeClass() {
//		collectlListener = new CollectlListener();
//		collectlListener.setCollectlConfig("collectl -P -o U");
//		t1 = new Thread(collectlListener);
//		t1.start();
//		while(collectlListener.queueIsEmpty()) {
//			System.out.println("waiting...");	
//			Sleep.now(1000);
//		}
//		ConfigMessageHandler cmh = new ConfigMessageHandler(collectlListener);
//		configListener = new ConfigListener(PORT_NUMBER, cmh);
//		t2 = new Thread(configListener);
//		t2.start();
//		gson = new Gson();
//	}
//
//	@AfterClass
//	public static void afterClass() {
//		collectlListener.requestInterrupt();
//		Sleep.now(1000);
//		assertFalse(t1.isAlive());
//		configListener.requestInterrupt();
//		Sleep.now(1000);
//		assertFalse(t2.isAlive());
//	}
//
//
//	@Test
//	public void simple() {
//
//
//	}
//	
//	@Test
//	public void viaSocket() {
//		
//		sendMessage();
//		
////		try { Thread.sleep(10000); } catch(InterruptedException e) { e.printStackTrace(); }
////		sendMessage();
//		
//
//	}
//
//	@Test
//	public void viaConsole() {
////		String[] cmd = {
////				"/bin/sh",
////				"nc localhost " + PORT_NUMBER + " "  + gson.toJson(setUp())
////		};
////		Process process;
////		try {
////			process = Runtime.getRuntime().exec(cmd);
////			process.destroy();
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}	
//	}
//	
//	private void sendMessage() {
//		try {
//			Socket s = new Socket("localhost", PORT_NUMBER);
//			try (OutputStreamWriter out = new OutputStreamWriter(
//					s.getOutputStream(), StandardCharsets.UTF_8)) {
//				out.write(gson.toJson(setUp()));
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//
//	private JsonMessage setUp() {
//		JsonMessage jm = new JsonMessage();
//		jm.agentMessage.name = "henrik";
//		jm.collectlMessage.name = "frans";
//		return jm;
//	}
//}
