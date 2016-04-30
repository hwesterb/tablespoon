package se.kth.tablespoon.agent.listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.Gson;

import se.kth.tablespoon.agent.handlers.ConfigMessageHandler;
import se.kth.tablespoon.agent.json.JsonMessage;

public class ConfigListener implements Runnable {

	private boolean interruptRequest = false;
	private boolean handlingMessage = false;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private int portNumber;
	private ConfigMessageHandler cmh;

	public ConfigListener(int portNumber, ConfigMessageHandler cmh) {
		this.portNumber = portNumber;
		this.cmh = cmh;
	}

	public void listen(int portNumber) throws IOException {
		//the last parameter restricts access outside localhost
		serverSocket = new ServerSocket(portNumber, 0, InetAddress.getByName("localhost"));
		Gson gson = new Gson();
		while (true) {
			clientSocket = serverSocket.accept();
			if (interruptRequest) break;
			handlingMessage = true;
			BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));			
			JsonMessage jm = gson.fromJson(br, JsonMessage.class);
			cmh.handleMessage(jm);
			clientSocket.close();
			handlingMessage = false;
		}
		serverSocket.close();
	}

	@Override
	public void run() {
		try {
			listen(portNumber);
		} catch (IOException e) {
			System.out.println("Exception caught: " + e.getMessage() + ".");
		}
		System.out.println("Ending socket-thread.");
	}
	
	//this is called from another thread
	public void requestInterrupt()  {
		//the other thread requests interrupt
		interruptRequest = true;
		
		//if the other thread is busy accepting
		try {
			if (clientSocket!=null && !clientSocket.isClosed() && !handlingMessage) clientSocket.close();
			if (serverSocket!=null && !serverSocket.isClosed()) serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}