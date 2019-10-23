package fr.leloubil.lotawarp;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class WarpManager {

    @Getter
    private static WarpManager instance = new WarpManager();

    private File file;

    @Getter
    private HashMap<Integer,Location> warpPositions = new HashMap<>();
    private WarpManager() {
    }

    @Getter
    private HashMap<String,Warp> warps = new HashMap<>();

    private FileConfiguration warpsfile = new YamlConfiguration();

    public void parseConfig(){
        createConfigIfNotExist();
        for(String warpname : warpsfile.getKeys(false)){
            ConfigurationSection thiscfg = warpsfile.getConfigurationSection(warpname);
            ItemStack icon = thiscfg.getItemStack("icon");
            Location location = StrToLocation(thiscfg.getString("location"));
            Integer index = thiscfg.getInt("index");

            Warp w = new Warp(index,location,icon);
            warps.put(warpname,w);
        }
        cachePositions();
        Commands.prepareinv();
    }

    public void cachePositions(){
        warps.forEach((k,v) -> warpPositions.put(v.getIndex(),v.getPosition()));
    }

    public static Location getPos(Integer i){
        return WarpManager.getInstance().getWarpPositions().get(i);
    }

    public void saveChanges(){
        warps.forEach((name, warp) -> {
            if(warp.isModified()){
                ConfigurationSection thiscfg = warpsfile.getConfigurationSection(name);
                if(thiscfg == null)  {
                    warpsfile.createSection(name);
                    thiscfg = warpsfile.getConfigurationSection(name);
                }
                thiscfg.set("icon", warp.getIcon());
                thiscfg.set("location", LocToString(warp.getPosition()));
                thiscfg.set("index", warp.getIndex());
                warp.setModified(false);
            }
        });
        cachePositions();
        Commands.prepareinv();
        try {
            warpsfile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(String name){
        warpPositions.remove(warps.get(name).getIndex());
        warpsfile.set(name,null);
        warps.remove(name);
        saveChanges();
    }

    public static Location StrToLocation(String str){
        if(!(str.startsWith("[") && str.endsWith("]"))) return null;
        try {
            str = str.substring(1,str.length() - 1);
            String[] splitted = str.split(";");
            if (splitted.length < 4 || splitted.length > 6) return null;
            String worldname = splitted[0];
            double x = Double.parseDouble(splitted[1]);
            double y = Double.parseDouble(splitted[2]);
            double z = Double.parseDouble(splitted[3]);
            float yaw = 0f;
            float pitch = 0f;
            if (splitted.length > 4) yaw = Float.parseFloat(splitted[4]);
            if (splitted.length == 6) pitch = Float.parseFloat(splitted[5]);
            return new Location(Bukkit.getWorld(worldname), x, y, z, yaw, pitch);
        }
        catch (NumberFormatException e){
            return null;
        }
    }

    public static String LocToString(Location loc){
        String temp = "[";
        temp+= loc.getWorld().getName() + ";";
        temp+= loc.getX() + ";";
        temp+= loc.getY() + ";";
        temp+= loc.getZ() + ";";
        temp+= loc.getYaw() + ";";
        temp+= loc.getPitch() + "]";
        return temp;
    }

    private void createConfigIfNotExist(){
        try {
            if (!LotaWarp.getInstance().getDataFolder().exists()) {
                LotaWarp.getInstance().getDataFolder().mkdirs();
            }
            file = new File(LotaWarp.getInstance().getDataFolder(), "warps.yml");
            if (!file.exists()) {
                LotaWarp.getLoger().info("warps.yml not found, creating!");
                if(!file.exists()){
                    file.createNewFile();
                }
            } else {
                LotaWarp.getLoger().info("warps.yml found, loading!");
            }
            warpsfile.load(file);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
