package fr.leloubil.shopapi;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ShopButton {
    @Getter @Setter
    private ItemStack item = null;
    @Getter @Setter
    private int pos;
    @Getter @Setter
    private Consumer<Player> purchased;

    public ShopButton(ItemStack item, int pos, Consumer<Player> purchased) {
        this.item = item;
        this.pos = pos;
        this.purchased = purchased;
    }

    public ShopButton() {
    }
}
