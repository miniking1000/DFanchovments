package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class frostbite extends CEnchantment implements Listener {

    public frostbite(NamespacedKey id) {
        super(id);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity target)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK
                && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            return;
        }
        ItemStack sword = player.getInventory().getItemInMainHand();
        if (sword.getType().isAir() || !sword.getType().name().endsWith("_SWORD")) return;
        if (!sword.hasItemMeta()) return;

        ItemMeta meta = sword.getItemMeta();

        if (!meta.getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) return;

        int level = meta.getPersistentDataContainer().getOrDefault(this.id, PersistentDataType.INTEGER, 1);
        int freezeTicksToAdd = level * 60;

        int newFreezeTicks = target.getFreezeTicks() + freezeTicksToAdd;
        if (newFreezeTicks > 400) newFreezeTicks = 400;

        target.setFreezeTicks(newFreezeTicks);

        target.getWorld().playSound(target.getLocation(), Sound.BLOCK_GLASS_BREAK, 0.5f, 1.5f);
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("WOODEN_SWORD");
        retu.add("STONE_SWORD");
        retu.add("IRON_SWORD");
        retu.add("GOLDEN_SWORD");
        retu.add("DIAMOND_SWORD");
        retu.add("NETHERITE_SWORD");
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&bОбморожение");
        defaults.put("biomes", List.of("ICE_SPIKES", "SNOWY_TAIGA", "FROZEN_OCEAN"));
        defaults.put("chance", 0.2);
        defaults.put("luck", 0.05);
        defaults.put("maxlvl", 3);
        return defaults;
    }

    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}