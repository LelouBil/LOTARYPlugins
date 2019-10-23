package fr.opperdev.lotaryapi.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Reflection {
	
	private static String package_base = "";
	
	/**
	 * @param package_base
	 */
	public static void setPackageBase(String package_base){
		Reflection.package_base = package_base;
	}
	
	/**
	 * @return String
	 */
	public static String getPackageBase(){
		return Reflection.package_base;
	}
	
	
	/**
	 * 
	 * @param name
	 * @return Class<?>
	 */
	public static Class<?> getClass(String name){
		Class<?> clazz = null;
		try{
			if(Reflection.package_base != "")
				return Class.forName(Reflection.package_base+"."+name);
			return Class.forName(name);
		}catch(ClassNotFoundException cnfe){
			System.out.println(cnfe.getMessage());
		}
		return clazz;
	}
	
	/**
	 * 
	 * @param clazz
	 * @param fieldName
	 * @param declared
	 * @return Field
	 */
	public static Field getField(Class<?> clazz, String fieldName, boolean declared){
		Field field = null;
		try {
			field = declared ? clazz.getDeclaredField(fieldName) : clazz.getField(fieldName);
			field.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return field;
	}
	
	/**
	 * 
	 * @param field
	 * @param obj
	 * @return Object
	 */
	public static Object getFieldValue(Field field, Object obj){
		Object object = null;
		try {
			object = field.get(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}
	
	/**
	 * 
	 * @param field
	 * @param obj
	 * @param value
	 * @param finalField
	 */
	public static void setFieldValue(Field field, Object obj, Object value, boolean finalField){
		field.setAccessible(true);
		try {
			if(!finalField) {
				field.set(obj, value);
			} else {
				Field modifiers = Field.class.getDeclaredField("modifiers");
				modifiers.setAccessible(true);
				modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
				field.set(obj, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param clazz
	 * @param methodName
	 * @param declared
	 * @param params
	 * @return Method
	 */
	public static Method getMethod(Class<?> clazz, String methodName, boolean declared, Class<?>... params){
		Method method = null;
		try{
			method = declared ? clazz.getDeclaredMethod(methodName, params) : clazz.getMethod(methodName, params);
			method.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return method;
	}
	
	/**
	 * 
	 * @param clazz
	 * @param declared
	 * @param params
	 * @return Constructor<?>
	 */
	public static Constructor<?> getConstructor(Class<?> clazz, boolean declared, Class<?>... params){
		Constructor<?> constructor = null;
		try {
			constructor = declared ? clazz.getDeclaredConstructor(params) : clazz.getConstructor(params);
			constructor.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return constructor;
	}
	
	/**
	 * 
	 * @param method
	 * @param object
	 * @param params
	 * @return Object
	 */
	public static Object invoke(Method method, Object object, Object... params){
		try {
			return method.invoke(object, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param constructor
	 * @param params
	 * @return Object
	 */
	public static Object newInstance(Constructor<?> constructor, Object... params){
		try {
			return constructor.newInstance(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param player
	 * @param packet
	 */
	public static void sendPacket(Player player, Object packet) {
		try {
			Object nmsPlayer = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
			playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param nmsClassName
	 * @return Class<?>
	 */
	public static Class<?> getNMSClass(String nmsClassName){
		try {
			return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + nmsClassName);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param player
	 * @param header
	 * @param footer
	 */
	public static void setTab(Player player, String header, String footer) {
		Class<?> chatSerializer = getNMSClass("IChatBaseComponent$ChatSerializer");
		Class<?> packetList = getNMSClass("PacketPlayOutPlayerListHeaderFooter");
		try {
			Object headerComponent = chatSerializer.getMethod("a", String.class).invoke(null, "{'text':'"+header+"'}");
			Object footerComponent = chatSerializer.getMethod("a", String.class).invoke(null, "{'text':'"+footer+"'}");
			Object packet = packetList.newInstance();
			Field headerField = packet.getClass().getDeclaredField("a");
			headerField.setAccessible(true);
			headerField.set(packet, headerComponent);
			headerField.setAccessible(!headerField.isAccessible());
			Field footerField = packet.getClass().getDeclaredField("b");
			footerField.setAccessible(true);
			footerField.set(packet, footerComponent);
			footerField.setAccessible(!footerField.isAccessible());
			
			sendPacket(player, packet);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

