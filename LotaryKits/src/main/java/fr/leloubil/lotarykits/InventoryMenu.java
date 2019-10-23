package fr.leloubil.lotarykits;

import fr.leloubil.lotarykits.parsing.Kit;
import fr.leloubil.lotarykits.parsing.KitsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;

public class InventoryMenu {
    private static Inventory baseInventory = Bukkit.createInventory(null, 45,"Kits");
    static {
        ItemStack blackpane = new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)11);
        ItemMeta bmeta = blackpane.getItemMeta();
        bmeta.setDisplayName(" ");
        blackpane.setItemMeta(bmeta);
        for (int i = 0; i < 9; i++) {
            baseInventory.setItem(i,blackpane);
        }
        for (int i = 36; i < 45; i++) {
            baseInventory.setItem(i,blackpane);
        }
        for (int i = 9; i < 28; i+=9) {
            baseInventory.setItem(i,blackpane);
        }
        for (int i = 8; i < 36; i+=9) {
            baseInventory.setItem(i,blackpane);
        }
        ItemStack door = new ItemStack(Material.DARK_OAK_DOOR_ITEM);
        ItemMeta dmeta = door.getItemMeta();
        dmeta.setDisplayName(ChatColor.RED + "FERMER ✗");
        door.setItemMeta(dmeta);
        baseInventory.setItem(44,door);
        ItemStack info = new ItemStack(Material.REDSTONE_TORCH_ON);
        ItemMeta itemMeta = info.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "➲ INFOS");
        itemMeta.setLore(Collections.singletonList(ChatColor.RED + "Pour acheter un kit fais " + ChatColor.DARK_RED + "/shop" + ChatColor.RED + " ;))"));
        info.setItemMeta(itemMeta);
        baseInventory.setItem(36,info);

    }

    public static Inventory getInventory(Player p ){
        Inventory inv = baseInventory;
        for (Kit k : KitsManager.getInstance().getKits().values()) {
            String name = k.getInvName();
            ItemStack icon = k.getIcon();
            ArrayList<String> Lore = new ArrayList<>();
            Lore.add(ChatColor.BLUE + "Cooldown " + ChatColor.DARK_AQUA + "➢ " + ChatColor.BLUE + new Time((int) k.getCooldown()).toString());
            if(!p.hasPermission(k.getPerm())) {
                name = ChatColor.RED + "✗ " +  Listeners.stripSymbols(k.getInvName());
                Lore.add( ChatColor.DARK_RED + "A acheter dans le Shop !");
            }
            ItemMeta imeta = icon.getItemMeta();
            imeta.setDisplayName(name);
            imeta.setLore(Lore);
            icon.setItemMeta(imeta);
            inv.setItem(k.getPosition(),icon);
        }
        return inv;
    }
}
