package fr.leloubil.lotawarp;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class WarpListener implements Listener {

    @EventHandler
    public void OnInventoryClickEvent(InventoryClickEvent e){
        switch (e.getInventory().getName()){
            case "Warps":
                WarpClickEvent(e);
                return;
            case "Homes":
                HomesClickEvent(e);
        }
    }

    public void WarpClickEvent(InventoryClickEvent e){
        e.setCancelled(true);
        if(e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
            return;
        }
        Location toTP = WarpManager.getPos(e.getSlot());
        e.getWhoClicked().teleport(toTP, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public void HomesClickEvent(InventoryClickEvent e){
        e.setCancelled(true);
        if(e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)){
            return;
        }
        Location toTP = WarpManager.getPos(e.getSlot());
        e.getWhoClicked().teleport(toTP, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }
}
