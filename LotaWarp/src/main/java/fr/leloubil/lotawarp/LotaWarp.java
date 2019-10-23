package fr.leloubil.lotawarp;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.pluginannotations.PluginAnnotations;

import java.util.logging.Logger;

public final class LotaWarp extends JavaPlugin {

    @Getter
    private static LotaWarp instance;

    @Getter
    private static Logger loger;
    @Override
    public void onEnable() {
        instance = this;
        loger = getLogger();
        loger.info("Plugin initialisé");
        WarpManager.getInstance().parseConfig();
        getServer().getPluginManager().registerEvents(new WarpListener(),this);
        PluginAnnotations.loadAll(this, new Commands());
    }

    @Override
    public void onDisable() {
    }
}
