package fr.opperdev.lotaryapi.bungee.utils.sql;

public class DBCred {
	
	private String host,user,pass,database;
	private int port;
	
	public DBCred(String host, String user, String pass, String database, int port) {
		this.host = host;
		this.user = user;
		this.pass = pass;
		this.database = database;
		this.port = port;
	}
	
	public String toURI() {
		return "jdbc:mysql://"+host+":"+port+"/"+database;
	}
	
	public String getHost() {
		return host;
	}
	
	public String getUser() {
		return user;
	}
	
	public String getPass() {
		return pass;
	}
	
	public String getDatabase() {
		return database;
	}
	
	public int getPort() {
		return port;
	}
	
}
