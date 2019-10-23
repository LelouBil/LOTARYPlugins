package fr.leloubil.lotarykits.parsing;

import fr.leloubil.lotarykits.Main;
import fr.leloubil.lotarykits.PickUpInfo;
import fr.leloubil.lotarykits.Time;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class Kit {


    @Getter @Setter
    private ArrayList<ItemStack> items;

    @Getter
    private boolean defalt;

    @Getter
    private String name;

    @Getter
    private String InvName;

    private ItemStack icon;

    @Getter
    private String perm;

    public ItemStack getIcon() {
        if(this.icon == null){
            this.icon = this.items.get(0);
        }
        return icon;
    }

    @Getter

    private int position;

    @Getter @Setter
    private double cooldown;

    public Kit(String aName,int aPos,String aInvName,ItemStack aIcon){
        this.name = aName;
        this.perm = "kits." + this.name.toLowerCase();
        this.defalt = false;
        this.position = aPos;
        this.InvName = ChatColor.translateAlternateColorCodes('&',aInvName);
        this.icon = aIcon;
    }

    public boolean canGive(Player p){
        if(Main.getKitsused().get(p.getUniqueId()) == null){
            HashMap<String,Integer> temp = new HashMap<>();
            temp.put(this.name,(int)System.currentTimeMillis() + (int)(cooldown * 1000));
            Main.getKitsused().put(p.getUniqueId(),temp);
            return true;
        }
        Integer maxtime = Main.getKitsused().get(p.getUniqueId()).get(this.name);
        if(maxtime == null){
            Main.getKitsused().get(p.getUniqueId()).put(this.name,(int)System.currentTimeMillis() + (int)(cooldown * 1000));
            return true;
        }
        if((int) System.currentTimeMillis() > maxtime){
            Main.getKitsused().get(p.getUniqueId()).remove(this.name);
            return true;
        }
        else{
            return false;
        }
    }

    public void give(Player p){
        Inventory in = p.getInventory();
        if(!p.hasPermission(this.perm)){
            p.sendMessage(new TextComponent("Tu n'a pas acheté le kit \"" + this.name + "\" !"));
            return;
        }
        if(canGive(p)){
            ItemStack[] tmp = new ItemStack[this.getItems().size()];
            tmp = this.getItems().toArray(tmp);
            HashMap<Integer,ItemStack> failed = in.addItem(tmp);
            if(!failed.isEmpty()){
                failed.values().forEach((ItemStack i) -> {
                   Item item =  p.getWorld().dropItem(p.getLocation(),i);
                   Main.getDroppedItems().put(item.getUniqueId(),new PickUpInfo(p.getUniqueId()));
                });
                p.sendMessage("Dépeche toi, test items sont protégés pendant 30 secondes !");
            }

        }
        else {
            Integer remaining = (Main.getKitsused().get(p.getUniqueId()).get(this.name) - (int)System.currentTimeMillis())/1000;
            p.sendMessage(ChatColor.RED + "Tu dois encore attendre " + ChatColor.GOLD + "➢ " + new Time(remaining).toString() /*+ "." */);
        }
    }
}
