package fr.leloubil.lotarykits.parsing;

import fr.leloubil.lotarykits.InventoryMenu;
import org.bukkit.entity.Player;
import org.inventivetalent.pluginannotations.command.Command;

public class Commands {
    @Command(usage = "<Kit>",min = 0,max = 1,aliases = {"kits","k"})
    public void kit(Player p,String kitname){
        if(kitname == null || kitname.equals("")){
            p.openInventory(InventoryMenu.getInventory(p));
            return;
        }
        if(KitsManager.getInstance().getKits().get(kitname) == null){
            p.sendMessage("Ce kit n'existe pas !");
            return;
        }
        KitsManager.getInstance().getKits().get(kitname).give(p);
    }


}
