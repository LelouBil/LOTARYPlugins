package fr.opperdev.lotaryapi.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

import fr.opperdev.lotaryapi.Main;

public class AutoUpdate {
	
	private String pluginName;
	private String pluginVersion;
	private URL metricsUrl; 
	
	public AutoUpdate(String pluginName, String pluginVersion, URL metricsUrl) {
		this.pluginName = pluginName;
		this.pluginVersion = pluginVersion;
		this.metricsUrl = metricsUrl;
	}
	
	public boolean hasUpdate() {
		if(!metricsUrl.getPath().endsWith(pluginVersion))return true;
		return false;
	}
	
	public void update() {
		if(hasUpdate()) {
			try {
				Files.delete(new File(getJarPath()).toPath());
				downloadFile(metricsUrl, getPluginsPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String getJarPath() {
		return (Main.instance.getDataFolder()+"/../"+pluginName+"-"+pluginVersion+".jar");
	}
	
	private String getPluginsPath() {
		return (Main.instance.getDataFolder()+"/../");
	}
	
	private void downloadFile(URL u, String lechemin) throws IOException {
	    URLConnection uc = u.openConnection( ); 
	    int fileLenght = uc.getContentLength( );
	    System.out.println(fileLenght);
	    if (fileLenght == -1 ) { 
	        throw new IOException(); 
	    }
	    BufferedInputStream bis = new BufferedInputStream(uc.getInputStream( ));
	    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(lechemin));
	    byte[] data = new byte[2048]; 
	    int nbRead = 0;
	    int deplacement = 0;
	    try {
	        while ((nbRead = bis.read(data)) > 0) { 
	            bos.write(data, 0, nbRead);
	            bos.flush();
	            deplacement += nbRead;
	        } 
	    } finally {
	        try {
	            bis.close();
	        } finally {
	            bos.close();
	        }
	    }
	    if (deplacement != fileLenght) { 
	        throw new IOException(); 
	    }
	}

}
