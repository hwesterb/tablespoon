package se.kth.tablespoon.agent.handlers;

import java.io.IOException;

import se.kth.tablespoon.agent.json.AgentMessage;
import se.kth.tablespoon.agent.json.CollectlMessage;
import se.kth.tablespoon.agent.json.JsonMessage;
import se.kth.tablespoon.agent.listeners.CollectlListener;

public class ConfigMessageHandler {
	
	private CollectlListener cl;
	
	public ConfigMessageHandler(CollectlListener cl) {
		this.cl = cl;
	}

	public void handleMessage(JsonMessage jm) {
		if (jm.agentMessage.changed) changeAgent(jm.agentMessage);
		if (jm.collectlMessage.changed) changeCollectl(jm.collectlMessage);
	}

	private void changeAgent(AgentMessage am) {

	}

	private void changeCollectl(CollectlMessage am) {
		try {
			cl.setCollectlConfig(am.config);
			cl.restartCollectl();
			System.out.println("Restarted collectl with new configuration.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
