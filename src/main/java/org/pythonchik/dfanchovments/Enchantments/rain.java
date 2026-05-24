package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class rain extends CEnchantment implements Listener {

    private final NamespacedKey rainedKey;
    private final NamespacedKey bypassKey;

    public rain(NamespacedKey id) {
        super(id);
        this.rainedKey = new NamespacedKey(DFanchovments.plugin, id.getKey() + "_is_rain");
        this.bypassKey = new NamespacedKey(DFanchovments.plugin, id.getKey() + "_bypass");
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onArrowHit(ProjectileHitEvent event) {
        // Проверяем, что попали именно в сущность, а не в блок
        if (event.getHitEntity() == null) return;

        if (!(event.getEntity() instanceof AbstractArrow arrow)) return;
        if (!(arrow.getShooter() instanceof Player player)) return;

        // Предотвращаем рекурсию: если это стрела, упавшая с неба, она не может вызвать новый дождь
        if (arrow.getPersistentDataContainer().has(rainedKey, PersistentDataType.BYTE)) return;

        ItemStack bow = null;

        // Быстрая проверка предмета в основной руке
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (!mainHand.getType().isAir() && mainHand.hasItemMeta()) {
            if (mainHand.getItemMeta().getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) {
                bow = mainHand;
            }
        }

        // Быстрая проверка предмета в левой руке
        if (bow == null) {
            ItemStack offHand = player.getInventory().getItemInOffHand();
            if (!offHand.getType().isAir() && offHand.hasItemMeta()) {
                if (offHand.getItemMeta().getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) {
                    bow = offHand;
                }
            }
        }

        if (bow == null) return;

        // Получаем уровень ровно один раз
        int level = bow.getItemMeta().getPersistentDataContainer().getOrDefault(this.id, PersistentDataType.INTEGER, 1);

        // Шанс: 6% за каждый уровень
        double chance = level * 0.06;

        ThreadLocalRandom random = ThreadLocalRandom.current();

        if (random.nextDouble() <= chance) {
            arrow.getPersistentDataContainer().set(rainedKey, PersistentDataType.BYTE, (byte) 1);

            Location hitLoc = event.getHitEntity().getLocation();

            for (int i = 0; i < level; i++) {
                Location spawnLoc = hitLoc.clone().add(
                        random.nextDouble() - 0.5,
                        30 - (4.0 * level),
                        random.nextDouble() - 0.5
                );

                AbstractArrow rainArrow = (AbstractArrow) hitLoc.getWorld().spawnEntity(spawnLoc, EntityType.ARROW);
                rainArrow.setShooter(player);
                rainArrow.setVelocity(new Vector(0, -2.5 * level, 0));

                rainArrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                rainArrow.getPersistentDataContainer().set(rainedKey, PersistentDataType.BYTE, (byte) 1);
            }
            Location extraSpawn = hitLoc.clone().add(0, 5.0 + (5.0 * level), 0);
            AbstractArrow extraArrow = (AbstractArrow) hitLoc.getWorld().spawnEntity(extraSpawn, EntityType.ARROW);
            extraArrow.setShooter(player);
            extraArrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
            extraArrow.getPersistentDataContainer().set(rainedKey, PersistentDataType.BYTE, (byte) 1);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onRainArrowDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof AbstractArrow arrow)) return;
        if (!(event.getEntity() instanceof LivingEntity victim)) return;

        if (arrow.getPersistentDataContainer().has(rainedKey, PersistentDataType.BYTE)) {
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

    @EventHandler
    public void onResp(PlayerRespawnEvent event) {
        if (event.getPlayer().getPersistentDataContainer().has(bypassKey, PersistentDataType.INTEGER)) {
            Player player = event.getPlayer();
            Integer orig = player.getPersistentDataContainer().get(bypassKey, PersistentDataType.INTEGER);
            if (orig != null) {
                player.setMaximumNoDamageTicks(orig);
                player.getPersistentDataContainer().remove(bypassKey);
            }
        }
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("BOW");
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Шквал Стрел");
        defaults.put("biomes", List.of("WOODED_BADLANDS"));
        defaults.put("chance", 1.0);
        defaults.put("luck", 2.0);
        defaults.put("maxlvl", 5);
        return defaults;
    }

    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}