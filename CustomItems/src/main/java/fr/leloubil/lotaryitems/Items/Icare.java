package fr.leloubil.lotaryitems.Items;

import fr.leloubil.lotaryitems.Glow;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class Icare implements BaseItem {

    public static final ArrayList<UUID> IcareUsers = new ArrayList<>();

    @Getter
    private ItemStack item;

    @Getter
    private String name = ChatColor.AQUA + "Icare";

    @Getter
    private String CItemID = "§eUne énorme puissance se dégage de cet objet";

    @Getter
    private int coolDown = 15;
    @Getter
    private String cooldownMsg = " Hey ! Patiente encore %s% secondes...";

    @Getter
    private boolean dropable = true;

    @Getter
    private boolean chestable = true;

    @Getter
    private boolean persistDeath = true;

    @Override
    public ItemStack give(Player p) {
        if (p != null) p.getInventory().addItem(item);
        return item;
    }

    @Override
    public BaseItem InitItem() {
        ItemStack Icare = new ItemStack(Material.FEATHER);
        ItemMeta IcareMeta = Icare.getItemMeta();
        IcareMeta.setDisplayName(this.name);
        IcareMeta.setLore(Collections.singletonList(this.CItemID));
        Icare.setItemMeta(IcareMeta);
        Icare.addUnsafeEnchantment(new Glow(2), 1);
        this.item = Icare;
        return this;
    }

    @Override
    public void OnClick(Player p, ItemStack i) {
        Vector eye = p.getLocation().getDirection();
        eye = eye.normalize().multiply(p.getFallDistance() != 0 ? (double) 6 : (double) 3).setY(1);
        if (!IcareUsers.contains(p.getUniqueId())) IcareUsers.add(p.getUniqueId());
        p.setVelocity(eye);
        if (i.getAmount() == 1) p.getInventory().remove(i);
        else i.setAmount(i.getAmount() - 1);

    }

    @Nullable
    @Override
    public Recipe getRecipie() {
        ShapedRecipe r = new ShapedRecipe(item);
        r.shape(" G ", "GFG", " G ");
        r.setIngredient('G', Material.GOLD_INGOT);
        r.setIngredient('F', Material.FEATHER);
        return r;
    }
}
