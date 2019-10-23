package fr.leloubil.lotarykits.parsing;

import fr.leloubil.lotarykits.Main;
import fr.sipixer.main.lotarybox;
import lombok.Getter;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.material.SpawnEgg;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class KitsManager {
    private FileConfiguration kitsfile = new YamlConfiguration();

    private static KitsManager ourInstance = new KitsManager();

    @Getter
    private Kit defaultKit;

    public static KitsManager getInstance() {
        return ourInstance;
    }

    private KitsManager() {
    }
    public final void loadKits(){
        Main.getLoger().info("Loading kits");
        createConfigIfNotExist();
        if(!kitsfile.contains("Kits")){
            Main.getLoger().info("Mauvais fichier !");
            Main.getInstance().getPluginLoader().disablePlugin(Main.getInstance());
            return;
        }
        ConfigurationSection MainCfg = kitsfile.getConfigurationSection("Kits");
        for(String kitname : MainCfg.getKeys(false)){
            ConfigurationSection thiscfg = MainCfg.getConfigurationSection(kitname);
            ItemStack icon = null;
            if(thiscfg.contains("icon")){
                icon = StringToItemStack(thiscfg.getString("icon"));
            }
            Kit tempkit = new Kit(thiscfg.getName(),thiscfg.getInt("index"), StringEscapeUtils.unescapeJava(thiscfg.getString("dispname",thiscfg.getName())),icon);
            ArrayList<String> itemsS = (ArrayList<String>) thiscfg.getStringList("Items");
            ArrayList<ItemStack> itemStacks = new ArrayList<>();
            itemsS.forEach((String i) -> {
                itemStacks.add(StringToItemStack(i));
            });
            tempkit.setItems(itemStacks);
            tempkit.setCooldown(thiscfg.getDouble("cooldown"));
            if(thiscfg.getBoolean("default",false)){
                this.defaultKit = tempkit;
            }
            else {
                this.kits.put(thiscfg.getName(), tempkit);
            }
        }
    }

    @Getter
    private HashMap<String,Kit> kits = new HashMap<>();

    private void createConfigIfNotExist(){
        try {
            if (!Main.getInstance().getDataFolder().exists()) {
                Main.getInstance().getDataFolder().mkdirs();
            }
            File file = new File(Main.getInstance().getDataFolder(), "kits.yml");
            if (!file.exists()) {
                //Main.getLoger().info("kits.yml not found, creating!");
                if(!file.exists()){
                    file.createNewFile();
                }
            } else {
               // Main.getLoger().info("kits.yml found, loading!");
            }
            kitsfile.load(file);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private ItemStack StringToItemStack(String i){
        //Main.getLoger().info("PROCESSING  : " + i);
        ItemStack temp;
        String[] splitted = i.split(";",-1);
        String material = splitted[0].split("~")[0];
        byte data = 0;
        if(splitted[0].startsWith("KEY=")){
            return lotarybox.getKey(splitted[0].split("=")[1]);
        }
        if(splitted[0].split("~").length == 2){
            data = Byte.parseByte(splitted[0].split("~")[1]);
        }
        Integer amount = Integer.parseInt(splitted[1]);
        if(splitted[0].startsWith("SP=")){
            //Main.getLoger().info("LIST : " + fr.leloubil.lotaryitems.Main.items.toString());
            temp = fr.leloubil.lotaryitems.Main.items.get(StringEscapeUtils.unescapeJava(material).split("=")[1].replace(" " ,"")).give(null);
            temp.setAmount(amount);
        }
        else if(splitted[0].equals("Potion") || splitted[0].equals("Splash_Potion")){
            Potion p = new Potion(1);
            if(splitted[0].equals("Splash_Potion")){
                p.setSplash(true);
            }
            temp = p.toItemStack(amount);
            PotionMeta potionMeta = (PotionMeta) temp.getItemMeta();
            String[] effects = splitted[3].split("~");
            for (String effect : effects){
                String name = effect.split("=")[0];
                Integer lv = Integer.valueOf(effect.split("=")[1]);
                Integer time= Integer.valueOf(effect.split("=")[2]);
                potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.getByName(name),time * 20,lv),true);
            }
            temp.setItemMeta(potionMeta);
        }
        else if(splitted[0].equals("SPAWN_EGG")){
            SpawnEgg egg = new SpawnEgg(EntityType.valueOf(splitted[3]));
            temp = egg.toItemStack();
            temp.setAmount(amount);
        }
        else {
            temp = new ItemStack(Material.getMaterial(material), amount,(byte) data);
        }
        String[] miscs = splitted[2].split("~");
        ItemMeta tempmeta = temp.getItemMeta();
        for (String miscfact : miscs){
            String[] facts = miscfact.split("=");
            switch (facts[0]){
                case "ench":
                    String enchname = facts[1];
                    Integer enchlv = Integer.parseInt(facts[2]);
                    tempmeta.addEnchant(Enchantment.getByName(enchname),enchlv,true);
                    break;
                case "name":
                    String name = facts[1];
                    tempmeta.setDisplayName(name);
                    break;
                case "lore":
                    String[] lore = facts[1].split("//");
                    List<String> lorelist = Arrays.asList(lore);
                    tempmeta.setLore(lorelist);
                    break;
            }
        }
        temp.setItemMeta(tempmeta);
        return temp;
    }
}
