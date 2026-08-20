package org.pythonchik.dfanchovments.Enchantments.Dlegendary;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.pythonchik.dfanchovments.Bosses;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class shield_ram extends CEnchantment implements Listener {

    private final NamespacedKey hitDelayKey;

    public shield_ram(NamespacedKey id) {
        super(id);
        this.hitDelayKey = new NamespacedKey(DFanchovments.plugin, id.getKey() + "_delay");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onShieldRam(PlayerMoveEvent event) {
        // Срабатывает только при физическом перемещении по X или Z
        if (event.getFrom().getX() == event.getTo().getX() && event.getFrom().getZ() == event.getTo().getZ()) return;

        Player player = event.getPlayer();
        if (!player.isBlocking()) return;

        // Проверяем наличие зачарованного щита в обеих руках
        ItemStack shield = player.getItemInUse();
        if (shield == null) return;

        Location frontLoc = player.getLocation().add(player.getLocation().getDirection().setY(0).normalize().multiply(1.0)).add(0, 0.5, 0);
        long currentTime = System.currentTimeMillis();

        // Проверяем цели перед щитом (в радиусе 1.3 блока)
        for (Entity entity : player.getWorld().getNearbyEntities(frontLoc, 1.3, 1.0, 1.3)) {
            if (entity instanceof LivingEntity victim && victim != player && victim.isValid() && !victim.isDead()) {

                // Защита от непрерывного урона одной и той же цели при отталкивании (0.4 сек)
                long lastHit = victim.getPersistentDataContainer().getOrDefault(hitDelayKey, PersistentDataType.LONG, 0L);
                if (currentTime - lastHit < 400) continue;
                victim.getPersistentDataContainer().set(hitDelayKey, PersistentDataType.LONG, currentTime);

                // 1. Нанесение 1 ед. урона (0.5 сердца)
                victim.damage(1.0, player);

                // 2. Мощное отбрасывание вперед и вверх
                Vector push = player.getLocation().getDirection().setY(0).normalize().multiply(1.6).setY(0.35);
                victim.setVelocity(push);

                // Эффекты удара
                victim.getWorld().playSound(victim.getLocation(), Sound.ITEM_SHIELD_BLOCK, 0.7f, 0.8f);
                victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, 0.7f, 1.0f);
            }
        }
    }

    private boolean hasEnchant(ItemStack item) {
        if (item == null || item.getType() != Material.SHIELD || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER);
    }

    @Override
    public List<String> getTragers() {
        List<String> retu = new ArrayList<>();
        retu.add(Material.ENCHANTED_BOOK.name());
        retu.add(Material.SHIELD.name());
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Таран");
        defaults.put("biomes", List.of("PLAINS", "SAVANNA", "STONY_PEAKS"));
        defaults.put("chance", 0.25);
        defaults.put("luck", 0.05);
        defaults.put("maxlvl", 1);

        Map<String, Object> bosses = new LinkedHashMap<>();
        bosses.put(Bosses.Rarity.LEGENDARY.getName(), 10);

        defaults.put("bosses", bosses);

        return defaults;
    }


}