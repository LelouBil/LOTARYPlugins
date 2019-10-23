package fr.leloubil.lotaryytb;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.IOException;

public class YoutubeReaload extends Command {
    public YoutubeReaload() {
        super("youtubereload", "lotaryytb.reload", "ytreload");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        try {
            Main.getInstance().setupconfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
