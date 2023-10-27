package org.pythonchik.dfanchovments;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Message {

    private final DFanchovments bad;
    public Message(DFanchovments bad) {this.bad = bad;}
    public void send(CommandSender sender,String message) {
        sender.sendMessage(recreator(message));
    }
    public String recreator(String message) {
        return ChatColor.translateAlternateColorCodes('&',"&7[&6АХАХАХАХХАХАХАХАХАХАХХАХАХАХАХАХАХАХАХАХАХАХХАХАХАХААХАХХАХАХАХАХАХХАХАХАХАХАХХАХАХАХАХАХХАХАХА&7]&r " + message);
    }
}
