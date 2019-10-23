package fr.leloubil.shopapi;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Shop {
    @Getter
    private final Entity PNJ;
    @Getter @Setter
    private String name;

    private Inventory CachedInv;

    public Shop(Entity pnj) throws Exception {
        if(!pnj.getType().equals(EntityType.VILLAGER)) throw new Exception("The Entity is not a villager !");
        this.PNJ = pnj;
        CachedInv = Bukkit.createInventory(null,36);
    }

    public static class ShopPage {
        private int page;
        private String ShopName;
        @Getter
        private ArrayList<ShopItem> items = new ArrayList<>(36);

        public ShopPage(ShopItem... itemss) throws Exception {
            if(itemss.length > 34) {
                throw new Exception("Trop d'items !");
            }
            try {
                for (ShopItem item : itemss) {
                    if(item.getPos() == 27 || item.getPos() == 36) throw new ArrayIndexOutOfBoundsException();
                    if(item.getPos() == -1) items.add(item);
                    else items.add(item.getPos(), item);
                }
            }
            catch (ArrayIndexOutOfBoundsException e){
                throw new Exception("Erreures de valeures !");
            }
        }
    }
    @Getter
    private ArrayList<ShopPage> pages;

    public Inventory MakeInv(int page,boolean cont) {
        Inventory temp = Bukkit.createInventory(null,36,this.getName());
        ArrayList<ShopItem> current = pages.get(page).getItems();
        for (int i = 0; i < 36; i++) {
            ShopItem item = null;
            if(current.get(i) == null) item  = current.get(i);
            else continue;
            temp.setItem(item.getPos(),item.getItem());
        }
        if(page != 0) {
            ItemStack back = new ItemStack(Material.PAPER);
            ItemMeta backmeta = back.getItemMeta();
            backmeta.setDisplayName("Aller a la page " + (page - 1));
            back.setItemMeta(backmeta);
            temp.setItem(28, back);
        }
        if(cont){
            ItemStack next = new ItemStack(Material.PAPER);
            ItemMeta nextmeta = next.getItemMeta();
            nextmeta.setDisplayName("Aller a la page " + (page + 1));
            next.setItemMeta(nextmeta);
            temp.setItem(36, next);
        }
        return temp;
    }

    public void open(Player player){
        player.openInventory(this.CachedInv);
    }
}

