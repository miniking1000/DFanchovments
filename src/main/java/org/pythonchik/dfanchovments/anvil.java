package org.pythonchik.dfanchovments;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class anvil implements Listener {
    Message message = DFanchovments.getMessage();
    private static final Pattern COLOR_PATTERN = Pattern.compile("(?i)(#[0-9a-f]{6}|&[0-9a-fk-or]|§[0-9a-fk-or])");

    @EventHandler
    public void AnviListener(PrepareAnvilEvent event) {
        AnvilInventory anvil = event.getInventory();
        ItemStack left = anvil.getItem(0);
        if (left == null || left.getItemMeta() == null) return;

        ItemStack right = anvil.getItem(1);
        if (!hasCustomEnchant(left.getItemMeta()) && (right == null || right.getItemMeta() == null || !hasCustomEnchant(right.getItemMeta()))) {
            return;
        }

        ItemStack result = left.clone();
        ItemMeta resultMeta = result.getItemMeta();
        if (resultMeta == null) return;
        boolean changed = false;

        if (right != null && right.getItemMeta() != null) {
            ItemMeta rightMeta = right.getItemMeta();

            for (Map.Entry<Enchantment, Integer> entry : rightMeta.getEnchants().entrySet()) {
                Enchantment incoming = entry.getKey();
                int incomingLevel = entry.getValue();
                if (hasVanillaConflict(resultMeta, incoming)) continue;
                int finalLevel = Math.max(resultMeta.getEnchantLevel(incoming), incomingLevel);
                resultMeta.addEnchant(incoming, finalLevel, false);
                changed = true;
            }

            for (CEnchantment ench : DFanchovments.CEnchantments) {
                Integer rightLevel = rightMeta.getPersistentDataContainer().get(ench.getId(), PersistentDataType.INTEGER);
                if (rightLevel == null) continue;
                if (!isApplicableTo(result.getType(), ench)) continue;
                if (hasCustomConflict(resultMeta, ench)) continue;

                int leftLevel = resultMeta.getPersistentDataContainer().getOrDefault(ench.getId(), PersistentDataType.INTEGER, 0);
                int level = leftLevel != rightLevel
                        ? Math.max(leftLevel, rightLevel)
                        : Math.min(leftLevel + 1, ench.getMaxLevel());
                level = Math.max(ench.getStartLevel(), Math.min(level, ench.getMaxLevel()));

                resultMeta.getPersistentDataContainer().set(ench.getId(), PersistentDataType.INTEGER, level);
                ench.applyAttributeEnchantments(resultMeta, level);
                resultMeta.setEnchantmentGlintOverride(true);
                changed = true;
            }
        }

        applyRename(event, resultMeta);
        updateCustomLore(resultMeta);

        if (changed) { //  || hasRename(event)
            result.setItemMeta(resultMeta);
            //event.getView().setRepairItemCountCost(right != null ? 1 : 0);
            event.getView().setRepairCost(30);
            event.setResult(result);
        }
    }

    private void updateCustomLore(ItemMeta meta) {
        List<String> customLore = new ArrayList<>();
        DFanchovments.CEnchantments.stream()
                .filter(ench -> meta.getPersistentDataContainer().has(ench.getId(), PersistentDataType.INTEGER))
                .sorted(Comparator.comparing(ench -> ench.getName() == null ? ench.getId().getKey() : ench.getName()))
                .forEach(ench -> {
                    int level = meta.getPersistentDataContainer().getOrDefault(ench.getId(), PersistentDataType.INTEGER, ench.getStartLevel());
                    customLore.add(message.hex(ench.getName() + " " + Util.toRoman(level)));
                });

        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        lore.removeIf(this::isCustomEnchantLoreLine);
        lore.addAll(customLore);

        if (lore.isEmpty()) {
            meta.setLore(null);
        } else {
            meta.setLore(lore);
        }
    }

    private boolean isCustomEnchantLoreLine(String loreLine) {
        String strippedLore = ChatColor.stripColor(loreLine);
        if (strippedLore == null) return false;

        for (CEnchantment ench : DFanchovments.CEnchantments) {
            String enchantName = ench.getName() == null ? ench.getId().getKey() : ench.getName();
            String strippedName = ChatColor.stripColor(message.hex(enchantName));
            if (strippedName != null && strippedLore.startsWith(strippedName + " ")) {
                return true;
            }
        }
        return false;
    }

    private void applyRename(PrepareAnvilEvent event, ItemMeta meta) {
        String rename = event.getView().getRenameText();
        if (rename == null || rename.isBlank()) return;
        if (COLOR_PATTERN.matcher(rename).find()) {
            meta.setDisplayName(message.hex(rename));
        } else {
            meta.setDisplayName(rename);
        }
    }

    private boolean hasRename(PrepareAnvilEvent event) {
        String rename = event.getView().getRenameText();
        return rename != null && !rename.isBlank();
    }

    private boolean isApplicableTo(Material material, CEnchantment ench) {
        for (String goodItem : ench.getTragers()) {
            Material accepted = Material.matchMaterial(goodItem);
            if (accepted == material) {
                return true;
            }
        }
        return false;
    }

    private boolean hasVanillaConflict(ItemMeta meta, Enchantment incoming) {
        for (Enchantment existing : meta.getEnchants().keySet()) {
            if (existing.conflictsWith(incoming) || incoming.conflictsWith(existing)) {
                return true;
            }
        }
        for (CEnchantment custom : DFanchovments.CEnchantments) {
            if (!meta.getPersistentDataContainer().has(custom.getId(), PersistentDataType.INTEGER)) continue;
            if (custom.conflictsWith(incoming.getKey().getKey()) || custom.conflictsWith(incoming.getKey())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasCustomConflict(ItemMeta meta, CEnchantment incoming) {
        for (CEnchantment existing : DFanchovments.CEnchantments) {
            if (!meta.getPersistentDataContainer().has(existing.getId(), PersistentDataType.INTEGER)) continue;
            if (existing.getId().equals(incoming.getId())) continue;
            if (existing.conflictsWith(incoming) || incoming.conflictsWith(existing)) {
                return true;
            }
        }
        for (Enchantment existing : meta.getEnchants().keySet()) {
            NamespacedKey existingKey = existing.getKey();
            if (incoming.conflictsWith(existingKey) || incoming.conflictsWith(existingKey.getKey())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasCustomEnchant(ItemMeta meta) {
        for (CEnchantment ench : DFanchovments.CEnchantments) {
            if (meta.getPersistentDataContainer().has(ench.getId(), PersistentDataType.INTEGER)) {
                return true;
            }
        }
        return false;
    }
}
