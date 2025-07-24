package me.iexception.buildffa.utils;

import com.avaje.ebean.validation.NotNull;
import net.md_5.bungee.api.ChatColor;

public class ChatUtils {


    public static @NotNull String format(String input){

        input = input.replace("%prefix%", FileManager.get("config.yml").getString("prefix"));

        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
