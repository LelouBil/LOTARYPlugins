package fr.opperdev.lotaryapi.utils;

import fr.opperdev.lotaryapi.Main;
import fr.opperdev.lotaryapi.utils.sql.DBManager;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomOfflinePlayer {

	private OfflinePlayer player;

	private long lotas;
	private long drachmes;

	private static Main main = Main.instance;

	private static DBManager mysql = main.mysql;

	/**
	 * Constructor #1
	 *
	 * @param player
	 * @param lotas
	 * @param drachmes
	 */
	public CustomOfflinePlayer(OfflinePlayer player, long lotas, long drachmes) {
		this.player = player;
		this.lotas = lotas;
		this.drachmes = drachmes;
		if (!mysql.exists("uuid", "'" + player.getUniqueId().toString() + "'", "lotacoin")) {
			mysql.insert("name,uuid,lotas,drachmes", "'" + player.getName() + "','" + player.getUniqueId().toString() + "','" + lotas + "','" + drachmes + "'", "lotacoin");
		}
	}

	/**
	 * @return isOnline
	 */

	public boolean isOnline() {
		return player.isOnline();
	}

	/**
	 * @return long
	 */
	public long getLotas() {
		return this.lotas;
	}

	/**
	 * @return long
	 */
	public long getDrachmes() {
		return this.drachmes;
	}

	/**
	 * @param lotas
	 */
	public void setLotas(long lotas) {
		this.lotas = lotas;
		mysql.update("lotas='" + lotas + "'", "uuid", "=", "'" + player.getUniqueId().toString() + "'", "lotacoin");
	}

	/**
	 * @param drachmes
	 */
	public void setDrachmes(long drachmes) {
		this.drachmes = drachmes;
		mysql.update("drachmes='" + drachmes + "'", "uuid", "=", "'" + player.getUniqueId().toString() + "'", "lotacoin");
	}

	/**
	 * @param player
	 * @return CustomPlayer
	 * @throws Exception
	 */
	public static boolean exists(OfflinePlayer player) {
		return mysql.exists("uuid", "'" + player.getUniqueId().toString() + "'", "lotacoin");
	}

	public static CustomOfflinePlayer get(OfflinePlayer player) {
		if (mysql.exists("uuid", "'" + player.getUniqueId().toString() + "'", "lotacoin")) {
            ArrayList<HashMap<String, String>> result = mysql.selectAll("uuid", "=", "'" + player.getUniqueId().toString() + "'", "lotacoin");
            final long[] lotas = {0};
            final long[] drachmes = {0};
            result.forEach(m -> {
                lotas[0] = Long.parseLong(m.get("lotas"));
                drachmes[0] = Long.parseLong(m.get("drachmes"));
            });
            return new CustomOfflinePlayer(player, lotas[0], drachmes[0]);
	    } else{
		    return new CustomOfflinePlayer(player, 0L, 0L);
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
