package org.pythonchik.dfanchovments;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import org.joml.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class dfchants implements CommandExecutor, TabCompleter {
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
            if (sender.isOp()) {
                if (args.length > 0) {
                    if (args[0].equals("cooldown")) {
                        if (args.length > 1) {
                            if (Bukkit.getPlayer(args[1]) != null) {
                                Bukkit.getPlayer(args[1]).getPersistentDataContainer().set(new NamespacedKey(plugin, "holycd"), PersistentDataType.INTEGER, 0);
                                message.send(sender, "Способность откачена.");
                            } else {
                                message.send(sender, "Игрок не найден.");
                            }
                        } else {
                            message.send(sender, "Нужно ввести имя игрока.");
                        }
                    } else if (args[0].equals("give")) {
                        if (args.length < 3) {
                            message.send(sender, "Команда не полная");
                        } else {
                            try {
                                Player player = (Player) sender;
                                ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
                                for (CEnchantment enchs : DFanchovments.CEnchantments) {
                                    if (enchs.getName().replace(" ", "_").equals(args[1])) {
                                        book.addUnsafeEnchantment(enchs, Integer.valueOf(args[2]) <= enchs.getMaxLevel() ? Integer.valueOf(args[2]) : enchs.getStartLevel());
                                        ItemMeta tobeMeta = book.getItemMeta();
                                        int lvl = book.getEnchantmentLevel(enchs);
                                        List<String> lore = new ArrayList<String>();
                                        lore.add(message.hex(enchs.getName() + " " + (lvl == 1 ? "I" : lvl == 2 ? "II" : lvl == 3 ? "III" : lvl == 4 ? "IV" : "V")));

                                        tobeMeta.setLore(lore);
                                        book.setItemMeta(tobeMeta);
                                        break;
                                    }
                                }
                                if (player.getInventory().addItem(book).equals(new HashMap<>())){
                                    message.send(sender, "Книга добавлена");
                                } else{
                                    message.send(sender, "Для книги нету места");
                                }

                            } catch (Exception ignored) {
                                message.send(sender, "erore...");
                            }
                        }
                    } else {
                        plugin.reload5();
                        message.send(sender, "Плагин был успешно.");
                    }
                } else {
                    plugin.reload5();
                    message.send(sender, "Плагин был успешно.");
                }
            } else {
                message.send(sender, "ха-ха, еще одно никому не нужноо сообщение (-_o) -> https://www.youtube.com/watch?v=dQw4w9WgXcQ <-");
            }
        } else {
            if (args.length > 0) {
                if (args[0].equals("cooldown")) {
                    if (args.length > 1) {
                        if (Bukkit.getPlayer(args[1]) != null) {
                            Bukkit.getPlayer(args[1]).getPersistentDataContainer().set(new NamespacedKey(plugin, "holycd"), PersistentDataType.INTEGER, 0);
                            plugin.getLogger().info("Способность откачена.");
                        } else {
                            plugin.getLogger().info("Игрок не найден.");
                        }
                    } else {
                        plugin.getLogger().info("Нужно ввести ник игрока.");
                    }
                } else {
                    plugin.reload5();
                    plugin.getLogger().info("Плагин был или не был.");
                }
            } else {
                plugin.reload5();
                plugin.getLogger().info("Плагин был или не был.");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("dfchants")) {
            if (args.length == 1) {
                List<String> completions = new ArrayList<>();
                completions.add("cooldown");
                completions.add("reload");
                completions.add("give");
                return completions;
            } else if (args.length == 2) {
                if (args[0].equals("cooldown")) {
                    List<String> completions = new ArrayList<>();
                    for (Player name : Bukkit.getOnlinePlayers()) {
                        completions.add(name.getName());
                    }
                    return completions;
                } else if (args[0].equals("give")) {
                    List<String> completions = new ArrayList<>();
                    for (String name : config.getKeys(false)) {
                        completions.add(config.getString(name + ".name").replace(" ", "_"));
                    }
                    return completions;
                }
            } else if (args.length == 3) {
                if (args[0].equals("give")) {
                    List<String> completions = new ArrayList<>();
                    for (CEnchantment ench : DFanchovments.CEnchantments){
                        if (ench.getName().replace(" ", "_").equals(args[1])){
                            for (int i = 1; i<= ench.getMaxLevel();i++){
                                completions.add(String.valueOf(i));
                            }
                        }
                    }

                    return completions;

                }
            } else{
                return null;
            }
        }
        return null;
    }
}
