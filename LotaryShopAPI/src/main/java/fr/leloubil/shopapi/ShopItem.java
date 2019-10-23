package fr.leloubil.shopapi;


import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sun.util.resources.ParallelListResourceBundle;

import java.lang.reflect.Method;
import java.util.function.Consumer;

public class ShopItem  extends ShopButton{
    @Getter @Setter
    private ItemStack item = null;

    @Getter @Setter
    private boolean Buyable = true;
    @Getter @Setter
    private int pos;
    @Getter @Setter
    private Consumer<Player> purchased;

    public ShopItem(Consumer<Player> tocall, double price) {
        this.purchased = tocall;
        this.price = price;
        this.pos = -1;
    }
    public ShopItem(Consumer<Player> tocall, double price, int pos) {
        this.purchased = tocall;
        this.price = price;
        this.pos = pos;
    }

    @Getter @Setter
    private boolean onetime = false;
    @Getter @Setter
    private double price;

    public boolean HasEnough(Player p){
        return Main.getEcon().getBalance(p) >= price;
    }
}
