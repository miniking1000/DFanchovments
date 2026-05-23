package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class hawkeye extends CEnchantment implements Listener {

    public hawkeye(NamespacedKey id) {
        super(id);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBowShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack bow = event.getBow();
        if (bow == null || !bow.hasItemMeta()) return;

        if (!bow.getItemMeta().getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) return;

        if (!(event.getProjectile() instanceof Arrow arrow)) return;

        double searchRadius = 10.0;

        new BukkitRunnable() {
            LivingEntity lockedTarget = null;
            int tickCounter = 0;

            @Override
            public void run() {
                if (arrow.isDead() || arrow.isInBlock() || arrow.isOnGround()) {
                    this.cancel();
                    return;
                }

                tickCounter++;

                if ((lockedTarget == null || lockedTarget.isDead()) && tickCounter % 3 == 0) {
                    double closestDistSq = 100.0;

                    for (Entity e : arrow.getNearbyEntities(searchRadius, searchRadius, searchRadius)) {
                        if (e instanceof LivingEntity living && living != player && !living.isDead() && !(living instanceof ArmorStand)) {
                            double distSq = arrow.getLocation().distanceSquared(living.getLocation());

                            if (distSq < closestDistSq) {
                                closestDistSq = distSq;
                                lockedTarget = living;
                            }
                        }
                    }
                }

                if (lockedTarget != null && !lockedTarget.isDead()) {
                    Location targetLoc = lockedTarget.getLocation().add(0, lockedTarget.getHeight() / 2.0, 0);
                    Vector currentVelocity = arrow.getVelocity();

                    double speed = Math.max(currentVelocity.length(), 1.5);
                    Vector directionToTarget = targetLoc.toVector().subtract(arrow.getLocation().toVector()).normalize();

                    Vector newVelocity = currentVelocity.normalize().multiply(0.1).add(directionToTarget.multiply(0.9)).normalize().multiply(speed);

                    arrow.setVelocity(newVelocity);
                    arrow.setGravity(false);
                } else {
                    arrow.setGravity(true);
                }
            }
        }.runTaskTimer(DFanchovments.plugin, 1L, 1L);
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("BOW");
        retu.add("CROSSBOW");
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Баллистика");
        defaults.put("biomes", List.of("FOREST", "TAIGA", "SNOWY_TAIGA"));
        defaults.put("chance", 0);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 1);
        return defaults;
    }

    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}