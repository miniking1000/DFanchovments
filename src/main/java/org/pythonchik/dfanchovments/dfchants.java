package org.pythonchik.dfanchovments;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class dfchants implements CommandExecutor {
    Message message = DFanchovments.getMessage();
    FileConfiguration config;
    DFanchovments plugin;
    public dfchants(FileConfiguration config,DFanchovments plugin){
        this.config=config;
        this.plugin=plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            if (sender.isOp()){
                plugin.reload5();
                message.send(sender,"Плагин был успешно.");
            }
        } else{
            plugin.reload5();
            plugin.getLogger().info("Плагин был или не был.");
        }
        return true;
    }
}
