package fr.leloubil.lotarykits;

import fr.leloubil.lotarykits.parsing.Commands;
import fr.leloubil.lotarykits.parsing.KitsManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.pluginannotations.PluginAnnotations;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    @Getter
    public static HashMap<UUID,HashMap<String,Integer>> kitsused = new HashMap<>();

    @Getter
    public static HashMap<UUID,PickUpInfo> droppedItems = new HashMap<>();

    @Getter
    public static Main instance;

    @Getter
    public static Logger loger;



    @Override
    public void onEnable(){
        loger = Logger.getLogger("[LotaryKits]");
        this.getServer().getPluginManager().registerEvents(new Listeners(),this);
        instance = this;
        KitsManager.getInstance().loadKits();
        PluginAnnotations.loadAll(this, new Commands());
        loger.info("Kits initialisés !");
    }


    @Override
    public void onDisable(){
        loger.info("Kits désactivés !");
    }

}
