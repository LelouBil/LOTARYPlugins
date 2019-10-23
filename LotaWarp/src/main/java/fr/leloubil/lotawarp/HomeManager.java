package fr.leloubil.lotawarp;

import com.sun.media.sound.InvalidFormatException;
import lombok.Getter;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.caching.UserData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.*;

public class HomeManager {
    @Getter
    private static HomeManager instance = new HomeManager();

    private HomeManager(){};


    @Getter
    private HashMap<UUID,ArrayList<Home>> HomeList = new HashMap<>();

    public void setupHomes(){
        Database db = new Database(LotaWarp.getInstance());
        ArrayList<HashMap<String,String>> homes = new ArrayList<>();
        try {
             homes = db.getFromQuerys("SELECT * FROM `LotaWarp_Homes`");
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        homes.forEach((data) -> {
            Home temp = new Home(data.get("UUID"),data.get("Name"),data.get("Location"));
            if(!HomeList.containsKey(UUID.fromString(data.get("UUID")))){
                ArrayList<Home> tmp = (ArrayList<Home>)Collections.singletonList(temp);
                HomeList.put(UUID.fromString(data.get("UUID")),tmp);
            }
            else {
                HomeList.get(UUID.fromString(data.get("UUID"))).add(temp);
            }
        });
    }

    public String registerHome(Home h){
        RegisteredServiceProvider<LuckPermsApi> provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi.class);
        if (provider == null) {
            LotaWarp.getInstance().getServer().getPluginManager().disablePlugin(LotaWarp.getInstance());
            return "luckperms";
        }
        LuckPermsApi api = provider.getProvider();
        UserData pdata = Objects.requireNonNull(api.getUser(h.getOwner())).getCachedData();
        Map<String,String> metaData = pdata.getMetaData(Contexts.global()).getMeta();
        Integer maxhomes = Integer.valueOf(metaData.get("maxhomes"));
        if(!HomeList.containsKey(UUID.fromString(h.getOwner().toString()))){
            ArrayList<Home> tmp = (ArrayList<Home>)Collections.singletonList(h);
            tmp.removeIf((home -> !home.getName().equals(h.getName())));
            HomeList.put(h.getOwner(),tmp);
        }
        else {
            ArrayList<Home> tmp = HomeList.get(h.getOwner());
            tmp.removeIf((home -> !home.getName().equals(h.getName())));
            if(tmp.size() + 1 > maxhomes){
                return "homes";
            }
            if(!tmp.isEmpty()){
                return "name";
            }
            HomeList.get(h.getOwner()).add(h);
        }
        return "good";
    }

    public Inventory getHomesInv(Player p){
        Inventory tmp = Bukkit.createInventory(null,9,"Homes");
        UUID id = p.getUniqueId();
        ArrayList<Home> homes = HomeList.get(id);
        int i = 0;
        for (Home home : homes) {
            ItemStack item = new ItemStack(Material.BED);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(home.getName());
            item.setItemMeta(meta);
            i++;
        }
        return tmp;
    }

}
