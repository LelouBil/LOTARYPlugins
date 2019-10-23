package fr.leloubil.lotawarp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.inventivetalent.pluginannotations.command.Command;
import org.inventivetalent.pluginannotations.command.Permission;

public class Commands {
    private static Inventory warpinv;
    public static void prepareinv(){
        warpinv = Bukkit.createInventory(null,36,"Warps");
        warpinv.getViewers().forEach(HumanEntity::closeInventory);
        //TODO Faire une interface jolie

        ItemStack i = new ItemStack(Material.WOODEN_DOOR);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName("Homes");
        i.setItemMeta(meta);
        warpinv.setItem(35,i);
        for (Warp warp : WarpManager.getInstance().getWarps().values()) {
            warpinv.setItem(warp.getIndex(),warp.getIcon());
        }
    }


    @Command(name = "warps", aliases = {"w","warp"}, description = "La commande des warps",max = 0)
    public void warps(Player p){
        p.openInventory(warpinv);
    }
    @Permission(value = "warpadmin.del",permissionMessage = "Commande inconnue")
    @Command(aliases = {"wdel","warpsdel"}, min = 1, max = 2, description = "Supprimer un warp",usage = "<WarpName>")
    public void warpdel(Player p,String name,String confirm){
        if(WarpManager.getInstance().getWarps().get(name) == null){
            p.sendMessage(ChatColor.RED + "Ce Warp n'existe pas !");
            return;
        }
        if(confirm == null || !confirm.equals("confirm")){
            p.sendMessage(ChatColor.BLUE + "fais la commande " + ChatColor.GOLD + "/waprdel " + name + " confirm " + ChatColor.BLUE + " pour confirmer");
            return;
        }
        WarpManager.getInstance().delete(name);
    }

    @Permission(value = "warpadmin.add",permissionMessage = "Commande inconnue")
    @Command(aliases = {"wadd","warpscreate"}, min = 2, max = 3, description = "Ajouter un warp",usage = "<WarpName> <WarpIndex> [WarpLoc]")
    public void warpadd(Player p,String name,Integer index, String location){
        if(WarpManager.getInstance().getWarps().get(name) != null){
            p.sendMessage(ChatColor.RED + "Un Warp du même nom existe déja ");
            return;
        }
        if(WarpManager.getInstance().getWarpPositions().get(index) != null){
            p.sendMessage(ChatColor.RED + "Un Warp existe déja a cette index");
            return;
        }
        if(p.getItemInHand() ==null ||p.getItemInHand().getType().equals(Material.AIR)){
            p.sendMessage(ChatColor.RED + "Il te faut l'icone du warp dans la main !");
            return;
        }
        Location pos;
        if(location == null || location.equals("")){
            p.sendMessage(ChatColor.GREEN + "Utilisation de ta position et de ton orientation");
            pos = p.getLocation();
        }
        else {
            try {
                pos = WarpManager.StrToLocation(location);
            }
            catch (NumberFormatException e){
                p.sendMessage(ChatColor.RED + "Il faut écrire la position sous la forme : " + ChatColor.GOLD + "[world;x;y;z;yaw;pitch]" + ChatColor.RED + "(yaw et pitch sont optionels)");
                return;
            }
            if(pos == null){
                p.sendMessage(ChatColor.RED + "Il faut écrire la position sous la forme : " + ChatColor.GOLD + "[world;x;y;z;yaw;pitch]" + ChatColor.RED + "(yaw et pitch sont optionels)");
                return;
            }
        }
        Warp w = new Warp(index,pos,p.getItemInHand());
        w.setModified(true);
        WarpManager.getInstance().getWarps().put(name,w);
        WarpManager.getInstance().saveChanges();
        WarpManager.getInstance().parseConfig();
    }
    @Permission(value = "warpadmin.edit",permissionMessage = "Commande inconnue")
    @Command(aliases = {"wedit","warpsmodify"}, min = 2, max = 2, description = "Modifier un warp",usage = "<WarpName> <ToModify>")
    public void warpedit(Player p,String name,String toModify){
        if(WarpManager.getInstance().getWarps().get(name) == null){
            p.sendMessage(ChatColor.RED + "Ce warp n'existe pas");
            return;
        }
        Integer index;
        try {
            index = Integer.parseInt(toModify);
            Integer lastindex =  WarpManager.getInstance().getWarps().get(name).getIndex();
            WarpManager.getInstance().getWarps().get(name).setIndex(index);
            WarpManager.getInstance().saveChanges();
            p.sendMessage(ChatColor.GREEN + "L'index du warp : " + ChatColor.BLUE + name + ChatColor.GREEN +" est passé de " + ChatColor.GOLD + lastindex + ChatColor.GREEN + " à " + ChatColor.GOLD + index);
            return;
        }
        catch (NumberFormatException ignored){}
        Location l = WarpManager.StrToLocation(toModify);
        if(l != null){
            String lastpos = WarpManager.LocToString(WarpManager.getInstance().getWarps().get(name).getPosition());
            WarpManager.getInstance().getWarps().get(name).setPosition(l);
            WarpManager.getInstance().saveChanges();
            p.sendMessage(ChatColor.GREEN + "La position du warp : " + ChatColor.BLUE + name + ChatColor.GREEN +" est passé de " + ChatColor.GOLD + lastpos + ChatColor.GREEN + " à " + ChatColor.GOLD + toModify);
            return;
        }
        if(toModify.equals("icon")){
            if(p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR)){
                p.sendMessage(ChatColor.RED + "Il faut que tu tienne le nouvel icone dans ta main !");
                return;
            }
            WarpManager.getInstance().getWarps().get(name).setIcon(p.getItemInHand());
            WarpManager.getInstance().saveChanges();
            return;
        }
        if(toModify.equals("position")){
            Location last = WarpManager.getInstance().getWarps().get(name).getPosition();
            WarpManager.getInstance().getWarps().get(name).setPosition(p.getLocation());
            WarpManager.getInstance().saveChanges();
            p.sendMessage(ChatColor.GREEN + "La position du warp : " + ChatColor.BLUE + name + ChatColor.GREEN +" est passé de " + ChatColor.GOLD + WarpManager.LocToString(last) + ChatColor.GREEN + " à " + ChatColor.GOLD + WarpManager.LocToString(p.getLocation()));
            return;
        }
        Warp save = WarpManager.getInstance().getWarps().get(name);
        save.setModified(true);
        WarpManager.getInstance().delete(name);
        WarpManager.getInstance().getWarps().put(toModify,save);
        WarpManager.getInstance().saveChanges();
        p.sendMessage(ChatColor.GREEN + "Le warp : " + ChatColor.BLUE + name + ChatColor.GREEN +" à bien été renommé en " + ChatColor.GOLD + toModify);
    }
    @Permission(value = "warpadmin.save",permissionMessage = "Commande inconnue")
    @Command(aliases = {"wsave","warpssave"}, max = 0, description = "Sauvegarder les warp",usage = "")
    public void warpsave(CommandSender s){
        WarpManager.getInstance().saveChanges();
    }


    @Command(aliases = {"home","h"},max = 0,usage = "")
    public void homes(Player p){
        p.openInventory(HomeManager.getInstance().getHomesInv(p));
    }

    @Command(aliases = {"homesadd","hadd"},max = 1,min = 1,usage = "<NomDuHome>")
    public void homeadd(Player p,String HomeName){
        Home h = new Home(p.getUniqueId(),p.getLocation(),HomeName);
        switch (HomeManager.getInstance().registerHome(h)){
            case "name":
                p.sendMessage("&5[&2LotaHomes&5]&cUne Home du même nom existe déja.");
                return;
            case "good":
                p.sendMessage("&5[&2LotaHomes&5]&2La Home + &5" + h.getName() + "&2 a bien été créée.");
                return;
            case "homes":
                p.sendMessage("&5[&2LotaHomes&5]&cVous avez atteint votre nombre maximum de homes");
                return;
        }
    }
}
