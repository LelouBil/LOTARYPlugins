package fr.opperdev.lotaryapi.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
	
	/**
	 * 
	 * @param file
	 */
	private static void createFile(File file) {
		try {
			if(file.isDirectory()) {
				if(!file.exists()) {
					file.mkdirs();
				}
			} else {
				if(!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				if(!file.exists()) {
					file.createNewFile();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param file
	 * @param tabchif
	 * @param tablet
	 */
	public static void saveFile(File file, int[] tabchif, String[] tablet) {
		try {
			createFile(file);
			PrintWriter out = new PrintWriter(new FileWriter(file));
			for(int i = 0; i < tabchif.length; i++) {
				out.println(tablet[i]);
			}
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param file
	 * @return String[]
	 */
	public static String[] readFile(File file) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			List<String> lines = new ArrayList<String>();
			String line;
			while((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
			bufferedReader.close();
			return lines.toArray(new String[lines.size()]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param xml
	 * @param tagName
	 * @return String
	 */
	public static String getValue(String xml, String tagName) {
		return xml.split("<"+tagName+">")[1].split("</"+tagName+">")[0];
	}
	
}
