package fr.leloubil.lotaryitems;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ItemCoolDown {

    @Getter
    private UUID player;

    @Getter
    @Setter
    private int time;
    @Getter
    @Setter
    private ItemStack i;

    // @Getter @Setter
    //private BaseItem item;

    public ItemCoolDown(int time, UUID p, ItemStack i) {
        this.time = time;
        this.player = p;
        this.i = i;
    }
}
