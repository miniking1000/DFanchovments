package org.pythonchik.dfanchovments;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class dfchants implements CommandExecutor, TabCompleter {
    Message message = DFanchovments.getMessage();
    DFanchovments plugin;
    public dfchants(DFanchovments plugin){
        this.plugin=plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            plugin.reload5();
            plugin.getLogger().info("Плагин был или не был.");
            return true;
        }
        if (!player.isOp()) {
            message.send(sender, "ха-ха, еще одно никому не нужнооееееее сообщение (-_o) -> https://www.youtube.com/watch?v=dQw4w9WgXcQ <-");
            return true;
        }
        if (args.length == 0) {
            plugin.reload5();
            message.send(sender, "Плагин не был неудач.");
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {
            handleGive(player, args);
            return true;
        }

        plugin.reload5();
        message.send(sender, "Плагин был почти успеш.");

        return true;
    }
    public void handleGive(Player player, String[] args) {
        if (args.length < 2) {
            message.send(player, "Команда не полная");
            return;
        }

        // === /command give all ===
        if (args[1].equalsIgnoreCase("all")) {
            giveAllEnchantments(player);
            return;
        }

        // === /command give <name> <level> ===
        if (args.length < 3) {
            message.send(player, "Команда не полная");
            return;
        }

        String enchName = args[1];
        int level;

        try {
            level = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            message.send(player, "Уровень должен быть числом");
            return;
        }

        CEnchantment enchantment = findEnchantment(enchName);
        if (enchantment == null) {
            message.send(player, "Зачарование не найдено");
            return;
        }

        ItemStack book = createBook(enchantment, level);

        if (player.getInventory().addItem(book).isEmpty()) {
            message.send(player, "Книга добавлена");
        } else {
            message.send(player, "Для книги нету места");
        }
    }

    private void giveAllEnchantments(Player player) {
        int given = 0;

        for (CEnchantment ench : DFanchovments.CEnchantments) {
            ItemStack book = createBook(ench, ench.getMaxLevel());

            if (player.getInventory().addItem(book).isEmpty()) {
                given++;
            } else {
                break;
            }
        }

        message.send(player, "Выдано книг: " + given);
    }

    private ItemStack createBook(CEnchantment ench, int level) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();

        if (meta == null) return book;

        int finalLevel = Math.max(
                ench.getStartLevel(),
                Math.min(level, ench.getMaxLevel())
        );

        meta.getPersistentDataContainer().set(ench.getId(), PersistentDataType.INTEGER, finalLevel);

        // special case
        if (ench.equals(DFanchovments.democracy)) {
            meta.addAttributeModifier(
                    Attribute.SCALE,
                    new AttributeModifier(
                            Attribute.SCALE.getKey(),
                            0.5,
                            AttributeModifier.Operation.ADD_NUMBER,
                            EquipmentSlotGroup.HEAD
                    )
            );
        }

        meta.setLore(List.of(message.hex(ench.getName() + " " + Util.toRoman(finalLevel))));

        meta.setEnchantmentGlintOverride(true);
        book.setItemMeta(meta);

        return book;
    }

    private CEnchantment findEnchantment(String input) {
        String normalized = input.replace(" ", "_");

        for (CEnchantment ench : DFanchovments.CEnchantments) {
            if (ench.getName().replace(" ", "_").equalsIgnoreCase(normalized)) {
                return ench;
            }
        }
        return null;
    }




    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase("dfchants")) {
            return List.of();
        }

        // ===== /dfchants <sub> =====
        if (args.length == 1) {
            return filterPrefix(
                    args[0],
                    List.of("reload", "give")
            );
        }

        // ===== /dfchants give <ench|all> =====
        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            List<String> completions = new ArrayList<>();
            completions.add("all");

            for (CEnchantment ench : DFanchovments.CEnchantments) {
                completions.add(ench.getName().replace(" ", "_"));
            }

            return filterPrefix(args[1], completions);
        }

        // ===== /dfchants give <ench> <level> =====
        if (args.length == 3 && args[0].equalsIgnoreCase("give")) {

            // /dfchants give all <nothing>
            if (args[1].equalsIgnoreCase("all")) {
                return List.of();
            }

            CEnchantment ench = findEnchantment(args[1]);
            if (ench == null) {
                return List.of();
            }

            List<String> levels = new ArrayList<>();
            for (int i = ench.getStartLevel(); i <= ench.getMaxLevel(); i++) {
                levels.add(String.valueOf(i));
            }

            return filterPrefix(args[2], levels);
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
