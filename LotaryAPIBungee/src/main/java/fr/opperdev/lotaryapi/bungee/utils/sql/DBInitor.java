package fr.opperdev.lotaryapi.bungee.utils.sql;

import java.sql.Connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBInitor {

	private DBCred cred;
	private HikariDataSource hds;

	private int maxPoolSize = 10;

	public DBInitor(DBCred credentials) {
		cred = credentials;
	}

	public void init() {
		HikariConfig config = new HikariConfig();
		config.setMaximumPoolSize(maxPoolSize);
		config.setJdbcUrl(cred.toURI());
		config.setUsername(cred.getUser());
		config.setPassword(cred.getPass());
		config.setMaxLifetime(600000L);
		config.setIdleTimeout(300000L);
		config.setLeakDetectionThreshold(300000L);
		config.setConnectionTimeout(10000L);
		hds = new HikariDataSource(config);
	}

	public void setMaxPoolSize(int size) {
		maxPoolSize = size;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void close() {
		hds.close();
	}

	public Connection getConnection() {
		if(hds == null)init();
		try {
			return hds.getConnection();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}
	
}
