package org.pythonchik.dfanchovments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.N;
import org.joml.Math;
import org.joml.Random;

import java.util.ArrayList;
import java.util.List;

public class fishing implements Listener {

    Message message = DFanchovments.getMessage();

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH)
            return;
        if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.FISHING_ROD)) {
            Entity entity = event.getCaught();
            if (!(entity instanceof Item))
                return;
            Player player = event.getPlayer();
            FileConfiguration config = DFanchovments.getConfig1();
            for (String cfg : config.getKeys(false)) {
                if (player.getLocation().getWorld().getBiome(player.getLocation()).equals(Biome.valueOf(config.getString(cfg + ".biome")))) {
                    if (Math.random() * 100 < config.getDouble(cfg + ".chance") + (config.getDouble(cfg + ".luck") * ((player.getInventory().getItemInMainHand().getItemMeta() != null) ? (player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.LUCK_OF_THE_SEA) ? player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LUCK_OF_THE_SEA) : 0) : 0))) {
                        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
                        for (CEnchantment enchs : DFanchovments.CEnchantments) {
                            if (enchs.getName().equals(config.getString(cfg + ".name"))) {
                                ItemMeta tobeMeta = book.getItemMeta();
                                tobeMeta.getPersistentDataContainer().set(enchs.getId(), PersistentDataType.INTEGER,Math.max(1, Math.min(new Random().nextInt(enchs.getMaxLevel() + 1), enchs.getMaxLevel())));
                                int lvl = tobeMeta.getPersistentDataContainer().get(enchs.getId(),PersistentDataType.INTEGER);
                                if (enchs.equals(DFanchovments.democracy)) {
                                    tobeMeta.addAttributeModifier(Attribute.SCALE, new AttributeModifier(Attribute.SCALE.getKey(), 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD));
                                }
                                List<String> lore = new ArrayList<String>();
                                lore.add(message.hex(enchs.getName() + " " + (lvl == 1 ? "I" : lvl == 2 ? "II" : lvl == 3 ? "III" : lvl == 4 ? "IV" : "V")));
                                tobeMeta.setLore(lore);

                                tobeMeta.setEnchantmentGlintOverride(true);
                                book.setItemMeta(tobeMeta);

                                Item item = (Item) entity;
                                item.setItemStack(book);
                                break;
                            }
                        }
                    }
                }
            }
        } else if (event.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.FISHING_ROD)) {
            Entity entity = event.getCaught();
            if (!(entity instanceof Item))
                return;
            Player player = event.getPlayer();
            FileConfiguration config = DFanchovments.getConfig1();
            for (String cfg : config.getKeys(false)) {
                if (player.getLocation().getWorld().getBiome(player.getLocation()).equals(Biome.valueOf(config.getString(cfg + ".biome")))) {
                    if (Math.random() * 100 < config.getDouble(cfg + ".chance") + (config.getInt(cfg + ".luck") * ((player.getInventory().getItemInOffHand().getItemMeta() != null) ? (player.getInventory().getItemInOffHand().getItemMeta().hasEnchant(Enchantment.LUCK_OF_THE_SEA) ? player.getInventory().getItemInOffHand().getEnchantmentLevel(Enchantment.LUCK_OF_THE_SEA) : 0) : 0))) {
                        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
                        for (CEnchantment enchs : DFanchovments.CEnchantments) {
                            if (enchs.getName().equals(config.getString(cfg + ".name"))) {
                                ItemMeta tobeMeta = book.getItemMeta();
                                tobeMeta.getPersistentDataContainer().set(enchs.getId(), PersistentDataType.INTEGER,Math.max(1, Math.min(new Random().nextInt(enchs.getMaxLevel() + 1), enchs.getMaxLevel())));
                                int lvl = tobeMeta.getPersistentDataContainer().get(enchs.getId(),PersistentDataType.INTEGER);
                                List<String> lore = new ArrayList<String>();
                                lore.add(message.hex(enchs.getName() + " " + (lvl == 1 ? "I" : lvl == 2 ? "II" : lvl == 3 ? "III" : lvl == 4 ? "IV" : "V")));
                                tobeMeta.setLore(lore);

                                tobeMeta.setEnchantmentGlintOverride(true);
                                book.setItemMeta(tobeMeta);

                                Item item = (Item) entity;
                                item.setItemStack(book);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
