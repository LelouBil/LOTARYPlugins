package fr.opperdev.lotaryapi.bungee;

import fr.opperdev.lotaryapi.bungee.listeners.SocketsEvent;
import fr.opperdev.lotaryapi.bungee.tasks.SocketsTask;
import fr.opperdev.lotaryapi.bungee.utils.sql.DBCred;
import fr.opperdev.lotaryapi.bungee.utils.sql.DBInitor;
import fr.opperdev.lotaryapi.bungee.utils.sql.DBManager;
import fr.rhaz.socketapi.server.SocketMessenger;
import fr.rhaz.socketapi.server.SocketServer;
import fr.rhaz.socketapi.server.SocketServerApp;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Main extends Plugin implements SocketServerApp {

	public static Main instance;
	
	public DBManager mysql;
	
	private Configuration config;
	private SocketServer server;
	
	private final boolean DEBUG = true;
	
	@Override
	public void onEnable() {
		if(DEBUG)print(">>>--->>> instancing...");
		instance = this;
		if(DEBUG)print(">>>--->>> opening the SQL connection...");
		mysql = new DBManager(
				new DBInitor(
						new DBCred("localhost", "lotacoins", "SWDNkwu2tdhsUzK1", "lotacoins", 3306)));
		if(DEBUG)print(">>>--->>> checking/creating yaml...");
		reload();
		if(DEBUG)print(">>>--->>> starting sockets...");
		start();
		if(DEBUG)print(">>>--->>> starting scheduler...");
		getProxy().getScheduler().schedule(this, new SocketsTask(), 1, 1, TimeUnit.MINUTES);
		if(DEBUG)print(">>>--->>> registering listeners...");
		getProxy().getPluginManager().registerListener(this, new SocketsEvent());
	}
	
	private void print(String message) {
		getProxy().getConsole().sendMessage(TextComponent.fromLegacyText(message));
	}
	
	@Override
	public void onDisable() {
		if(DEBUG)print("<<<---<<< closing the SQL connection...");
		mysql.getInitor().close();
		if(DEBUG)print("<<<---<<< stopping sockets...");
		stop();
	}
	
	public void reload(){
		config = loadConfig("config.yml");
	}
	
	public Configuration loadConfig(String name){
		if (!getDataFolder().exists()) getDataFolder().mkdir();
        File file = new File(getDataFolder(), name);
        if (!file.exists()) {
            try {
				Files.copy(this.getResourceAsStream("server.yml"), file.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
        } try {
			return ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
		} catch (IOException e) {
			e.printStackTrace();
		} return null;
	}
	
	public class BungeeSocketConnectEvent extends Event {
		private SocketMessenger mess;
		
		public BungeeSocketConnectEvent(SocketMessenger mess){
			this.mess = mess;
		}
		
		public SocketMessenger getMessenger(){
			return mess;
		}
	}
	
	public class BungeeSocketDisconnectEvent extends Event {
		private SocketMessenger mess;
		
		public BungeeSocketDisconnectEvent(SocketMessenger mess){
			this.mess = mess;
		}
		
		public SocketMessenger getMessenger(){
			return mess;
		}
	}
	
	public class BungeeSocketJSONEvent extends Event {
		private SocketMessenger mess;
		private Map<String, String> map;
		
		public BungeeSocketJSONEvent(SocketMessenger mess, Map<String, String> map){
			this.mess = mess;
			this.map = map;
		}
		
		public String getChannel(){
			return map.get("channel");
		}
		
		public String getData(){
			return map.get("data");
		}
		
		public String getName(){
			return mess.getName();
		}
		
		public SocketMessenger getMessenger(){
			return mess;
		}
		
		public void write(String data){
			mess.writeJSON(getChannel(), data);
		}
	}
	
	public class BungeeSocketHandshakeEvent extends Event {
		private String name;
		private SocketMessenger mess;
		
		public BungeeSocketHandshakeEvent(SocketMessenger mess, String name){
			this.mess = mess;
			this.name = name;
		}
		
		public SocketMessenger getMessenger(){
			return mess;
		}
		
		public String getName(){
			return name;
		}
	}
	
	public SocketServer getSocketServer(){
		return server;
	}
	
	public boolean start(){
		server = new SocketServer(config.getString("name", "Bungee"), instance, config.getInt("port", 25575), config.getInt("security-level", 1));
		IOException err = server.start();
		if(err != null){
			getLogger().warning("Could not start socket server on port "+server.getPort());
			err.printStackTrace();
			return false;
		} else {
			getLogger().info("Successfully started socket server on port "+server.getPort());
			return true;
		}
	}
	
	public boolean stop(){
		IOException err = server.close();
		if(err != null){
			getLogger().warning("Could not stop socket server on port "+server.getPort());
			err.printStackTrace();
			return false;
		} else {
			getLogger().info("Successfully stopped socket server on port "+server.getPort());
			return true;
		}
	}
	
	public void restart(){
		if(stop()){
			getProxy().getScheduler().schedule(this, new Runnable(){
				public void run() {
					start();
				}
			}, 1000, TimeUnit.MILLISECONDS);
		}
	}
	
	public void run(SocketServer server){
		getProxy().getScheduler().runAsync(instance, server);
	}
		
	public void run(SocketMessenger mess){
		getProxy().getScheduler().runAsync(instance, mess);
	}
	
	public void onConnect(SocketMessenger mess) {
		getProxy().getPluginManager().callEvent(new BungeeSocketConnectEvent(mess));
	}
	
	public void onHandshake(SocketMessenger mess, String name) {
		getProxy().getPluginManager().callEvent(new BungeeSocketHandshakeEvent(mess, name));
	}

	public void onJSON(SocketMessenger mess, Map<String, String> map) {
		getProxy().getPluginManager().callEvent(new BungeeSocketJSONEvent(mess, map));
	}

	public void onDisconnect(SocketMessenger mess) {
		getProxy().getPluginManager().callEvent(new BungeeSocketDisconnectEvent(mess));
	}

	public void log(String err) {
		if(config.getBoolean("debug", false)) getProxy().getLogger().info(err);
	}
	
}
