package fr.leloubil.lotaryitems.Items;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BackPack implements BaseItem {

    @Getter
    private static HashMap<PackID, Inventory> Packs = new HashMap<>();

    @Getter
    private ItemStack item;

    @Getter
    private String name = "§6BackPack™";

    @Getter
    private String CItemID = "§4Une §5REVOLUTION §4en matière de stockage portable !";

    @Getter
    private String cooldownMsg = "";
    @Getter
    private int coolDown = 0;

    @Getter
    private boolean dropable = true;

    @Getter
    private boolean chestable = true;

    @Getter
    private boolean persistDeath = true;

    @Override
    public ItemStack give(Player p) {
        ItemStack i = item.clone();
        ItemMeta itemMeta = i.getItemMeta();
        List<String> lore = itemMeta.getLore();
        lore.add("Numéro de série : " + new PackID().toString());
        itemMeta.setLore(lore);
        i.setItemMeta(itemMeta);
        PackID id = new PackID(itemMeta.getLore().get(1).substring(18, 24));
        Packs.put(id, Bukkit.createInventory(null, 18, "BackPack " + id.toString()));
        if (p != null) p.getInventory().addItem(i);
        return i;
    }


    @Override
    public BaseItem InitItem() {
        ItemStack Icare = new ItemStack(Material.CHEST);
        ItemMeta IcareMeta = Icare.getItemMeta();
        IcareMeta.setDisplayName(this.getName());
        IcareMeta.setLore(Collections.singletonList(this.CItemID));
        Icare.setItemMeta(IcareMeta);
        this.item = BaseItem.addGlow(Icare);
        return this;
    }

    @Override
    public void OnClick(Player p, ItemStack i) {
        ItemMeta itemMeta = i.getItemMeta();
        PackID id = new PackID(itemMeta.getLore().get(1).substring(18, 24));
        p.openInventory(Packs.get(id));
    }


    @Nullable
    @Override
    public Recipe getRecipie() {
        ShapedRecipe r = new ShapedRecipe(item);
        r.shape(" G ", "GFG", " G ");
        r.setIngredient('G', Material.LEATHER);
        r.setIngredient('F', Material.CHEST);
        return r;
    }
}
