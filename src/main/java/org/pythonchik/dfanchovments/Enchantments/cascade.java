package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class cascade extends CEnchantment implements Listener {

    public cascade(NamespacedKey id) {
        super(id);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onTridentHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Trident trident)) return;
        if (!(trident.getShooter() instanceof Player player)) return;

        ItemStack item = trident.getItem();
        if (item.getType().isAir() || !item.hasItemMeta()) return;

        if (!item.getItemMeta().getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) return;

        // --- НАСТРОЙКИ КАСКАДА ---
        int maxJumps = 9;         // Сколько целей максимум поразит молния
        double jumpRadius = 7.0;  // Максимальное расстояние между целями
        double damage = 3.0;      // Урон от каждого удара молнии (3 сердечка)

        // Множество, чтобы не бить одну и ту же цель дважды (и не ударить самого себя)
        Set<Entity> hitEntities = new HashSet<>();
        hitEntities.add(player);

        Location currentLoc;

        // Если попали прямо в моба - начинаем каскад от него
        if (event.getHitEntity() instanceof LivingEntity victim) {
            currentLoc = victim.getLocation().add(0, victim.getHeight() / 2.0, 0);
            hitEntities.add(victim); // Добавляем первую жертву в игнор
        }
        // Если промахнулись и попали в блок - каскад начнется от самого трезубца
        else if (event.getHitBlock() != null) {
            currentLoc = trident.getLocation();
        } else {
            return;
        }

        // Запускаем цепную реакцию
        for (int i = 0; i < maxJumps; i++) {
            LivingEntity nextTarget = null;
            double closestDistSq = jumpRadius * jumpRadius;

            // Ищем ближайшего моба к текущей точке
            for (Entity e : currentLoc.getWorld().getNearbyEntities(currentLoc, jumpRadius, jumpRadius, jumpRadius)) {
                if (e instanceof LivingEntity living && !hitEntities.contains(living) && !living.isDead() && !(living instanceof ArmorStand)) {
                    double distSq = currentLoc.distanceSquared(living.getLocation());

                    if (distSq < closestDistSq) {
                        closestDistSq = distSq;
                        nextTarget = living;
                    }
                }
            }

            // Если в радиусе больше нет врагов - прерываем цепную молнию
            if (nextTarget == null) break;

            Location targetLoc = nextTarget.getLocation().add(0, nextTarget.getHeight() / 2.0, 0);

            // 1. Рисуем красивый луч из электрических искр
            double dist = currentLoc.distance(targetLoc);
            Vector dir = targetLoc.toVector().subtract(currentLoc.toVector()).normalize().multiply(0.5);
            Location particleLoc = currentLoc.clone();

            for (double step = 0; step < dist; step += 0.5) {
                particleLoc.add(dir);
                particleLoc.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, particleLoc, 2, 0.1, 0.1, 0.1, 0);
                particleLoc.getWorld().spawnParticle(Particle.GLOW, particleLoc, 1, 0, 0, 0, 0);
            }

            // 2. Наносим урон
            nextTarget.damage(damage, player);
            hitEntities.add(nextTarget);

            // 3. Звук настоящей молнии и грома (сделан очень тихим - 0.15f)
            targetLoc.getWorld().playSound(targetLoc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.15f, 1.5f);
            targetLoc.getWorld().playSound(targetLoc, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 0.15f, 1.2f);

            // Смещаем центр поиска: следующая молния полетит уже от этой цели
            currentLoc = targetLoc;
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
        defaults.put("name", "&7Цепная молния");
        defaults.put("biomes", List.of("OCEAN", "DEEP_COLD_OCEAN", "RIVER"));
        defaults.put("chance", 0.07);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 1);
        return defaults;
    }

    @Override
    public NamespacedKey getId() {
        return this.id;
    }
}