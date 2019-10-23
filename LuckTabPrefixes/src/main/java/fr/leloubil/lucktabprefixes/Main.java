package fr.leloubil.lucktabprefixes;

import lombok.Getter;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    @Getter
    private static Logger loger = Logger.getLogger("[LuckTabPrefixes]");

    @Getter
    private static LuckPermsApi LuckApi;

    @Override
    public void onEnable() {
        instance = this;
        try {
            LuckApi = LuckPerms.getApi();
        }
        catch (IllegalStateException e){
            e.printStackTrace();
        }
        getServer().getPluginManager().registerEvents(new LTPListener(),this);
        Main.getLoger().info("Plugin Initialisé !");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
