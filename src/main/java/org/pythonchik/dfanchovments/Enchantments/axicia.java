package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.List;

public class axicia extends CEnchantment implements Listener {
    //название это ножницы на латыни
    NamespacedKey id;
    public axicia(NamespacedKey id) {
        this.id = id;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        if (world.getEnvironment() != World.Environment.THE_END) return; // Проверяем только в Краю

        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null || from.getBlockY() == to.getBlockY()) return; // Проверяем только изменения по Y

        if (to.getBlockY() >= 0) return;

        ItemStack boots = player.getInventory().getBoots();
        if (boots != null && boots.hasItemMeta() && boots.getItemMeta().getPersistentDataContainer().has(id)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 30 * 20, 1, false, false, true));
        }
    }
    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");

        //bots
        retu.add("LEATHER_BOOTS");
        retu.add("CHAINMAIL_BOOTS");
        retu.add("IRON_BOOTS");
        retu.add("DIAMOND_BOOTS");
        retu.add("GOLDEN_BOOTS");
        retu.add("NETHERITE_BOOTS");
        return retu;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
