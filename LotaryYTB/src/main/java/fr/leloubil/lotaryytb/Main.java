package fr.leloubil.lotaryytb;


import com.google.common.io.ByteStreams;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;

public class Main extends Plugin {

    @Getter
    private static String ytaddmsg;

    @Getter
    private static String ytmsg;

    @Getter
    private static String noyt;

    @Getter
    private static File configFile;

    @Getter
    private static Main instance;

    @Getter
    public static Database db;
    @Override
    public void onEnable(){
        db = new Database(this);
        instance = this;
        try {
            setupconfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getLogger().info("LotaryYTB chargé");
        getProxy().getPluginManager().registerCommand(this,new YoutubeAdd());
        getProxy().getPluginManager().registerCommand(this,new Youtube());
        getProxy().getPluginManager().registerCommand(this,new YoutubeReaload());
    }

    public void setupconfig() throws IOException {
        if(!getDataFolder().exists()) getDataFolder().mkdirs();
        Main.configFile = new File(getDataFolder(),"config.yml");
        if(!configFile.exists()){
            configFile.createNewFile();
            try (InputStream is = getResourceAsStream("config.yml");
                 OutputStream os = new FileOutputStream(configFile)) {
                ByteStreams.copy(is, os);
            }
        }
        Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(Main.configFile);
        Main.ytaddmsg = ChatColor.translateAlternateColorCodes('&',cfg.getString("ytaddmsg"));
        Main.ytmsg = ChatColor.translateAlternateColorCodes('&',cfg.getString("ytmsg"));
        Main.noyt = ChatColor.translateAlternateColorCodes('&',cfg.getString("ytnone"));
    }

}
