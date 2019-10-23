package fr.opperdev.lotaryapi;

import fr.opperdev.lotaryapi.listeners.SocketsEvent;
import fr.opperdev.lotaryapi.utils.VaultEconomy;
import fr.opperdev.lotaryapi.utils.sql.DBCred;
import fr.opperdev.lotaryapi.utils.sql.DBInitor;
import fr.opperdev.lotaryapi.utils.sql.DBManager;
import fr.rhaz.socketapi.client.SocketClient;
import fr.rhaz.socketapi.client.SocketClientApp;
import lombok.Getter;
import me.lucko.luckperms.api.LuckPermsApi;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Main extends JavaPlugin implements SocketClientApp {
	
	public static Main instance;
	
	public DBManager mysql;
	
	private Configuration config;
	private SocketClient client;

	@Getter
	private static LuckPermsApi luckPermsApi;
	
	public static void main(String[] args) {
		
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		instance = this;
		mysql = new DBManager(
				new DBInitor(
						new DBCred("localhost", "lotacoins", "SWDNkwu2tdhsUzK1", "lotacoins", 3306)));
		reload();
		start();
		RegisteredServiceProvider<LuckPermsApi> provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi.class);
		if (provider == null) {
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		Main.luckPermsApi = provider.getProvider();
		getServer().getPluginManager().registerEvents(new SocketsEvent(), this);
		VaultEconomy.setEnabled(true);
		Bukkit.getServicesManager().register(Economy.class,new VaultEconomy(),Bukkit.getPluginManager().getPlugin("Vault"), ServicePriority.Normal);
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		mysql.getInitor().close();
		stop();
	}
	
	public void reload() {
		config = loadConfig("config.yml");
	}
	
	public void start() {
		client = new SocketClient(this, config.getString("name", "Spigot"), config.getString("host", "localhost"), config.getInt("port", 25575), config.getInt("security-level", 1));
		getServer().getScheduler().runTaskAsynchronously(this, client);
	}
	
	public boolean stop() {
		IOException err = client.interrupt();
		if(err != null) {
			getLogger().warning("Could not stop socket client on port "+client.getPort());
			err.printStackTrace();
			return false;
		} else {
			getLogger().info("Successfully stopped socket client on port "+client.getPort());
			return true;
		}
	}
	
	public void restart() {
		if(stop())start();
	}
	
	public Configuration loadConfig(String name) {
		if (!getDataFolder().exists()) getDataFolder().mkdir();
        File file = new File(getDataFolder(), name);
        if (!file.exists()) saveResource(name, false);
        return YamlConfiguration.loadConfiguration(file);
	}
	
	public static class BukkitSocketConnectEvent extends Event {
		private final static HandlerList handlers = new HandlerList();
		private SocketClient client;
		
		public BukkitSocketConnectEvent(SocketClient client) {
			this.client = client;
		}

		public SocketClient getClient(){
			return client;
		}
		
		@Override
		public HandlerList getHandlers() {
			return handlers;
		}
		
		public static HandlerList getHandlerList(){
			return handlers;
		}
	}
	
	public static class BukkitSocketDisconnectEvent extends Event {
		private final static HandlerList handlers = new HandlerList();
		private SocketClient client;
		
		public BukkitSocketDisconnectEvent(SocketClient client) {
			this.client = client;
		}

		public SocketClient getClient(){
			return client;
		}
		
		@Override
		public HandlerList getHandlers() {
			return handlers;
		}
		
		public static HandlerList getHandlerList(){
			return handlers;
		}
	}
	
	public static class BukkitSocketHandshakeEvent extends Event {
		private final static HandlerList handlers = new HandlerList();
		private SocketClient client;
		
		public BukkitSocketHandshakeEvent(SocketClient client) {
			this.client = client;
		}

		public SocketClient getClient(){
			return client;
		}
		
		@Override
		public HandlerList getHandlers() {
			return handlers;
		}
		
		public static HandlerList getHandlerList(){
			return handlers;
		}
	}
	
	public static class BukkitSocketJSONEvent extends Event {
		private final static HandlerList handlers = new HandlerList();
		private Map<String, String> map;
		private SocketClient client;

		public BukkitSocketJSONEvent(SocketClient client, Map<String, String> map) {
			this.map = map;
			this.client = client;
		}
		
		public SocketClient getClient(){
			return client;
		}
		
		public String getChannel(){
			return map.get("channel");
		}
		
		public String getData(){
			return map.get("data");
		}
		
		public void write(String data){
			client.writeJSON(getChannel(), data);
		}
		
		@Override
		public HandlerList getHandlers() {
			return handlers;
		}
		
		public static HandlerList getHandlerList(){
			return handlers;
		}
	}
	
	public SocketClient getSocketClient(){
		return client;
	}
	
	public void onConnect(SocketClient client) {
		getLogger().info("Successfully connected to "+client.getHost()+" on port "+client.getPort());
		getServer().getPluginManager().callEvent(new BukkitSocketConnectEvent(client));
	}

	public void onDisconnect(SocketClient client) {
		getLogger().warning("Disconnected from "+client.getHost()+" on port "+client.getPort());
		getServer().getPluginManager().callEvent(new BukkitSocketDisconnectEvent(client));
	}
	public void onHandshake(SocketClient client) {
		getServer().getPluginManager().callEvent(new BukkitSocketHandshakeEvent(client));
	}
	public void onJSON(SocketClient client, Map<String, String> map) {
		getServer().getPluginManager().callEvent(new BukkitSocketJSONEvent(client, map));
	}
	public void log(String err) {
		if(config.getBoolean("debug", false)) getServer().getLogger().info(err);
	}
	
}