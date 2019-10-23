package fr.opperdev.lotaryapi.inventories;

import java.util.Arrays;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.opperdev.lotaryapi.serializer.Serializer;

/*
 * CustomItem API
 * @author OpperDev
 * @version 4.2
 */
public class CustomItem {
	
	private ItemStack itemStack;
	
	private ItemMeta itemMeta;
	
	/*
	 * Constructor #1
	 * @param material
	 * @param amount
	 * @param data
	 */
	public CustomItem(Material material, int amount, short data) {
		itemStack = new ItemStack(material, amount, data);
		itemMeta = itemStack.getItemMeta();
	}
	
	/*
	 * Constructor #2
	 * @param material
	 * @param amount
	 */
	public CustomItem(Material material, int amount) {
		this(material, amount, (byte)0);
	}
	
	/*
	 * Constructor #3
	 * @param material
	 */
	public CustomItem(Material material) {
		this(material, 1);
	}
	
	/*
	 * @param lores
	 */
	public CustomItem addLores(String... lores) {
		itemMeta.setLore(Arrays.asList(lores));
		return this;
	}
	
	/*
	 * @param enchants
	 */
	public CustomItem addEnchants(Map<Enchantment, Integer> enchants) {
		for(Enchantment ench : enchants.keySet())
			addEnchant(ench, enchants.get(ench));
		return this;
	}
	
	/*
	 * @param enchs[]
	 * @param levels[]
	 */
	public CustomItem addEnchants(Enchantment enchs[], int levels[]) {
		if(enchs.length != levels.length)throw new IllegalArgumentException(enchs.length+" != "+levels.length);
		for(int i = 0; i < enchs.length; i++) {
			addEnchant(enchs[i], levels[i]);
		}
		return this;
	}
	
	/*
	 * @param ench
	 * @param level
	 */
	public CustomItem addEnchant(Enchantment ench, int level) {
		itemMeta.addEnchant(ench, level, true);
		return this;
	}
	
	/*
	 * @param name
	 */
	public CustomItem setName(String name) {
		itemMeta.setDisplayName(name);
		return this;
	}
	
	/*
	 * @param unbreakable
	 */
	public CustomItem setUnbreakable(boolean unbreakable) {
		itemMeta.spigot().setUnbreakable(unbreakable);
		return this;
	}
	
	/*
	 * @return ItemStack
	 */
	public ItemStack build() {
		itemStack.setItemMeta(itemMeta);
		return itemStack;
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
	public String serialize() {
		Serializer<CustomItem> serializer = new Serializer<CustomItem>();
		return serializer.serialize(this);
	}
	
	/*
	 * @param s
	 * @return CustomItem
	 */
	public static CustomItem deserialize(String s) {
		Serializer<CustomItem> serializer = new Serializer<CustomItem>();
		return serializer.deserialize(s, CustomItem.class);
	}
	
}
