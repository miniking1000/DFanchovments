package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class bamboom extends CEnchantment implements Listener {

    private final Map<UUID, Long> lastShotTime = new HashMap<>();
    private final Map<UUID, Boolean> lastShotResult = new HashMap<>();

    public bamboom(NamespacedKey id) {
        super(id);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onCrossShoot(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();

        if (!(projectile.getShooter() instanceof Player player)) {
            return;
        }

        ItemStack crossbow = null;

        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (mainHand.getType() == Material.CROSSBOW && mainHand.hasItemMeta()) {
            crossbow = mainHand;
        } else {
            ItemStack offHand = player.getInventory().getItemInOffHand();
            if (offHand.getType() == Material.CROSSBOW && offHand.hasItemMeta()) {
                crossbow = offHand;
            }
        }

        if (crossbow == null) return;

        ItemMeta meta = crossbow.getItemMeta();
        if (!meta.getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) return;

        int level = meta.getPersistentDataContainer().getOrDefault(this.id, PersistentDataType.INTEGER, 1);

        long now = System.currentTimeMillis();
        boolean isBoom = false;
        boolean isFirstArrow = true;

        if (lastShotTime.containsKey(player.getUniqueId()) && (now - lastShotTime.get(player.getUniqueId()) < 50)) {
            isBoom = lastShotResult.getOrDefault(player.getUniqueId(), false);
            isFirstArrow = false;
        } else {
            double chance = level * 0.12;
            isBoom = ThreadLocalRandom.current().nextDouble() <= chance;

            lastShotTime.put(player.getUniqueId(), now);
            lastShotResult.put(player.getUniqueId(), isBoom);
        }

        if (isBoom) {
            if (isFirstArrow) {
                projectile.getWorld().playSound(projectile.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 0.5f, 1.0f);
            }

            projectile.getWorld().spawnParticle(Particle.SONIC_BOOM, projectile.getLocation(), 1);
            projectile.setVelocity(projectile.getVelocity().multiply(1.0 + (0.5 * level)));
        }
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("CROSSBOW");
        return retu;
    }
    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Звуковой заряд");
        defaults.put("biomes", List.of("DEEP_DARK"));
        defaults.put("chance", 1.0);
        defaults.put("luck", 3.0);
        defaults.put("maxlvl", 3);
        return defaults;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
