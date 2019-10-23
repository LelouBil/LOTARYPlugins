package fr.leloubil.lotaryitems.Items;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.Collections;

public class Recycleur implements BaseItem {
    @Getter
    private ItemStack item;

    @Getter
    private String name = ChatColor.AQUA + "Recycleur";

    @Getter
    private String CItemID = "§eEeeeh fais gaffe, on annule pas un contrat sans pertes !";

    @Getter
    private int coolDown = 0;

    @Getter
    private boolean dropable = false;

    @Getter
    private String cooldownMsg = "";

    @Getter
    private boolean chestable = false;

    @Getter
    private boolean persistDeath = false;

    private static boolean matchcancel(ItemStack i) {
        return i.getItemMeta().getDisplayName().equals(ChatColor.DARK_RED + "» Fermer") && i.getItemMeta().getLore().get(0).equals(ChatColor.RED + "Pour annuler");

    }

    private static boolean matchvalid(ItemStack i) {
        return i.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "» Confirmer") && i.getItemMeta().getLore().get(0).equals(ChatColor.GREEN + "Pour valider");
    }

    public static boolean isrecycl(Inventory i) {
        return ChatColor.stripColor(i.getName()).equals("Recycleur") && Recycleur.matchcancel(i.getItem(18)) && Recycleur.matchvalid(i.getItem(26));
    }

    @Override
    public ItemStack give(Player p) {
        if (p != null) p.getInventory().addItem(item);
        return item;
    }

    @Override
    public BaseItem InitItem() {
        ItemStack i = new ItemStack(Material.DISPENSER);
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(this.name);
        itemMeta.setLore(Collections.singletonList(this.CItemID));
        i.setItemMeta(itemMeta);
        this.item = i;
        return this;
    }

    @Override
    public void OnClick(Player p, ItemStack i) {
        Inventory recyclinv = Bukkit.createInventory(null, 27, "Recycleur");
        ItemStack cancel = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta cancmeta = cancel.getItemMeta();
        cancmeta.setDisplayName(ChatColor.DARK_RED + "» Fermer");
        cancmeta.setLore(Collections.singletonList(ChatColor.RED + "Pour annuler"));
        cancel.setItemMeta(cancmeta);

        ItemStack valid = new ItemStack(Material.SLIME_BALL);
        ItemMeta validmeta = cancel.getItemMeta();
        validmeta.setDisplayName(ChatColor.GREEN + "» Confirmer");
        validmeta.setLore(Collections.singletonList(ChatColor.GREEN + "Pour valider"));
        valid.setItemMeta(validmeta);
        ItemStack vitre = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
        ItemMeta vitremeta = vitre.getItemMeta();
        vitremeta.setDisplayName(" ");
        vitre.setItemMeta(vitremeta);
        for (int ii = 0; ii <= 9; ii++) {
            recyclinv.setItem(ii, vitre);
        }
        recyclinv.setItem(9, vitre);
        for (int ii = 17; ii <= 25; ii++) {
            recyclinv.setItem(ii, vitre);
        }

        recyclinv.setItem(18, cancel);
        recyclinv.setItem(26, valid);
        p.openInventory(recyclinv);

    }

    @Nullable
    @Override
    public Recipe getRecipie() {
        return null;
    }
}
