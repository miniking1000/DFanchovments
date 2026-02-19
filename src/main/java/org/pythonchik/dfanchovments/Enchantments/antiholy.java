package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.Util;

import java.util.*;

public class antiholy extends CEnchantment implements Listener {
    ArrayList<PotionEffectType> badeffects = new ArrayList<>();
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
    }
    @EventHandler
    public void event(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        ItemStack chestplate = player.getInventory().getChestplate();
        if (chestplate == null) return;
        if (!chestplate.hasItemMeta()) return;
        if (!chestplate.getItemMeta().getPersistentDataContainer().has(id)) return;
        if (event.getNewEffect() == null) return;
        // Cancel only if a bad effect is being applied or changed
        if (badeffects.contains(event.getModifiedType())) {
            event.setCancelled(true);
        }
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");

        retu.addAll(Util.chestplates());
        return retu;
    }
    @Override
    public java.util.Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&6Освещённость");
        defaults.put("biomes", java.util.List.of("minecraft:pale_garden"));
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
