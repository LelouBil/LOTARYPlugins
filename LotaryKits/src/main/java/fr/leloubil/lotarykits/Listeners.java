package fr.leloubil.lotarykits;

import fr.leloubil.lotarykits.parsing.Kit;
import fr.leloubil.lotarykits.parsing.KitsManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;

public class Listeners implements Listener {

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e){
        Item i = e.getItem();
        if(Main.getDroppedItems().get(i.getUniqueId()) != null){
            PickUpInfo info = Main.getDroppedItems().get(i.getUniqueId());
            boolean finished = (int)info.getMaxtime() < (int)System.currentTimeMillis();
            if(finished){
                Main.getDroppedItems().remove(i.getUniqueId());
                return;
            }
            if(e.getPlayer().getUniqueId().equals(info.getPlayerID())){
                Main.getDroppedItems().remove(i.getUniqueId());
                return;
            }
            e.setCancelled(true);

        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!e.getInventory().getTitle().equals("Kits")) return;
        if (!e.getInventory().getItem(0).getItemMeta().hasDisplayName()) return;
        if (!e.getInventory().getItem(0).getItemMeta().getDisplayName().equals(" ")) return;
        Inventory inv = e.getInventory();
        Player p = (Player) e.getWhoClicked();
        e.setCancelled(true);
        String kitname = e.getCurrentItem().getItemMeta().getDisplayName();
        if (e.getSlot() == 44) {
            p.closeInventory();
        } else if (e.getSlot() == 36) e.setCancelled(true);
        else if (!kitname.equals(" ")) {
            kitname = stripSymbols(kitname);
            Kit k = KitsManager.getInstance().getKits().get(kitname);
            if (k == null) {
                p.sendMessage(ChatColor.RED + "Une erreure innatendue est survenue");
                return;
            }
            if (p.hasPermission(k.getPerm())) {
                k.give(p);
            }
            else {
                p.sendMessage(ChatColor.RED + "Tu n'a pas acheté ce kit !");
            }
            p.closeInventory();
        }
    }
    public static String stripSymbols(String toStrip){
        toStrip = ChatColor.stripColor(toStrip.replace("➥ ", ""));
        toStrip = toStrip.replace(" ✴", "");
        toStrip = toStrip.replace(" ❂", "");
        toStrip = toStrip.replace(" ✪", "");
        toStrip = toStrip.replace("✗ ", "");
        return toStrip;
    }
}
