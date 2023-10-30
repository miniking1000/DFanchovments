package org.pythonchik.dfanchovments;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {

    private final DFanchovments bad;
    public Message(DFanchovments bad) {this.bad = bad;}
    public void send(CommandSender sender,String message) {
        sender.sendMessage(recreator(message));
    }
    public String recreator(String message) {
        return hexPerfix(message);
    }
    public String hex(String message) {
        Pattern pattern = Pattern.compile("(#[a-fA-F0-9]{6})");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&" + c);
            }
            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message).replace('&', '§');
    }
    public String hexPerfix(String message){
        return hex("&7[&6Уникальные чары&7]&r " + message);
    }
}
