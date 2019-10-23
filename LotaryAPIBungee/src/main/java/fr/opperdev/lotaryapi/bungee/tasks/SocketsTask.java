package fr.opperdev.lotaryapi.bungee.tasks;

import fr.opperdev.lotaryapi.bungee.listeners.SocketsEvent;
import fr.opperdev.lotaryapi.bungee.utils.Utils;

public class SocketsTask implements Runnable {

	public void run() {
		for(String server : SocketsEvent.sockets.keySet()) {
			Utils.send(server, "INFOS", "RAM#MAX");
			Utils.send(server, "INFOS", "RAM#TOTAL");
			Utils.send(server, "INFOS", "RAM#FREE");
		}
	}

}
