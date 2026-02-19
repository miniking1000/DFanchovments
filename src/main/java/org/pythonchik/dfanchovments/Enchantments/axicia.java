package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.Util;

import java.util.*;

public class axicia extends CEnchantment implements Listener {
    //название это ножницы на латыни
    public axicia(NamespacedKey id) {
        super(id);
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

        retu.addAll(Util.boots());
        return retu;
    }
    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Связь пустоты");
        defaults.put("biomes", List.of("WARPED_FOREST"));
        defaults.put("chance", 0.4);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 1);
        return defaults;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
