package org.pythonchik.dfanchovments;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class bosscommand implements CommandExecutor, TabCompleter {
    Message message = DFanchovments.getMessage();
    DFanchovments plugin;

    public bosscommand(DFanchovments plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            plugin.getLogger().info("Плагин не был.");
            return true;
        }
        if (!player.isOp()) {
            message.send(sender, "ха-ха, еще одно никому не нужнооееееее сообщение (-_o) -> https://www.youtube.com/watch?v=dQw4w9WgXcQ <-");
            return true;
        }
        if (args.length < 2) {
            message.send(sender, "usage: /makeaboss rarity selector");
            return true;
        }
        Bosses.Rarity rarity = Bosses.Rarity.valueOf(args[0].toUpperCase());
        if (rarity == Bosses.Rarity.UNKNOWN) {
            message.send(sender, "rarity not found");
            return true;
        }
        List<Entity> entities = Bukkit.selectEntities(sender, args[1]);
        for (Entity alpha : entities) {
            if (alpha instanceof Enemy entity) {
                DFanchovments.bosses.makeIntoBoss(entity, rarity);
            }
        }
        message.send(sender, "Плагин был почти успеш.");
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase("makeaboss")) {
            return List.of();
        }

        if (args.length == 1) {
            return filterPrefix(
                    args[0],
                    List.of("common", "uncommon", "rare", "epic", "legendary", "mythic")
            );
        }

        if (args.length == 2) {
            List<String> completions = new ArrayList<>();
            completions.add("@e[limit=1,type=!player,sort=nearest]");
            return filterPrefix(args[1], completions);
        }

        return List.of();
    }

    private List<String> filterPrefix(String current, List<String> options) {
        String lower = current.toLowerCase();

        return options.stream()
                .filter(opt -> opt.toLowerCase().startsWith(lower))
                .toList();
    }


}
