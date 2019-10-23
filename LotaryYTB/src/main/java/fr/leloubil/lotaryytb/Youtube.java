package fr.leloubil.lotaryytb;

import com.sun.media.sound.InvalidFormatException;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Youtube extends Command {
    public Youtube() {
        super("youtube", "", "yt");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length != 1){
            commandSender.sendMessage(new ComponentBuilder("usage : /youtube <nom>").color(ChatColor.RED).create());
            return;
        }

        try {
            ResultSet rs = Main.getDb().getFromKey("Profil","pseudo",strings[0]);
            rs.beforeFirst();
            if(!rs.next()){
                String msg = Main.getNoyt().replaceAll("%P",strings[0] + ChatColor.RESET).replaceAll("%P%",strings[0] + ChatColor.RESET);
                commandSender.sendMessage(new ComponentBuilder(msg).create());
                return;
            }
            else{
                rs.first();
                TextComponent link = new TextComponent(rs.getString("youtube"));
                link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,rs.getString("youtube")));
                link.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Clique pour y aller !").create()));
                TextComponent msg = new TextComponent(Main.getYtmsg().replaceAll("%P",strings[0] + ChatColor.RESET).replaceAll("%P%",strings[0] + ChatColor.RESET).split("%L")[0]);
                msg.addExtra(link);
                commandSender.sendMessage(msg);
            }
        } catch (SQLException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }
}
