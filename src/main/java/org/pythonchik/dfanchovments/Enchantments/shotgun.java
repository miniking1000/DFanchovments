package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class shotgun extends CEnchantment implements Listener {
    private final NamespacedKey bypassKey;

    public shotgun(NamespacedKey id) {
        super(id);
        this.bypassKey = new NamespacedKey(DFanchovments.plugin, id.getKey() + "_bypass");
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onCrossbowShoot(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof AbstractArrow originalArrow)) return;
        if (!(originalArrow.getShooter() instanceof Player player)) return;

        ItemStack item = null;

        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (mainHand.getType() == Material.CROSSBOW && mainHand.hasItemMeta()) {
            if (mainHand.getItemMeta().getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) {
                item = mainHand;
            }
        }

        if (item == null) {
            ItemStack offHand = player.getInventory().getItemInOffHand();
            if (offHand.getType() == Material.CROSSBOW && offHand.hasItemMeta()) {
                if (offHand.getItemMeta().getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) {
                    item = offHand;
                }
            }
        }

        if (item == null) return;

        NamespacedKey hawkeyeKey = new NamespacedKey(DFanchovments.plugin, "hawkeye");
        boolean hasHawkeye = false;
        if (item.getItemMeta().getPersistentDataContainer().has(hawkeyeKey, PersistentDataType.INTEGER)) {
            hasHawkeye = true;
        }

        ThreadLocalRandom random = ThreadLocalRandom.current();
        int totalArrows = 5 + random.nextInt(6);

        originalArrow.getPersistentDataContainer().set(this.id, PersistentDataType.BYTE, (byte) 1);

        Vector originalVelocity = originalArrow.getVelocity();
        Location spawnLoc = originalArrow.getLocation();

        player.getWorld().playSound(spawnLoc, Sound.ENTITY_GENERIC_EXPLODE, 1.2F, 1.5F);
        player.getWorld().playSound(spawnLoc, Sound.ITEM_CROSSBOW_SHOOT, 1.5F, 0.5F);

        Vector direction = originalVelocity.clone().normalize();
        for (int i = 0; i < 15; i++) {
            double offsetX = direction.getX() + (random.nextDouble() - 0.5) * 0.4;
            double offsetY = direction.getY() + (random.nextDouble() - 0.5) * 0.4;
            double offsetZ = direction.getZ() + (random.nextDouble() - 0.5) * 0.4;

            player.getWorld().spawnParticle(
                    Particle.CLOUD,
                    spawnLoc.clone().add(direction.clone().multiply(0.5)),
                    0, offsetX, offsetY, offsetZ, 0.25
            );
        }

        List<AbstractArrow> volleyArrows = new ArrayList<>();

        for (int i = 0; i < totalArrows - 1; i++) {
            AbstractArrow newArrow = (AbstractArrow) player.getWorld().spawnEntity(spawnLoc, originalArrow.getType());
            newArrow.setShooter(player);
            newArrow.setDamage(originalArrow.getDamage());
            newArrow.setPierceLevel(originalArrow.getPierceLevel());
            newArrow.setKnockbackStrength(originalArrow.getKnockbackStrength());
            newArrow.setFireTicks(originalArrow.getFireTicks());
            newArrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);

            if (originalArrow instanceof Arrow oA && newArrow instanceof Arrow nA) {
                nA.setBasePotionType(oA.getBasePotionType());
                nA.setColor(oA.getColor());
                for (org.bukkit.potion.PotionEffect effect : oA.getCustomEffects()) {
                    nA.addCustomEffect(effect, true);
                }
            }

            double spread = 0.35;
            Vector spreadVec = new Vector(
                    (random.nextDouble() - 0.5) * spread,
                    (random.nextDouble() - 0.5) * spread,
                    (random.nextDouble() - 0.5) * spread
            );

            newArrow.setVelocity(originalVelocity.clone().add(spreadVec));
            newArrow.getPersistentDataContainer().set(this.id, PersistentDataType.BYTE, (byte) 1);

            volleyArrows.add(newArrow);
        }

        if (hasHawkeye && !volleyArrows.isEmpty()) {
            double searchRadius = 10.0;

            new BukkitRunnable() {
                final Map<AbstractArrow, LivingEntity> lockedTargets = new HashMap<>();
                int tickCounter = 0;

                @Override
                public void run() {
                    volleyArrows.removeIf(a -> {
                        if (a.isDead() || a.isInBlock() || a.isOnGround()) {
                            lockedTargets.remove(a);
                            return true;
                        }
                        return false;
                    });

                    if (volleyArrows.isEmpty()) {
                        this.cancel();
                        return;
                    }

                    tickCounter++;
                    boolean doRadar = (tickCounter % 3 == 0);

                    for (AbstractArrow activeArrow : volleyArrows) {
                        LivingEntity target = lockedTargets.get(activeArrow);

                        if ((target == null || target.isDead()) && doRadar) {
                            double closestDistSq = 100.0;
                            LivingEntity newTarget = null;

                            for (Entity e : activeArrow.getNearbyEntities(searchRadius, searchRadius, searchRadius)) {
                                if (e instanceof LivingEntity living && living != player && !living.isDead() && !(living instanceof ArmorStand)) {
                                    double distSq = activeArrow.getLocation().distanceSquared(living.getLocation());
                                    if (distSq < closestDistSq) {
                                        closestDistSq = distSq;
                                        newTarget = living;
                                    }
                                }
                            }

                            if (newTarget != null) {
                                lockedTargets.put(activeArrow, newTarget);
                                target = newTarget;
                            }
                        }

                        if (target != null && !target.isDead()) {
                            Location targetLoc = target.getLocation().add(0, target.getHeight() / 2.0, 0);
                            Vector currentVelocity = activeArrow.getVelocity();

                            double speed = Math.max(currentVelocity.length(), 1.5);
                            Vector directionToTarget = targetLoc.toVector().subtract(activeArrow.getLocation().toVector()).normalize();

                            Vector newVelocity = currentVelocity.normalize().multiply(0.1).add(directionToTarget.multiply(0.9)).normalize().multiply(speed);

                            activeArrow.setVelocity(newVelocity);
                            activeArrow.setGravity(false);
                        } else {
                            activeArrow.setGravity(true);
                        }
                    }
                }
            }.runTaskTimer(DFanchovments.plugin, 1L, 1L);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onArrowHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof AbstractArrow arrow)) return;
        if (!(event.getEntity() instanceof LivingEntity victim)) return;

        if (arrow.getPersistentDataContainer().has(this.id, PersistentDataType.BYTE)) {
            if (!victim.getPersistentDataContainer().has(bypassKey, PersistentDataType.INTEGER)) {
                int max = victim.getMaximumNoDamageTicks();
                victim.getPersistentDataContainer().set(bypassKey, PersistentDataType.INTEGER, max);

                victim.setMaximumNoDamageTicks(0);
                victim.setNoDamageTicks(0);

                Bukkit.getScheduler().runTask(DFanchovments.plugin, () -> {
                    if (victim.isValid()) {
                        Integer orig = victim.getPersistentDataContainer().get(bypassKey, PersistentDataType.INTEGER);
                        if (orig != null) {
                            victim.setMaximumNoDamageTicks(orig);
                            victim.getPersistentDataContainer().remove(bypassKey);
                        }
                    }
                });
            } else {
                victim.setNoDamageTicks(0);
            }
        }
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add(Material.CROSSBOW.name());
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Картечь");
        defaults.put("biomes", List.of("BADLANDS", "DESERT"));
        defaults.put("chance", 0.5);
        defaults.put("luck", 0.1);
        defaults.put("maxlvl", 1);
        return defaults;
    }

    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}