package org.pythonchik.dfanchovments.Enchantments.Dlegendary;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.pythonchik.dfanchovments.Bosses;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.Util;

import java.util.*;

public class proklyatost extends CEnchantment implements Listener {

    private static final Set<PotionEffectType> BENEFICIAL_EFFECTS = new HashSet<>();

    static {
        // Список положительных эффектов для блокировки
        addIfPresent("REGENERATION");
        addIfPresent("STRENGTH");
        addIfPresent("SPEED");
        addIfPresent("RESISTANCE");
        addIfPresent("FIRE_RESISTANCE");
        addIfPresent("WATER_BREATHING");
        addIfPresent("INVISIBILITY");
        addIfPresent("NIGHT_VISION");
        addIfPresent("HEALTH_BOOST");
        addIfPresent("ABSORPTION");
        addIfPresent("SATURATION");
        addIfPresent("HASTE");
        addIfPresent("SLOW_FALLING");
        addIfPresent("DOLPHINS_GRACE");
        addIfPresent("CONDUIT_POWER");
        addIfPresent("LUCK");
        addIfPresent("HERO_OF_THE_VILLAGE");
        addIfPresent("INSTANT_HEALTH");
        addIfPresent("JUMP_BOOST");
    }

    private static void addIfPresent(String name) {
        PotionEffectType type = PotionEffectType.getByName(name);
        if (type != null) {
            BENEFICIAL_EFFECTS.add(type);
        }
    }

    public proklyatost(NamespacedKey id) {
        super(id);
    }

    // --- 1. БЛОКИРОВКА ПОЛОЖИТЕЛЬНЫХ ЭФФЕКТОВ ЗЕЛИЙ ---
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPotionApply(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!hasHelmetEquipped(player)) return;

        if (event.getAction() == EntityPotionEffectEvent.Action.ADDED ||
                event.getAction() == EntityPotionEffectEvent.Action.CHANGED) {

            if (event.getNewEffect() != null && BENEFICIAL_EFFECTS.contains(event.getNewEffect().getType())) {
                event.setCancelled(true);
                player.getWorld().spawnParticle(Particle.SMOKE, player.getEyeLocation(), 8, 0.2, 0.2, 0.2, 0.05);
            }
        }
    }

    // --- 2. УДВОЕНИЕ ИСХОДЯЩЕГО УРОНА (x2) ---
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK &&
                event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            return;
        }

        if (!(event.getDamager() instanceof Player player)) return;

        if (hasHelmetEquipped(player)) {
            event.setDamage(event.getDamage() * 2.0);
        }
    }

    // Проверка зачарования только на надетом шлеме
    private boolean hasHelmetEquipped(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        if (helmet != null && !helmet.getType().isAir() && helmet.hasItemMeta()) {
            return helmet.getItemMeta().getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER);
        }
        return false;
    }

    @Override
    public List<String> getTragers() {
        List<String> retu = new ArrayList<>();
        retu.add(Material.ENCHANTED_BOOK.name());
        retu.addAll(Util.helmets());
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&4ᚢᚹᛟᛕᚳᚱᛠᛟᛈᛠ'");
        defaults.put("biomes", List.of("NETHER_WASTES", "BASALT_DELTAS", "DEEP_DARK"));
        defaults.put("chance", 0.005);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 1);
        Map<String, Object> bosses = new LinkedHashMap<>();
        bosses.put(Bosses.Rarity.MYTHIC.getName(), 10);

        defaults.put("bosses", bosses);
        return defaults;
    }


}