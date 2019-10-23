package fr.opperdev.lotaryapi.utils.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;

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
        config.setLeakDetectionThreshold(60 * 1000);
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
	    if(hds == null || hds.isClosed()){
            System.out.println("Closed or Null");
	    	init();
		}
		try {
			return hds.getConnection();
		} catch (Exception exception) {
            System.out.println("exception bruh");
			exception.printStackTrace();
		}
		return null;
	}
	
}
