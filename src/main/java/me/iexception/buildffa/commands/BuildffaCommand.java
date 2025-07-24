package me.iexception.buildffa.commands;

import me.iexception.buildffa.BuildFFA;
import me.iexception.buildffa.game.managers.UserManager;
import me.iexception.buildffa.utils.FileManager;
import me.iexception.buildffa.utils.MessageUtils;
import me.iexception.buildffa.utils.ScoreboardUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildffaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length == 0) {
            if (!(sender.hasPermission("buildffa.help") || sender.hasPermission("buildffa.*"))) {
                MessageUtils.getInstance().sendMessage(sender, "no-permissions");
                return true;
            }
            MessageUtils.getInstance().sendMessage(sender, "help");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help":
                if (!(sender.hasPermission("buildffa.help") || sender.hasPermission("buildffa.*"))) {
                    MessageUtils.getInstance().sendMessage(sender, "no-permissions");
                    break;
                }
                MessageUtils.getInstance().sendMessage(sender, "help");
                break;
            case "about":
                if (!(sender.hasPermission("buildffa.about") || sender.hasPermission("buildffa.*"))) {
                    MessageUtils.getInstance().sendMessage(sender, "no-permissions");
                    break;
                }
                MessageUtils.getInstance().sendMessage(sender, "about");
                break;
            case "reload":
                if (!(sender.hasPermission("buildffa.reload") || sender.hasPermission("buildffa.*"))) {
                    MessageUtils.getInstance().sendMessage(sender, "no-permissions");
                    break;
                }
                MessageUtils.getInstance().reloadMessages();
                MessageUtils.getInstance().sendMessage(sender, "reload");
                for(Player online : Bukkit.getOnlinePlayers()){
                    ScoreboardUtils.setScoreboard(online, UserManager.getInstance().getUser(online.getUniqueId()));
                }
                break;
            case "setspawn":
                if (!(sender.hasPermission("buildffa.setspawn") || sender.hasPermission("buildffa.*"))) {
                    MessageUtils.getInstance().sendMessage(sender, "no-permissions");
                    break;
                }

                if(!(sender instanceof Player)){
                    MessageUtils.getInstance().sendMessage(sender, "only-player-command");
                    break;
                }
                Player player = (Player) sender;
                Location location = player.getLocation();
                String locString = location.getWorld().getName() + ";" +
                        location.getX() + ";" +
                        location.getY() + ";" +
                        location.getZ() + ";" +
                        location.getYaw() + ";" +
                        location.getPitch();

                MessageUtils.getInstance().sendMessage(sender, "spawn-set");
                FileManager.set("config.yml", "spawn", locString);
                FileManager.save(BuildFFA.getInstance(), "config.yml");
                break;
            case "spawn":
                if (!(sender.hasPermission("buildffa.spawn") || sender.hasPermission("buildffa.*"))) {
                    MessageUtils.getInstance().sendMessage(sender, "no-permissions");
                    break;
                }

                if(args.length == 1){
                    if(!(sender instanceof Player)){
                        MessageUtils.getInstance().sendMessage(sender, "only-player-command");
                        break;
                    }

                    player = (Player) sender;
                    UserManager.getInstance().teleportToSpawn(UserManager.getInstance().getUser(player.getUniqueId()));
                    break;
                }

                Player target = Bukkit.getPlayer(args[1]);
                if(target == null){
                    MessageUtils.getInstance().sendMessage(sender, "player-not-found", args[1]);
                    break;
                }

                UserManager.getInstance().teleportToSpawn(UserManager.getInstance().getUser(target.getUniqueId()));
                break;

        }

        return false;
    }
}
