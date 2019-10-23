package fr.opperdev.lotaryapi.utils;

public class Utils {
	
	public static long getMaxRam() {
		return Runtime.getRuntime().maxMemory() / 1024L / 1024L;
	}
	
	public static long getTotalRam() {
		return Runtime.getRuntime().totalMemory() / 1024L / 1024L;
	}
	
	public static long getFreeRam() {
		return Runtime.getRuntime().freeMemory() / 1024L / 1024L;
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
	
}
