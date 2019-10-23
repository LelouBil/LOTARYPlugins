package fr.leloubil.shopapi;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class ShopAPI {
    private HashMap<String, Shop> list = new HashMap<>();
    private static ShopAPI instance = new ShopAPI();

    private ShopAPI(){}

    public static ShopAPI get(){return instance;}

    public void openShop(String Name, int page, Player p){

    }
    public void registerShop(Shop shop){
        list.put(shop.getName(),shop);
    }
}
