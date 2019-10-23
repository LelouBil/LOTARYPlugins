package fr.leloubil.shopapi;

import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.pluginannotations.PluginAnnotations;
import org.inventivetalent.pluginannotations.command.Command;

import java.util.logging.Logger;

public class Main extends JavaPlugin {
    @Getter
    private static Main instance;

    @Getter
    private static Economy econ;

    @Getter
    private static Permission perms;

    @Getter
    private static Chat chat;

    @Getter
    private static final Logger log = Logger.getLogger("[ShopAPI]");
    @Override
    public void onEnable(){
        PluginAnnotations.COMMAND.registerCommands(this,this);
        instance = this;
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        //setupChat();
    }

    @Override
    public void onDisable(){

    }
    private boolean setupEconomy() {
        Plugin[] plugins = getServer().getPluginManager().getPlugins();
        int i = 0;
        for (Plugin plugin : plugins ){
            if(plugin.getName().equals("Vault")) i++;
        }
        if(i == 0) return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }
    @Command(name = "testapi")
    public void testapi(Player player){
        if(!player.getNearbyEntities(12,12,12).get(0).getType().equals(EntityType.VILLAGER)){
            return;
        }
        Entity pnj = (Entity) Bukkit.getWorld("world").getEntitiesByClass(Villager.class).toArray()[0];
        String name = "The best shop";
        Shop thebest = null;
        try {
             thebest = new Shop(pnj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        thebest.setName(name);
        ItemStack it = new ItemStack(Material.SLIME_BALL);
        ShopItem test = new ShopItem((Player pl) -> {pl.getInventory().addItem(new ItemStack(Material.SLIME_BALL));},1.2);
        test.setItem(it);
        test.setOnetime(true);
        Shop.ShopPage page1 = null;
        try {
             page1 = new Shop.ShopPage(test,test);
        } catch (Exception e) {
            e.printStackTrace();
        }
        thebest.getPages().add(page1);
        thebest.MakeInv(0,false);
        ShopAPI.get().registerShop(thebest);
    }
}
