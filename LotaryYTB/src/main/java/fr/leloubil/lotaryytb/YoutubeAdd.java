package fr.leloubil.lotaryytb;

import com.sun.media.sound.InvalidFormatException;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Pattern;

public class YoutubeAdd extends Command {

    public YoutubeAdd(){
        super("youtubeadd","lotaryytb.add","ytadd");
    }
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length != 2){
            commandSender.sendMessage(new ComponentBuilder("usage : /youtubeadd <nom> <lien>").color(ChatColor.RED).create());
            return;
        }
        if(Main.getInstance().getProxy().getPlayer(strings[0]) == null){
            commandSender.sendMessage(new ComponentBuilder("Le joueur n'est pas connecté !").color(ChatColor.RED).create());
            return;
        }
        ProxiedPlayer p = Main.getInstance().getProxy().getPlayer(strings[0]);
        Pattern pattern = Pattern.compile("(http(s?)://)?(www\\.)?youtube\\.(fr|com|be)/(user|channel)/.+");
        if(!pattern.matcher(strings[1]).matches()){
            commandSender.sendMessage(new ComponentBuilder("Format de lien invalide !").color(ChatColor.RED).create());
            return;
        }
        try {
            ResultSet rs = Main.getDb().getFromKey("Profil","uuid",p.getUniqueId().toString());
            if(!rs.next()){
                Main.getDb().createNewStatementIfClosed();
                Connection c = Main.getDb().getConnection();

                PreparedStatement st = c.prepareStatement("INSERT INTO `Profil` (`pseudo`,`uuid`,`coins`,`youtube`) VALUES (?,?,?,?);");
                st.setString(1,p.getName());
                st.setString(2,p.getUniqueId().toString());
                st.setInt(3,0);

                st.setString(4,!strings[1].startsWith("http://") && !strings[1].startsWith("https://") ? "https://" + strings[1] : strings[1]);
                st.execute();
            }
            else{
                Main.getDb().createNewStatementIfClosed();
                Connection c = Main.getDb().getConnection();
                PreparedStatement ps = c.prepareStatement("UPDATE `Profil` SET `youtube` = ?, `pseudo` = ? WHERE `uuid` = ?");
                ps.setString(1,!strings[1].startsWith("http://") && !strings[1].startsWith("https://") ? "https://" + strings[1] : strings[1]);
                ps.setString(2,p.getName());
                ps.setString(3,p.getUniqueId().toString());
                ps.executeUpdate();
            }
        } catch (InvalidFormatException | SQLException e) {
            e.printStackTrace();
        }
        String msg = Main.getYtaddmsg().replaceAll("%P",p.getDisplayName() + ChatColor.RESET).replaceAll("%P%",p.getName() + ChatColor.RESET).replaceAll("%L",strings[1] + ChatColor.RESET);

        commandSender.sendMessage(new ComponentBuilder(msg).create());
    }
}
