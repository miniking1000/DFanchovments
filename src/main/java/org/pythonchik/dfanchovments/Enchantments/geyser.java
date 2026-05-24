package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class geyser extends CEnchantment implements Listener {

    private final NamespacedKey triggeredKey;

    public geyser(NamespacedKey id) {
        super(id);
        this.triggeredKey = new NamespacedKey(DFanchovments.plugin, id.getKey() + "_triggered");
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onTridentHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Trident trident)) return;
        if (!(trident.getShooter() instanceof Player player)) return;

        if (trident.getPersistentDataContainer().has(triggeredKey, PersistentDataType.BYTE)) return;

        ItemStack item = trident.getItem();
        if (item.getType().isAir() || !item.hasItemMeta()) return;

        if (!item.getItemMeta().getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) return;

        trident.getPersistentDataContainer().set(triggeredKey, PersistentDataType.BYTE, (byte) 1);

        Location hitLoc = trident.getLocation();

        double radius = 7.5;
        double upForce = 2.05;

        // --- ЭПИЧНЫЕ ЗВУКИ ВЗРЫВА (Громкость снижена на 30%) ---
        // 1. Ударная волна Вардена (дает глубокий бас)
        player.getWorld().playSound(hitLoc, Sound.ENTITY_WARDEN_SONIC_BOOM, 0.35f, 1.5f);
        // 2. Резкий раскат грома
        player.getWorld().playSound(hitLoc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.35f, 2.0f);
        // 3. Мощный всплеск воды на высокой скорости
        player.getWorld().playSound(hitLoc, Sound.ENTITY_PLAYER_SPLASH_HIGH_SPEED, 1.05f, 0.5f);

        player.getWorld().spawnParticle(Particle.SPLASH, hitLoc, 200, radius / 2, 1.0, radius / 2, 0.2);
        player.getWorld().spawnParticle(Particle.BUBBLE_POP, hitLoc, 50, radius / 2, 0.5, radius / 2, 0.1);

        // ПОДБРАСЫВАЕТ ВООБЩЕ ВСЕХ (Других игроков, мобов и самого владельца трезубца)
        for (Entity e : trident.getNearbyEntities(radius, radius, radius)) {
            if (e instanceof LivingEntity living) {

                Vector knockup = new Vector(0, upForce, 0);

                Vector currentVelocity = living.getVelocity();
                knockup.setX(currentVelocity.getX() * 0.5);
                knockup.setZ(currentVelocity.getZ() * 0.5);

                living.setVelocity(knockup);
            }
        }
    }

    @Override
    public List<String> getTragers() {
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("TRIDENT");
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&bГейзер");
        defaults.put("biomes", List.of("OCEAN", "BEACH", "SWAMP"));
        defaults.put("chance", 0.05);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 1);
        return defaults;
    }

    @Override
    public NamespacedKey getId() {
        return this.id;
    }
}