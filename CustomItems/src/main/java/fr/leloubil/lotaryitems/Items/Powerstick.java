package fr.leloubil.lotaryitems.Items;

import fr.leloubil.lotaryitems.Glow;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nullable;
import java.util.Collections;

public class Powerstick implements BaseItem {
    @Getter
    private ItemStack item;

    @Getter
    private String name = ChatColor.AQUA + "Powerstick";

    @Getter
    private String CItemID = ChatColor.GOLD + "POWER !" + ChatColor.RESET;

    @Getter
    private String cooldownMsg = " Wow ! Calmos ! Attend encore %s% secondes !";
    @Getter
    private int coolDown = 18000;

    @Getter
    private boolean dropable = false;

    @Getter
    private boolean chestable = false;

    @Getter
    private boolean persistDeath = false;

    @Override
    public ItemStack give(Player p) {
        if (p != null) p.getInventory().addItem(item);
        return item;
    }

    @Override
    public BaseItem InitItem() {
        ItemStack i = new ItemStack(Material.STICK);
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(this.name);
        itemMeta.setLore(Collections.singletonList(this.CItemID));
        i.setItemMeta(itemMeta);
        i.addUnsafeEnchantment(new Glow(48), 1);
        i = BaseItem.addGlow(i);
        this.item = i;
        return this;
    }

    @Override
    public void OnClick(Player p, ItemStack i) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 6000, 2));
    }

    @Nullable
    @Override
    public Recipe getRecipie() {
        return null;
    }


}
