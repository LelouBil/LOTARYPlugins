package fr.opperdev.lotaryapi.bungee.utils;

import fr.opperdev.lotaryapi.bungee.Main;
import fr.opperdev.lotaryapi.bungee.utils.sql.DBManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomPP {
	
	private ProxiedPlayer player;
	
	private long lotas;
	private long drachmes;
	
	private static Main main = Main.instance;
	
	private static DBManager mysql = main.mysql;
	
	public CustomPP(ProxiedPlayer p, long l, long d) {
		player = p;
		lotas = l;
		drachmes = d;
		if(!mysql.exists("uuid", "'"+p.getUniqueId().toString()+"'", "lotacoin")) {
			mysql.insert("name,uuid,lotas,drachmes", "'"+p.getName()+"','"+p.getUniqueId().toString()+"','"+l+"','"+d+"'", "lotacoin");
		}
	}
	
	public ProxiedPlayer getPlayer() {
		return player;
	}
	
	public void sendActionBar(String message) {
		if(player.getPendingConnection().getVersion() < 47)return;
		player.sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(message).create());
	}
	
	public void sendTitle(String title, String subTitle, int stay, int fadeIn, int fadeOut) {
		if(player.getPendingConnection().getVersion() < 47)return;
		Title bT = ProxyServer.getInstance().createTitle();
		bT.title(new ComponentBuilder(title).create());
		bT.subTitle(new ComponentBuilder(subTitle).create());
		bT.stay(stay);
		bT.fadeIn(fadeIn);
		bT.fadeOut(fadeOut);
		bT.send(player);
	}
	
	public void setTab(String header, String footer) {
		if(player.getPendingConnection().getVersion() < 47)return;
		player.resetTabHeader();
		player.setTabHeader(new ComponentBuilder(header).create(), new ComponentBuilder(footer).create());
	}
	
	public void connectToServer(String serverName) {
		player.connect(ProxyServer.getInstance().getServerInfo(serverName));
	}
	
	public long getLotas() {
		return this.lotas;
	}
	
	public long getDrachmes() {
		return this.drachmes;
	}
	
	public void setLotas(long lotas) {
		this.lotas = lotas;
		mysql.update("lotas='"+lotas+"'", "uuid", "=", "'"+player.getUniqueId().toString()+"'", "lotacoin");
	}
	
	public void setDrachmes(long drachmes) {
		this.drachmes = drachmes;
		mysql.update("drachmes='"+drachmes+"'", "uuid", "=", "'"+player.getUniqueId().toString()+"'", "lotacoin");
	}
	
	public static CustomPP get(ProxiedPlayer player) throws Exception {
		if(mysql.exists("uuid", "'"+player.getUniqueId().toString()+"'", "lotacoin")) {
			ArrayList<HashMap<String, String>> result = mysql.selectAll("uuid", "=", "'"+player.getUniqueId().toString()+"'", "lotacoin");
			final long lotas[] = {0};
			final long drachmes[] = {0};
            result.forEach(m -> {
                lotas[0] = Long.parseLong(m.get("lotas"));
                drachmes[0] = Long.parseLong(m.get("drachmes"));
            });
            return new CustomPP(player, lotas[0], drachmes[0]);
		} else {
			return new CustomPP(player, 0L, 0L);
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
}
