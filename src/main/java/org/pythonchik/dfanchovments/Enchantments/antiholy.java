package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;
import org.pythonchik.dfanchovments.Util;

import java.util.*;

public class antiholy extends CEnchantment implements Listener {
    private static final long CACHE_TICKS = 20L;

    private final Set<PotionEffectType> badeffects = new HashSet<>();
    private final Map<UUID, Boolean> armorCache = new HashMap<>();

    public antiholy(NamespacedKey id) {
        super(id);
        this.id = id;
        badeffects.add(PotionEffectType.SLOWNESS);
        badeffects.add(PotionEffectType.MINING_FATIGUE);
        badeffects.add(PotionEffectType.INSTANT_DAMAGE);
        badeffects.add(PotionEffectType.NAUSEA);
        badeffects.add(PotionEffectType.BLINDNESS);
        badeffects.add(PotionEffectType.HUNGER);
        badeffects.add(PotionEffectType.WEAKNESS);
        badeffects.add(PotionEffectType.POISON);
        badeffects.add(PotionEffectType.LEVITATION);
        badeffects.add(PotionEffectType.UNLUCK);
        badeffects.add(PotionEffectType.WEAVING);
        badeffects.add(PotionEffectType.WIND_CHARGED);
        badeffects.add(PotionEffectType.INFESTED);
        badeffects.add(PotionEffectType.OOZING);
        badeffects.add(PotionEffectType.WITHER);
        badeffects.add(PotionEffectType.DARKNESS);

        Bukkit.getScheduler().runTaskTimer(DFanchovments.plugin, armorCache::clear, CACHE_TICKS, CACHE_TICKS);
    }

    @EventHandler(ignoreCancelled = true)
    public void event(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getNewEffect() == null) return;

        if (!badeffects.contains(event.getModifiedType())) return;

        if (hasAntiholyCached(player)) {
            event.setCancelled(true);
        }
    }

    private boolean hasAntiholyCached(Player player) {
        UUID uuid = player.getUniqueId();
        if (armorCache.containsKey(uuid)) {
            return armorCache.get(uuid);
        } else {
            boolean res = hasAntiholyChestplate(player);
            armorCache.put(player.getUniqueId(), res);
            return res;
        }
    }

    private boolean hasAntiholyChestplate(Player player) {
        ItemStack chestplate = player.getInventory().getChestplate();
        if (chestplate == null || chestplate.getType().isAir()) return false;

        ItemMeta meta = chestplate.getItemMeta();
        if (meta == null) return false;

        return meta.getPersistentDataContainer().has(id);
    }


    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");

        retu.addAll(Util.chestplates());
        return retu;
    }
    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&6Освещённость");
        defaults.put("biomes", List.of("pale_garden"));
        defaults.put("chance", 0.01);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 1);
        return defaults;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
