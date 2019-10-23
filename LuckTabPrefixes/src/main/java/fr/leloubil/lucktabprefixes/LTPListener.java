package fr.leloubil.lucktabprefixes;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaContexts;
import me.lucko.luckperms.api.caching.MetaData;
import me.lucko.luckperms.api.caching.UserData;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LTPListener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent e){
        Main.getLoger().info("PLAYER JOINED");
        UserData pdata = Objects.requireNonNull(Main.getLuckApi().getUser(e.getPlayer().getUniqueId())).getCachedData();
        Map<String,String> metaData = pdata.getMetaData(Contexts.global()).getMeta();
        String prefix = "";
        String suffix = "";
        if(metaData.containsKey("tab-prefix")) {
            prefix = ChatColor.translateAlternateColorCodes('&',metaData.get("tab-prefix"));
        }
        if(metaData.containsKey("tab-suffix")) {
            suffix = ChatColor.translateAlternateColorCodes('&',metaData.get("tab-suffix"));
        }
        Main.getLoger().info("PREFIX = " + prefix + "; SUFFIX = " + suffix);
        e.getPlayer().setPlayerListName(prefix + e.getPlayer().getName() + suffix);
        e.getPlayer().setDisplayName(prefix + e.getPlayer().getName() + suffix);
    }
}
