package fr.leloubil.lotaryitems.Items;

import fr.leloubil.lotaryitems.ItemCoolDown;
import fr.leloubil.lotaryitems.Main;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.UUID;

public interface BaseItem {

    static ItemCoolDown getCoolDown(UUID id) {
        return Arrays.stream(Main.cooldowns.toArray()).filter((i) -> id.toString().equals(((ItemCoolDown) i).getPlayer().toString())).toArray(ItemCoolDown[]::new)[0];
    }

    static boolean hasCoolDown(UUID id) {
        return Arrays.stream(Main.cooldowns.toArray()).anyMatch((i) -> id.toString().equals(((ItemCoolDown) i).getPlayer().toString()));
    }

    static ItemStack addGlow(ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null) tag = nmsStack.getTag();
        NBTTagList ench = new NBTTagList();
        tag.set("ench", ench);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }

    ItemStack give(Player p);

    BaseItem InitItem();

    void OnClick(Player p, ItemStack i);

    String getName();

    String getCooldownMsg();

    ItemStack getItem();

    String getCItemID();

    int getCoolDown();

    @Nullable
    Recipe getRecipie();

    boolean isDropable();

    boolean isChestable();

    boolean isPersistDeath();
}
