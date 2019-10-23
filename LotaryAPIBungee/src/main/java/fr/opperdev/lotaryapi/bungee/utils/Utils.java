package fr.opperdev.lotaryapi.bungee.utils;

import java.util.HashMap;
import java.util.Map;

import fr.opperdev.lotaryapi.bungee.listeners.SocketsEvent;
import fr.rhaz.socketapi.server.SocketMessenger;

public class Utils {
	
	public enum RamType {
		MAX,FREE,TOTAL;
	}
	
	private static Map<String, SocketMessenger> sockets = SocketsEvent.sockets;
	
	public static long getRam(RamType type, String server) {
		switch(type) {
			case MAX:
				return RamCache.getRamCache(server).getMax();
			case FREE:
				return RamCache.getRamCache(server).getFree();
			case TOTAL:
				return RamCache.getRamCache(server).getTotal();
			default:return 0L;
		}
	}
	
	public static void send(String server, String channel, String message) {
        if(sockets.containsKey(server) && sockets.get(server).isHandshaked() && sockets.get(server).isConnectedAndOpened()){
            sockets.get(server).writeJSON(channel, message);
        }
    }
	
	public static ServerPing getServerPing(String serverName) {
		for(ServerPingList spl : ServerPingList.values()) {
			if(spl.getServerName().equalsIgnoreCase(serverName)) {
				return spl.getServerPing();
			}
		}
		return null;
	}
	
	public enum ServerPingList {
		LOBBY("lobby", new ServerPing("localhost", 25566)),
		PRACTICE("practice", new ServerPing("localhost", 25567)),
		FACTIONS("factions", new ServerPing("localhost", 25568)),
		DEV("dev", new ServerPing("localhost", 25569)),
		OPENING("opening", new ServerPing("localhost", 25570)),
		BUILD("build", new ServerPing("localhost", 25571));
		
		private String serverName;
		private ServerPing serverPing;
		
		ServerPingList(String sN, ServerPing sP){
			serverName = sN;
			serverPing = sP;
		}
		
		public String getServerName() {
			return serverName;
		}
		
		public ServerPing getServerPing() {
			return serverPing;
		}
		
	}
	
	public static class RamCache {
		String serverName;
		long maxRam;
		long freeRam;
		long totalRam;
		public static Map<String, RamCache> caches = new HashMap<String, RamCache>();
		public RamCache(String serverName, long max, long free, long total) {
			maxRam = max;
			freeRam = free;
			totalRam = total;
			caches.put(serverName, this);
		}
		public long getMax() {
			return maxRam;
		}
		public long getFree() {
			return freeRam;
		}
		public long getTotal() {
			return totalRam;
		}
		public void setMax(long max) {
			maxRam = max;
		}
		public void setFree(long free) {
			freeRam = free;
		}
		public void setTotal(long total) {
			totalRam = total;
		}
		public static RamCache getRamCache(String serverName) {
			if(caches.containsKey(serverName)) {
				return caches.get(serverName);
			} else return new RamCache(serverName, 0L, 0L, 0L);
		}
	}
	
}
