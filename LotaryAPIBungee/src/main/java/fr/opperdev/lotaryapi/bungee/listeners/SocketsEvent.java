package fr.opperdev.lotaryapi.bungee.listeners;

import fr.opperdev.lotaryapi.bungee.Main;
import fr.opperdev.lotaryapi.bungee.Main.BungeeSocketHandshakeEvent;
import fr.opperdev.lotaryapi.bungee.Main.BungeeSocketJSONEvent;
import fr.opperdev.lotaryapi.bungee.utils.Utils;
import fr.rhaz.socketapi.server.SocketMessenger;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SocketsEvent implements Listener {
	
	public static Map<String, SocketMessenger> sockets = new HashMap<String, SocketMessenger>();
	
	private final boolean DEBUG = true;
	
    @EventHandler
    public void onHandshake(BungeeSocketHandshakeEvent e){
    	if(DEBUG)ProxyServer.getInstance().getConsole().sendMessage(TextComponent.fromLegacyText("*-------------* HandshakeEvent > " + e.getName() + " *--------------*"));
        sockets.put(e.getName(), e.getMessenger());
    }
    
    @EventHandler
    public void onMessage(BungeeSocketJSONEvent e){
    	if(DEBUG)ProxyServer.getInstance().getConsole().sendMessage(TextComponent.fromLegacyText("*-------------* SocketJSONEvent > " + e.getName() + " > " + e.getChannel() + " > " + e.getData() +" *--------------*"));
        if(e.getChannel().equals("INFOS")){
            if(e.getData().startsWith("RAM")){
                String[] parts = e.getData().split("#");
                long ram = Long.parseLong(parts[2]);
				switch (parts[1]) {
					case "MAX":
						Utils.RamCache.getRamCache(e.getName()).setMax(ram);
						break;
					case "FREE":
						Utils.RamCache.getRamCache(e.getName()).setFree(ram);
						break;
					case "TOTAL":
						Utils.RamCache.getRamCache(e.getName()).setTotal(ram);
						break;
				}
            }
        }
    }
    
    @EventHandler
    public void onLogin(PreLoginEvent e) {
    	if(DEBUG)ProxyServer.getInstance().getConsole().sendMessage(TextComponent.fromLegacyText("*-------------* PreLoginEvent > " + e.getConnection().getName() + " > HostString : " + e.getConnection().getVirtualHost().getHostString() + " > HostName : " + e.getConnection().getVirtualHost().getHostName() + " *--------------*"));
    	if(!e.getConnection().getVirtualHost().getHostString().equals("play.lotary.net")) {
    		e.setCancelReason(new ComponentBuilder("§cMerci de vous connecter avec l'adresse \"§6play.lotary.net§c\" !").create());
    		e.setCancelled(true);
    	}
    }
    
    public static boolean maint = true;
    public static String maintReason = "§eServeur en développement.";
    
    public static String motdBase = "§6§lLOTARY ";
    
    private String sep = "§5}=----------~----------={";
    
    @EventHandler
    public void onPing(ProxyPingEvent e) {
    	String name = e.getConnection().getName();
    	if(DEBUG)ProxyServer.getInstance().getConsole().sendMessage(TextComponent.fromLegacyText("*-------------* ProxyPingEvent > "+ name + " *--------------*"));
    	ServerPing ping = e.getResponse();
    	ServerPing.Protocol ver = ping.getVersion();
    	ServerPing.Players players = ping.getPlayers();
    	ver.setName("§aVersions : §b1.7.X §a> §b1.12.X");
    	ver.setProtocol(ping.getVersion().getProtocol());
    	players.setOnline(987);
    	players.setMax(100000);
    	if(!maint) {
    		players.setSample(new ServerPing.PlayerInfo[] {
    				new ServerPing.PlayerInfo(sep, UUID.randomUUID()),
    				new ServerPing.PlayerInfo("", UUID.randomUUID()),
    				new ServerPing.PlayerInfo("§9Salut "+name+",", UUID.randomUUID()),
    				new ServerPing.PlayerInfo("", UUID.randomUUID()),
    				new ServerPing.PlayerInfo("§eJoueurs connectés sur le lobby : "+Main.instance.getProxy().getServerInfo("lobby").getPlayers().size()
    						, UUID.randomUUID()),
    				new ServerPing.PlayerInfo("§eJoueurs connectés sur le faction : "+Main.instance.getProxy().getServerInfo("factions").getPlayers().size()
    						, UUID.randomUUID()),
    				new ServerPing.PlayerInfo("§eJoueurs connectés sur le practice : "+Main.instance.getProxy().getServerInfo("practice").getPlayers().size()
    						, UUID.randomUUID()),
    				new ServerPing.PlayerInfo("", UUID.randomUUID()),
    				new ServerPing.PlayerInfo("§bSite : https://www.lotary.net/", UUID.randomUUID()),
    				new ServerPing.PlayerInfo("§bTeamspeak : ts.lotary.net", UUID.randomUUID()),
    				new ServerPing.PlayerInfo("§bDiscord : https://discord.lotary.net/", UUID.randomUUID()),
    				new ServerPing.PlayerInfo("", UUID.randomUUID()),
    				new ServerPing.PlayerInfo(sep, UUID.randomUUID()),
    		});
    		ping.setDescriptionComponent(new TextComponent(motdBase+" §4> §cSalut §b"+name+" §c! §4> §cMOTD BASE"));
    	} else {
    		players.setSample(new ServerPing.PlayerInfo[] {
    				new ServerPing.PlayerInfo(sep, UUID.randomUUID()),
    				new ServerPing.PlayerInfo("", UUID.randomUUID()),
    				new ServerPing.PlayerInfo("§9Salut "+name+",", UUID.randomUUID()),
    				new ServerPing.PlayerInfo("", UUID.randomUUID()),
    				new ServerPing.PlayerInfo("§eMaintenance en cours.", UUID.randomUUID()),
    				new ServerPing.PlayerInfo("§eRaison : "+maintReason, UUID.randomUUID()),
    				new ServerPing.PlayerInfo("", UUID.randomUUID()),
    				new ServerPing.PlayerInfo("§bSite : https://www.lotary.net/", UUID.randomUUID()),
    				new ServerPing.PlayerInfo("§bTeamspeak : ts.lotary.net", UUID.randomUUID()),
    				new ServerPing.PlayerInfo("§bDiscord : https://discord.lotary.net/", UUID.randomUUID()),
    				new ServerPing.PlayerInfo("", UUID.randomUUID()),
    				new ServerPing.PlayerInfo(sep, UUID.randomUUID()),
    		});
    		ping.setDescriptionComponent(new TextComponent(motdBase+" §4> §cComing soon... §b(Hello "+name+")"));
    	}
    	ping.setPlayers(players);
    	ping.setVersion(ver);
    	e.setResponse(ping);
    }
	
}
