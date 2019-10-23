package fr.opperdev.lotaryapi.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import fr.opperdev.lotaryapi.Main.BukkitSocketJSONEvent;
import fr.opperdev.lotaryapi.utils.Utils;

public class SocketsEvent implements Listener {
	
	@EventHandler
	public void onMessage(BukkitSocketJSONEvent e) {
		if(e.getChannel().equals("INFOS")) {
			if(e.getData().startsWith("RAM")) {
				String ramType = e.getData().split("#")[1];
				switch (ramType) {
					case "MAX":
						e.write("RAM#MAX#" + Utils.getMaxRam());
						break;
					case "TOTAL":
						e.write("RAM#TOTAL#" + Utils.getTotalRam());
						break;
					case "FREE":
						e.write("RAM#FREE#" + Utils.getFreeRam());
						break;
				}
			}
		}
	}
	
	
	
}
