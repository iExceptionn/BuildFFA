package me.iexception.buildffa.utils;

import me.iexception.buildffa.BuildFFA;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageUtils {


    private static final MessageUtils messageUtils = new MessageUtils();
    public HashMap<String, ArrayList<String>> loadedMessage = new HashMap<>();

    public void loadMessages() {

        for (String string : FileManager.get("messages.yml").getKeys(false)) {

            if (FileManager.get("messages.yml").isList(string)) {
                loadedMessage.put(string, (ArrayList<String>) FileManager.get("messages.yml").getStringList(string));
            } else {
                ArrayList<String> notList = new ArrayList<>();
                notList.add(FileManager.get("messages.yml").getString(string));
                loadedMessage.put(string, notList);
            }

        }

    }

    public void reloadMessages() {

        loadedMessage.clear();
        FileManager.configs.clear();

        FileManager.load(BuildFFA.getInstance(), "config.yml");
        FileManager.load(BuildFFA.getInstance(), "messages.yml");
        loadMessages();

    }

    public void sendMessage(Player player, String neededMessage, String... args) {
        if (loadedMessage.containsKey(neededMessage)) {
            ArrayList<String> message = new ArrayList<>(loadedMessage.get(neededMessage));
            for (int i = 0; i < args.length; ) {
                for (int j = 0; j < message.size(); j++) {
                    String msg = message.get(j);
                    if (msg.toLowerCase().contains("[" + i + "]")) {
                        msg = msg.replace("[" + i + "]", args[i]);
                        message.set(j, msg);
                    }
                }
                i++;
            }
            message.forEach((String msg) -> player.sendMessage(ChatUtils.format(msg)));
            return;
        }
        sendConsoleMessage("message-not-found", neededMessage);
        sendMessage(player, "message-not-found", neededMessage);
    }

    public void sendMessage(CommandSender commandSender, String neededMessage, String... args) {
        if (loadedMessage.containsKey(neededMessage)) {
            ArrayList<String> message = new ArrayList<>(loadedMessage.get(neededMessage));
            for (int i = 0; i < args.length; ) {
                for (int j = 0; j < message.size(); j++) {
                    String msg = message.get(j);
                    if (msg.toLowerCase().contains("[" + i + "]")) {
                        msg = msg.replace("[" + i + "]", args[i]);
                        message.set(j, msg);
                    }
                }
                i++;
            }
            message.forEach((String msg) -> commandSender.sendMessage(ChatUtils.format(msg)));
            return;
        }
        sendConsoleMessage("message-not-found", neededMessage);
        sendMessage(commandSender, "message-not-found", neededMessage);
    }

    public void sendConsoleMessage(String neededMessage, String... args) {
        if (loadedMessage.containsKey(neededMessage)) {
            ArrayList<String> message = new ArrayList<>(loadedMessage.get(neededMessage));
            for (int i = 0; i < args.length; ) {
                for (int j = 0; j < message.size(); j++) {
                    String msg = message.get(j);
                    if (msg.toLowerCase().contains("[" + i + "]")) {
                        msg = msg.replace("[" + i + "]", args[i]);
                        message.set(j, msg);
                    }
                }
                i++;
            }
            message.forEach((String msg) -> BuildFFA.getInstance().getServer().getConsoleSender().sendMessage(ChatUtils.format(msg)));
        }
    }

    public String getMessage(String neededMessage, String... args) {

        if (loadedMessage.containsKey(neededMessage)) {
            ArrayList<String> message = new ArrayList<>(loadedMessage.get(neededMessage));
            for (int i = 0; i < args.length; ) {
                for (int j = 0; j < message.size(); j++) {
                    String msg = message.get(j);
                    if (msg.toLowerCase().contains("[" + i + "]")) {
                        msg = msg.replace("[" + i + "]", args[i]);
                        message.set(j, msg);
                    }
                }
                i++;
            }
            return ChatUtils.format(message.get(0));
        }
        sendConsoleMessage("message-not-found", neededMessage);
        return getMessage("message-not-found", neededMessage);
    }

    public ArrayList<String> getStringList(String neededMessage, String... args) {
        if (loadedMessage.containsKey(neededMessage)) {
            ArrayList<String> message = new ArrayList<>(loadedMessage.get(neededMessage));
            // Loop through arguments
            for (int i = 0; i < args.length; i++) {
                for (int j = 0; j < message.size(); j++) {
                    String msg = message.get(j);
                    String placeholder = "[" + i + "]";
                    if (msg.contains(placeholder)) {
                        msg = msg.replace(placeholder, args[i]);
                        message.set(j, msg);
                    }
                }
            }
            return message;
        }
        sendConsoleMessage("message-not-found", neededMessage);
        return getStringList("message-not-found", neededMessage);
    }

    public static MessageUtils getInstance() {
        return messageUtils;
    }
}
