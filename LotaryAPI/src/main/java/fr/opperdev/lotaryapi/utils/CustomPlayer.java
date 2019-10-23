package fr.opperdev.lotaryapi.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.opperdev.lotaryapi.Main;
import fr.opperdev.lotaryapi.utils.sql.DBManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class CustomPlayer {
	
	private Player player;
	
	private long lotas;
	private long drachmes;
	
	private static Main main = Main.instance;
	
	private static DBManager mysql = main.mysql;
	
	/**
	 * Constructor #1
	 * @param player
	 * @param lotas
	 * @param drachmes
	 */
	public CustomPlayer(Player player, long lotas, long drachmes) {
		this.player = player;
		this.lotas = lotas;
		this.drachmes = drachmes;
		if(!mysql.exists("uuid", "'"+player.getUniqueId().toString()+"'", "lotacoin")) {
			mysql.insert("name,uuid,lotas,drachmes", "'"+player.getName()+"','"+player.getUniqueId().toString()+"','"+lotas+"','"+drachmes+"'", "lotacoin");
		}
	}
	
	/**
	 * @param header
	 * @param footer
	 */
	public void setTab(String header, String footer) {
		Reflection.setTab(player, header, footer);
	}
	
	/**
	 * @param serverName
	 */
	public void connectToServer(String serverName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(serverName);
		player.sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray());
	}

	public static void connectToServer(UUID id, String serverName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(serverName);
		Bukkit.getPlayer(id).sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray());
	}
	/**
	 * @param message
	 */
	public void kickEverywere(String message) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("KickPlayer");
		out.writeUTF(player.getName());
		out.writeUTF(message);
		player.sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray());
	}
	
	/**
	 * @param message
	 */
	public void sendMessageEverywere(String message) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Message");
		out.writeUTF(player.getName());
		out.writeUTF(message);
		player.sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray());
	}
	
	/**
	 * 
	 * @return long
	 */
	public long getLotas() {
		return this.lotas;
	}
	
	/**
	 * 
	 * @return long
	 */
	public long getDrachmes() {
		return this.drachmes;
	}
	
	/**
	 * 
	 * @param lotas
	 */
	public void setLotas(long lotas) {
		this.lotas = lotas;
		mysql.update("lotas='"+lotas+"'", "uuid", "=", "'"+player.getUniqueId().toString()+"'", "lotacoin");
	}
	
	/**
	 * 
	 * @param drachmes
	 */
	public void setDrachmes(long drachmes) {
		this.drachmes = drachmes;
		mysql.update("drachmes='"+drachmes+"'", "uuid", "=", "'"+player.getUniqueId().toString()+"'", "lotacoin");
	}
	
	/**
	 * 
	 * @param player
	 * @return CustomPlayer
	 * @throws Exception
	 */
	public static CustomPlayer get(Player player) throws Exception {
		if(mysql.exists("uuid", "'"+player.getUniqueId().toString()+"'", "lotacoin")) {
			ArrayList<HashMap<String, String>> result = mysql.selectAll("uuid", "=", "'"+player.getUniqueId().toString()+"'", "lotacoin");
            final long[] lotas = {0};
            final long[] drachmes = {0};
			result.forEach(m -> {
                lotas[0] = Long.parseLong(m.get("lotas"));
                System.out.println("Lotas + " + m.get("lotas"));
                System.out.println("Lotas + " + m.get("drachmes"));
                drachmes[0] = Long.parseLong(m.get("drachmes"));
            });

			return new CustomPlayer(player, lotas[0], drachmes[0]);
		} else {
			return new CustomPlayer(player, 0L, 0L);
		}
	}

	public String getToken(){
		if(mysql.exists("token","'" + player.getUniqueId().toString() + "'","lotacoin")){
			return (String) mysql.select("uuid","=","'" + player.getUniqueId().toString() + "'","Token","lotacoin");
		}
		else {
			RandomString rnd = new RandomString(30);
			String token = rnd.nextString();
			while (mysql.exists("Token","'" + token + "'","lotacoin")){
				token = rnd.nextString();
			}
			mysql.update("Token='" + token + "'","uuid","=","'" + player.getUniqueId().toString() +"'","lotacoin");
			return token;
		}
	}
		public void setToken(String token){
			mysql.update("Token='" + token + "'","uuid","=","'" + player.getUniqueId().toString() +"'","lotacoin");
	}
		public String getGrade(){
			return Objects.requireNonNull(Main.getLuckPermsApi().getUser(player.getUniqueId())).getPrimaryGroup();
	}

    public String getGradePrefix(){
        return Objects.requireNonNull(Main.getLuckPermsApi().getUser(player.getUniqueId())).getPrimaryGroup();
    }

}



