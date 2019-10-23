package fr.opperdev.lotaryapi.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Serializer<T> {
	
	private static Gson gson;
	
	static {
		gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().setPrettyPrinting().create();
	}
	
	public String serialize(T clazz) {
		return gson.toJson(clazz);
	}
	
	public T deserialize(String json, Class<T> clazz) {
		return gson.fromJson(json, clazz);
	}
	
}
