package org.pythonchik.dfanchovments;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.regex.Pattern;

public class anvil implements Listener {
    Message message = DFanchovments.getMessage();
    private static final Pattern COLOR_PATTERN = Pattern.compile("(?i)(#[0-9a-f]{6}|&[0-9a-fk-or]|§[0-9a-fk-or])");
    private final NamespacedKey CUSTOM_ANVIL_RESULT =
            new NamespacedKey(DFanchovments.plugin, "custom_anvil_result");

    @EventHandler(priority = EventPriority.LOWEST)
    public void AnviListener(PrepareAnvilEvent event) {
        AnvilInventory anvil = event.getInventory();
        ItemStack left = anvil.getItem(0);
        if (left == null || left.getItemMeta() == null) return;

        ItemStack right = anvil.getItem(1);
        if (!hasCustomEnchant(left.getItemMeta()) && (right == null || right.getItemMeta() == null || !hasCustomEnchant(right.getItemMeta()))) {
            return;
        }

        boolean leftIsBook = left.getType() == Material.ENCHANTED_BOOK;
        boolean rightIsBook = right != null && right.getType() == Material.ENCHANTED_BOOK;

        boolean leftHasCustom = hasCustomEnchant(left.getItemMeta());
        boolean rightHasCustom = right != null && right.getItemMeta() != null && hasCustomEnchant(right.getItemMeta());

        if (leftIsBook && !rightIsBook && (leftHasCustom || rightHasCustom)) {
            //event.setResult(new ItemStack(Material.AIR));
            //event.getView().setRepairCost(0);
            return;
        }

        ItemStack result = left.clone();
        ItemMeta resultMeta = result.getItemMeta();
        assert resultMeta != null;
        Map<Enchantment, Integer> enchantments = new HashMap<>(getAllEnchants(resultMeta));

        boolean changed = false;

        if (right != null && right.getItemMeta() != null) {
            ItemMeta rightMeta = right.getItemMeta();

            for (Map.Entry<Enchantment, Integer> entry : getAllEnchants(rightMeta).entrySet()) {
                Enchantment incoming = entry.getKey();
                int incomingLevel = entry.getValue();
                int currentLevel = enchantments.getOrDefault(incoming, 0);
                int finalLevel = incomingLevel == currentLevel ? currentLevel + 1 : Math.max(currentLevel, incomingLevel);
                finalLevel = Math.clamp(finalLevel, incoming.getStartLevel(), incoming.getMaxLevel());

                boolean canEnchant = isVanillaApplicableTo(incoming, result);
                for (Map.Entry<Enchantment, Integer> originalEnchantment: enchantments.entrySet()) {
                    if (originalEnchantment.getKey() != incoming && (incoming.conflictsWith(originalEnchantment.getKey()) || originalEnchantment.getKey().conflictsWith(incoming))) {
                        canEnchant = false;
                        break;
                    }
                }

                if (canEnchant) {
                    enchantments.put(incoming, finalLevel);
                    changed = true;
                }
            }
            if (changed) {
                applyVanillaEnchants(resultMeta, enchantments);
            }


            for (CEnchantment ench : DFanchovments.CEnchantments) {
                Integer rightLevel = rightMeta.getPersistentDataContainer().get(ench.getId(), PersistentDataType.INTEGER);
                if (rightLevel == null) continue;
                if (!isApplicableTo(result.getType(), ench)) continue;
                if (hasCustomConflict(resultMeta, ench)) continue;

                int leftLevel = resultMeta.getPersistentDataContainer().getOrDefault(ench.getId(), PersistentDataType.INTEGER, 0);
                int level = leftLevel == rightLevel
                        ? leftLevel + 1
                        : Math.max(leftLevel, rightLevel);
                level = Math.clamp(level, ench.getStartLevel(), ench.getMaxLevel());

                resultMeta.getPersistentDataContainer().set(ench.getId(), PersistentDataType.INTEGER, level);
                ench.applyAttributeEnchantments(resultMeta, level);
                resultMeta.setEnchantmentGlintOverride(true);
                changed = true;
            }
        }

        applyRename(event, resultMeta);
        Util.updateCustomLore(resultMeta);

        if (changed) {
            resultMeta.getPersistentDataContainer().set(
                    CUSTOM_ANVIL_RESULT,
                    PersistentDataType.BYTE,
                    (byte) 1
            );

            result.setItemMeta(resultMeta);
            event.getView().setRepairCost(30);
            event.setResult(result);
        }
    }

    @EventHandler
    public void onAnvilTake(InventoryClickEvent event) {
        if (!(event.getInventory() instanceof AnvilInventory anvil)) return;
        if (event.getRawSlot() != 2) return; // result slot
        if (!(event.getWhoClicked() instanceof Player player)) return;
        ItemStack result = event.getCurrentItem();
        if (result == null || !result.hasItemMeta()) return;

        ItemMeta meta = result.getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().has(CUSTOM_ANVIL_RESULT, PersistentDataType.BYTE)) {
            return;
        }

        ItemStack right = anvil.getItem(1);
        if (right == null || right.getType().isAir()) return;

        if (player.getGameMode().equals(GameMode.CREATIVE) || XP.getTotalExperience(player) >= XP.getTotalXpForLevel(30)) {
            // if one of those is not true - craft fails

            meta.getPersistentDataContainer().remove(CUSTOM_ANVIL_RESULT);
            result.setItemMeta(meta);

            Bukkit.getScheduler().runTaskLater(DFanchovments.plugin, () -> {
                if (right.getAmount() <= 1) {
                    anvil.setItem(1, null);
                } else {
                    right.setAmount(right.getAmount() - 1);
                    anvil.setItem(1, right);
                }
            }, 1);
        }
    }

    private Map<Enchantment, Integer> getAllEnchants(ItemMeta meta) {
        if (meta instanceof EnchantmentStorageMeta storageMeta) {
            return new HashMap<>(storageMeta.getStoredEnchants());
        }
        return new HashMap<>(meta.getEnchants());
    }

    private void applyVanillaEnchants(ItemMeta meta, Map<Enchantment, Integer> enchants) {
        if (meta instanceof EnchantmentStorageMeta storage) {
            // removeStoredEnchant нельзя делать по keySet() напрямую — поэтому копия ключей
            for (Enchantment e : new ArrayList<>(storage.getStoredEnchants().keySet())) {
                storage.removeStoredEnchant(e);
            }
            for (Map.Entry<Enchantment, Integer> e : enchants.entrySet()) {
                storage.addStoredEnchant(e.getKey(), e.getValue(), false);
            }
        } else {
            for (Enchantment e : new ArrayList<>(meta.getEnchants().keySet())) {
                meta.removeEnchant(e);
            }
            for (Map.Entry<Enchantment, Integer> e : enchants.entrySet()) {
                meta.addEnchant(e.getKey(), e.getValue(), false);
            }
        }
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
        return ench.getTragers().contains(material.name().toUpperCase());
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

    private boolean isVanillaApplicableTo(Enchantment enchantment, ItemStack result) {
        Material material = result.getType();
        if (material == Material.ENCHANTED_BOOK) {
            return true;
        } 
        return enchantment.canEnchantItem(result);
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
