package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class martyr_curse extends CEnchantment implements Listener {

    private final NamespacedKey primedKey;

    public martyr_curse(NamespacedKey id) {
        super(id);
        this.primedKey = new NamespacedKey(DFanchovments.plugin, id.getKey() + "_primed");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMartyrDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (player.getPersistentDataContainer().has(this.primedKey, PersistentDataType.BYTE)) {
            if (event.getCause() != EntityDamageEvent.DamageCause.VOID) {
                event.setCancelled(true);
            }
            return;
        }

        ItemStack leggings = player.getInventory().getLeggings();
        if (leggings == null || !leggings.hasItemMeta()) return;
        if (!leggings.getItemMeta().getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) return;

        double finalHealth = player.getHealth() - event.getFinalDamage();

        if (finalHealth > 0 && finalHealth <= 4.0) {

            player.getPersistentDataContainer().set(this.primedKey, PersistentDataType.BYTE, (byte) 1);

            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1.5f, 1.0f);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_TNT_PRIMED, 1.5f, 1.0f);

            new BukkitRunnable() {
                int ticks = 0;

                @Override
                public void run() {
                    if (!player.isOnline() || player.isDead()) {
                        player.getPersistentDataContainer().remove(primedKey);
                        this.cancel();
                        return;
                    }

                    Location loc = player.getLocation().add(0, 1, 0);

                    player.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 3, 0.3, 0.5, 0.3, 0.05);
                    player.getWorld().spawnParticle(Particle.LAVA, loc, 1, 0.3, 0.5, 0.3, 0);

                    if (ticks % 10 == 0) {
                        float pitch = 1.0f + (ticks / 100.0f);
                        player.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_HAT, 1.5f, pitch);
                        player.getWorld().playSound(loc, Sound.UI_BUTTON_CLICK, 1.0f, pitch);
                    }

                    if (ticks >= 100) {

                        Location center = player.getLocation();

                        center.getWorld().createExplosion(center, 8.0f, false, false);

                        for (Entity e : center.getWorld().getNearbyEntities(center, 6.0, 6.0, 6.0)) {
                            if (e instanceof LivingEntity victim && victim != player) {
                                victim.damage(100.0, player);
                            }
                        }

                        player.setHealth(0.0);

                        this.cancel();
                    }
                    ticks++;
                }
            }.runTaskTimer(DFanchovments.plugin, 0L, 1L);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTotemResurrect(EntityResurrectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.getPersistentDataContainer().has(this.primedKey, PersistentDataType.BYTE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player.getPersistentDataContainer().has(this.primedKey, PersistentDataType.BYTE)) {
            player.getPersistentDataContainer().remove(this.primedKey);
        }
    }

    @Override
    public List<String> getTragers() {
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("LEATHER_LEGGINGS");
        retu.add("CHAINMAIL_LEGGINGS");
        retu.add("IRON_LEGGINGS");
        retu.add("GOLDEN_LEGGINGS");
        retu.add("DIAMOND_LEGGINGS");
        retu.add("NETHERITE_LEGGINGS");
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&cПроклятие Мученика");
        defaults.put("biomes", List.of("SOUL_SAND_VALLEY", "CRIMSON_FOREST"));
        defaults.put("chance", 0.1);
        defaults.put("luck", 0.05);
        defaults.put("maxlvl", 1);
        return defaults;
    }

    @Override
    public NamespacedKey getId() {
        return this.id;
    }
}