package fr.leloubil.lotaryitems;

import com.google.common.collect.Collections2;
import fr.leloubil.lotaryitems.Items.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.pluginannotations.PluginAnnotations;
import org.inventivetalent.pluginannotations.command.Command;
import org.inventivetalent.pluginannotations.command.Completion;
import org.inventivetalent.pluginannotations.command.Permission;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin {

    public static final ArrayList<ItemCoolDown> cooldowns = new ArrayList<>();
    public static final HashMap<String, BaseItem> items = new HashMap<>();
    private static String packs;

    private static String packids;

    private static int packsnb;
    private static FileConfiguration invscfg;
    private static File invsf;
    @Getter
    private static Main instance;

    @Getter
    @Setter
    private static boolean bypassCooldown;

    private static FileConfiguration getSpecialConfig() {
        return invscfg;
    }

    @Override
    public void onEnable() {
        Main.instance = this;
        createFiles();
        PluginAnnotations.CONFIG.loadValues(this, this);
        PluginAnnotations.COMMAND.registerCommands(this, this);
        getServer().getPluginManager().registerEvents(new ItemsListener(), this);
        Main.packs = getSpecialConfig().getString("packs");
        Main.packids = getSpecialConfig().getString("packids");
        Main.packsnb = getSpecialConfig().getInt("packsnb");
        if (packsnb > 0) {
            String[] invs = packs.split("!!!!");
            String[] ids = packids.split("!!!!");
            for (int i = 0; i <= (packsnb - 1); i++) {
                PackID id = new PackID(ids[i]);
                BackPack.getPacks().put(id, InventoryStringDeSerializer.StringToInventory(invs[i], "BackPack n° " + id.toString()));
            }
        }

        registerGlow();
        registerItems();
        getLogger().info("ITEMS REGISTERED");
        items.values().forEach((BaseItem b) -> {
            if (b.getRecipie() != null) Bukkit.addRecipe(b.getRecipie());
        });

        new CoolDownRunnable().runTaskTimer(this, 0, 20);

    }

    @Override
    public void onDisable() {
        final String[] invs = {""};
        final String[] ids = {""};
        BackPack.getPacks().forEach((k, v) -> {
            ids[0] += k.toString() + "!!!!";
            invs[0] += InventoryStringDeSerializer.InventoryToString(v) + "!!!!";
        });
        if (invs[0].endsWith("!!!!")) invs[0] = invs[0].substring(0, invs[0].length() - 4);
        if (ids[0].endsWith("!!!!")) ids[0] = ids[0].substring(0, ids[0].length() - 4);

        getSpecialConfig().set("packs", invs[0]);
        getSpecialConfig().set("packids", ids[0]);
        getSpecialConfig().set("packsnb", BackPack.getPacks().size());
        try {
            getSpecialConfig().save(invsf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerGlow() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Glow glow = new Glow(70);
            Enchantment.registerEnchantment(glow);
        } catch (IllegalArgumentException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Permission(value = "citem.command")
    @Command(usage = "<item>", min = 1)
    public void citem(Player player, String item) {

        if (!items.containsKey(item)) return;

        items.get(item).give(player);


    }

    @Completion
    public void citem(List<String> completions, Player p, String item) {
        if (item == null) completions.addAll(items.keySet());
        else completions.addAll(Collections2.filter(items.keySet(), s -> s != null && s.startsWith(item)));
    }

    @Permission(value = "citem.debug")
    @Command
    public void itemdebug(Player p) {
        p.sendMessage(items.toString() + " ---------- " + items.size());
    }

    @Permission(value = "citem.bypass")
    @Command
    public void cbypass(Player p) {
        Main.setBypassCooldown(!Main.isBypassCooldown());
    }

    private void registerItems() {
        items.put(ChatColor.stripColor(new Icare().getName()), new Icare().InitItem());
        items.put(ChatColor.stripColor(new BackPack().getName()), new BackPack().InitItem());
        items.put(ChatColor.stripColor(new Powerstick().getName()), new Powerstick().InitItem());
        items.put(ChatColor.stripColor(new Recycleur().getName()), new Recycleur().InitItem());
        getLogger().info("ITEMS REGISTERED");
    }

    private void createFiles() {

        invsf = new File(getDataFolder(), "invs.yml");
        if (!invsf.exists()) {
            invsf.getParentFile().mkdirs();
            saveResource("invs.yml", false);
        }

        invscfg = new YamlConfiguration();
        try {
            invscfg.load(invsf);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}

