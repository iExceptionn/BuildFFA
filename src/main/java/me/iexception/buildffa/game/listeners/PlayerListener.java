package me.iexception.buildffa.game.listeners;

import me.iexception.buildffa.BuildFFA;
import me.iexception.buildffa.game.User;
import me.iexception.buildffa.game.managers.UserManager;
import me.iexception.buildffa.utils.ChatUtils;
import me.iexception.buildffa.utils.FileManager;
import me.iexception.buildffa.utils.MessageUtils;
import me.iexception.buildffa.utils.ScoreboardUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        Player player = event.getPlayer();
        UserManager.getInstance().loadUser(player.getUniqueId());
        User user = UserManager.getInstance().getUser(player.getUniqueId());
        UserManager.getInstance().teleportToSpawn(user);
        ScoreboardUtils.setScoreboard(player, user);

        if(FileManager.get("config.yml").getBoolean("settings.join-messages")){
            event.setJoinMessage(ChatUtils.format(FileManager.get("config.yml").getString("settings.join-message").replaceAll("%player%", player.getName())));
        } else {
            event.setJoinMessage(null);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){

        Player player = event.getPlayer();
        User user = UserManager.getInstance().getUser(player.getUniqueId());
        UserManager.getInstance().saveUser(user);

        if(FileManager.get("config.yml").getBoolean("settings.quit-messages")){
            event.setQuitMessage(ChatUtils.format(FileManager.get("config.yml").getString("settings.quit-message").replaceAll("%player%", player.getName())));
        } else {
            event.setQuitMessage(null);
        }

    }
}
