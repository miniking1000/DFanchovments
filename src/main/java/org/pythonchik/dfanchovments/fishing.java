package org.pythonchik.dfanchovments;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.type.Switch;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.joml.Math;
import org.joml.Random;

public class fishing implements Listener {

    @EventHandler
    public void onFish(PlayerFishEvent event) {

        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH)
            return;
        Entity entity = event.getCaught();
        if (!(entity instanceof Item))
            return;
        Player player = event.getPlayer();
        FileConfiguration config = DFanchovments.getConfig1();
        for (String cfg : config.getKeys(false)){
            if (player.getLocation().getWorld().getBiome(player.getLocation()).equals(Biome.FOREST)) { //valueOf(config.getString(cfg + ".biome"))
                if (Math.random()*100 < config.getDouble(cfg + ".chance")+(config.getInt(cfg + ".luck")*(player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.LUCK) ? player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LUCK) : 0))){
                    ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
                    for (CEnchantment enchs : DFanchovments.CEnchantments) {
                        if (enchs.getName().equals(config.getString(cfg + ".name"))) {
                            book.addUnsafeEnchantment(enchs,Math.max(1,new Random().nextInt(enchs.getMaxLevel())));
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
