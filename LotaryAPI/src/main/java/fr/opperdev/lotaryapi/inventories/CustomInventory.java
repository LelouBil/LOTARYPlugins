package fr.opperdev.lotaryapi.inventories;


import fr.opperdev.lotaryapi.Main;
import fr.opperdev.lotaryapi.serializer.Serializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/*
 * CustomInventory API
 * @author OpperDev
 * @version 1.4
 */
public class CustomInventory implements Listener {
	
	@EventHandler
	public void onClose(final InventoryCloseEvent e) {
		final Inventory closedInventory = e.getInventory();
		if(closedInventory.getTitle().equalsIgnoreCase(inventory.getTitle())) {
			if(!closable) {
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
					public void run() {
						e.getPlayer().openInventory(closedInventory);
					}
				}, 5);
			}
		}
	}
	
	private Inventory inventory;
	
	public static HashMap<String, CustomInventory> customInventorys = new HashMap<String, CustomInventory>();
	
	private JavaPlugin javaPlugin;
	
	private boolean closable = true;
	
	private String inventoryName = "";
	
	/*
	 * Constructor #1
	 * @param javaPlugin
	 * @param inventoryName
	 * @param save
	 * @param inventoryHolder
	 * @param inventoryTitle
	 * @param inventorySize
	 */
	public CustomInventory(JavaPlugin javaPlugin, String inventoryName, boolean save, InventoryHolder inventoryHolder, String inventoryTitle, int inventorySize) {
		this(javaPlugin, inventoryName);
		if(customInventorys.containsKey(inventoryName))
			throw new IllegalArgumentException(inventoryName+" already exists.");
		inventory = Bukkit.createInventory(inventoryHolder, inventorySize, inventoryTitle);
		if(save)customInventorys.put(inventoryName, this);
	}
	
	/*
	 * Constructor #2
	 * @param javaPlugin
	 * @param inventoryName
	 * @param save
	 * @param inventory
	 */
	public CustomInventory(JavaPlugin javaPlugin, String inventoryName, boolean save, Inventory inventory) {
		this(javaPlugin, inventoryName);
		if(customInventorys.containsKey(inventoryName))
			throw new IllegalArgumentException(inventoryName+" already exists.");
		if(save)customInventorys.put(inventoryName, this);
		this.inventory = inventory;
	}
	
	/*
	 * Constructor #3 (private)
	 * @param javaPlugin
	 */
	private CustomInventory(JavaPlugin javaPlugin, String inventoryName) {
		this.javaPlugin = javaPlugin;
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
		this.inventoryName = inventoryName;
	}
	
	/*
	 * @param closable
	 */
	public CustomInventory closable(boolean closable) {
		this.closable = closable;
		return this;
	}
	
	/*
	 * @param item
	 * @param slots
	 */
	public CustomInventory fillSlots(ItemStack item, int[] slots) {
		for(int slot : slots)
			addItem(item, slot);
		return this;
	}
	
	/*
	 * @param item
	 */
	public CustomInventory fill(ItemStack item) {
		for(int i = 0; i < inventory.getSize(); i++)
			addItem(item, i);
		return this;
	}
	
	/*
	 * @param item
	 * @param line
	 */
	public CustomInventory line(ItemStack item, int line) {
		for(int i = 0; i < 9; i++)
			inventory.setItem(line == 0 ? 0+i :
					line == 1 ? 9+i :
							line == 2 ? 18+i :
									line == 3 ? 27+i :
											line == 4 ? 36+i :
													line == 5 ? 45 + i :
														line == 6 ? 54 + i :
														0, item);
		return this;
	}
	
	/*
	 * @param item
	 * @param border
	 * @deprecated
	 */
	public CustomInventory borders(ItemStack item, int border) { return this; }
	
	/*
	 * @param item
	 * @param column
	 */
	public CustomInventory column(ItemStack item, int column) {
		int lines = inventory.getSize() / 9;
		for(int i = 0; i < lines; i++) {
			addItem(item, column + (9*i));
		}
		return this;
	}
	
	/*
	 * @param items
	 */
	public CustomInventory addItems(ItemStack... items) {
		inventory.addItem(items);
		return this;
	}
	
	/*
	 * @param items
	 */
	public CustomInventory addItems(Map<ItemStack, Integer> items) {
		for(ItemStack item : items.keySet())
			addItem(item, items.get(item));
		return this;
	}
	
	/*
	 * @param items[]
	 * @param slots[]
	 */
	public CustomInventory addItems(ItemStack items[], int slots[]) {
		if(items.length != slots.length)throw new IllegalArgumentException(items.length+" != "+slots.length);
		for(int i = 0; i < items.length; i++) {
			addItem(items[i], slots[i]);
		}
		return this;
	}
	
	/*
	 * @param item
	 * @param slot
	 */
	public CustomInventory addItem(ItemStack item, int slot) {
		inventory.setItem(slot, item);
		return this;
	}
	
	/*
	 * @deprecated
	 */
	public CustomInventory update() {
		ItemStack[] contents = inventory.getContents();
		inventory.clear();
		inventory.setContents(contents);
		return this;
	}
	
	/*
	 * @return Inventory
	 */
	public Inventory build() {
		return inventory;
	}
	
	/*
	 * @param player
	 */
	public void open(Player player) {
		player.openInventory(inventory);
	}
	
	/*
	 * @return Object
	 */
	public Object getObject() {
		return this;
	}
	
	/*
	 * @return String
	 */
	@Deprecated
	public String serialize() {
		Serializer<CustomInventory> serializer = new Serializer<CustomInventory>();
		return serializer.serialize(this);
	}
	
	/*
	 * @param file
	 */
	public void serialize(File file) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(this);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * @param inventoryName
	 * @return CustomInventory
	 */
	public static CustomInventory getCustomInventory(String inventoryName) {
		if(exists(inventoryName))
			return customInventorys.get(inventoryName);
		throw new IllegalArgumentException(inventoryName+" doesn't exists");
	}
	
	/*
	 * @param s
	 * @return CustomInventory
	 */
	@Deprecated
	public static CustomInventory deserialize(String s) {
		Serializer<CustomInventory> serializer = new Serializer<CustomInventory>();
		return serializer.deserialize(s, CustomInventory.class);
	}
	
	/*
	 * @param file
	 * @return CustomInventory
	 */
	public static CustomInventory deserialize(File file) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
			Object customInventory = in.readObject();
			return (CustomInventory) customInventory;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * @param inventoryName
	 */
	public static void removeCustomInventory(String inventoryName) {
		if(exists(inventoryName))
			customInventorys.remove(inventoryName);
		else
			throw new IllegalArgumentException(inventoryName+" doesn't exists");
	}
	
	/*
	 * @param inventoryName
	 * @return boolean
	 */
	public static boolean exists(String inventoryName) {
		return customInventorys.containsKey(inventoryName);
	}
	
	
	/*
	 * @return JavaPlugin
	 */
	public JavaPlugin getJavaPlugin() {
		return javaPlugin;
	}
	
	/*
	 * @return String
	 */
	public String getInventoryName() {
		return inventoryName;
	}
	
}
