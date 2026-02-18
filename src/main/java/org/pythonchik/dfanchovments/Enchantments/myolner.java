package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class myolner extends CEnchantment implements Listener {
    public myolner(NamespacedKey id) {
        super(id);
    }
    private HashMap<Entity, BukkitTask> tasks = new HashMap<>();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.MACE) return;
        if (player.getFallDistance() <= 5.0f) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().has(this.id)) return;
        int level = meta.getPersistentDataContainer().get(this.id, PersistentDataType.INTEGER);
        Entity target = event.getEntity();
        Location hitLocation = target.getLocation().clone();
        World world = hitLocation.getWorld();
        if (world == null) return;

        double chance;
        if (world.isThundering()) {
            chance = 0.15*level;
        } else if (world.hasStorm()) {
            chance = 0.1*level;
        } else {
            return;
        }

        if (Math.random() > chance) {
            return;
        }

        world.spawnParticle(
                Particle.ELECTRIC_SPARK,
                hitLocation.add(0, 1, 0),
                50,
                0.5, 1.0, 0.5,
                0.05
        );

        Bukkit.getScheduler().runTaskLater(
                DFanchovments.plugin,
                () -> {
                    if (!target.isValid()) return;
                    if (target instanceof LivingEntity living && living.isDead()) return;
                    world.strikeLightning(hitLocation);
                },
                25L
        );

    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("MACE");
        return retu;
    }
    @Override
    public java.util.Map<String, Object> getDefaultConfig() {
        java.util.Map<String, Object> defaults = new java.util.LinkedHashMap<>();
        defaults.put("name", "&7Искромётность");
        defaults.put("biome", "DEEP_COLD_OCEAN");
        defaults.put("chance", 0.5);
        defaults.put("luck", 0.05);
        defaults.put("maxlvl", 3);
        return defaults;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
