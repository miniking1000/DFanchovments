package org.pythonchik.dfanchovments.Enchantments.Dlegendary;

import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.pythonchik.dfanchovments.Bosses;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.Util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class turbine extends CEnchantment implements Listener {

    public turbine(NamespacedKey id) {
        super(id);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDazeHit(EntityDamageByEntityEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity victim)) return;

        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (mainHand.getType().isAir() || !mainHand.hasItemMeta()) return;
        if (!mainHand.getType().name().endsWith("_SPEAR")) return;
        if (!mainHand.getItemMeta().getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) return;

        // Шанс срабатывания 10%
        if (Math.random() > 0.20) return;

        // 1. Разворот противника на 180 градусов
        Location victimLoc = victim.getLocation();
        victimLoc.setYaw((victimLoc.getYaw() + 180.0f) % 360.0f);
        victim.teleport(victimLoc);

        // 2. Наложение эффекта тошноты на 10 секунд (200 тиков)
        victim.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 200, 0, false, true, true));

        // Звуковой эффект звона/оглушения и частицы
        victim.getWorld().playSound(victim.getLocation(), Sound.BLOCK_BELL_USE, 1.2f, 1.5f);
        victim.getWorld().playSound(victim.getLocation(), Sound.BLOCK_ANVIL_LAND, 0.6f, 1.8f);
    }

    @Override
    public List<String> getTragers() {
        List<String> retu = new ArrayList<>();
        retu.add(Material.ENCHANTED_BOOK.name());
        retu.addAll(Util.spears());
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Оглушение");
        defaults.put("biomes", List.of("DARK_FOREST", "SWAMP", "JUNGLE"));
        defaults.put("chance", 0.2);
        defaults.put("luck", 0.05);
        defaults.put("maxlvl", 1);

        Map<String, Object> bosses = new LinkedHashMap<>();
        bosses.put(Bosses.Rarity.LEGENDARY.getName(), 10);

        defaults.put("bosses", bosses);
        return defaults;
    }


}