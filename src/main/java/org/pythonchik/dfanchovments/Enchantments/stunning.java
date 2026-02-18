package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.List;

public class stunning extends CEnchantment implements Listener {
    public stunning(NamespacedKey id) {
        super(id);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.MACE) return;
        if (player.getFallDistance() <= 5.0f) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().has(this.id)) return;

        int level = meta.getPersistentDataContainer().get(this.id, PersistentDataType.INTEGER);
        if (event.getEntity() instanceof LivingEntity target) {
            target.getWorld().spawnParticle(Particle.CRIT, target.getLocation(), 50, 0.2, 0.5, 0.2);
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 5*20, (2*level)-1));
            target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 5*20, (2*level)-1));
            target.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 5*20, (2*level)-1));
            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5*20, 0));
        }
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("MACE");
        return retu;
    }
    @Override
    public java.util.Map<String, Object> getDefaultConfig() {
        java.util.Map<String, Object> defaults = new java.util.LinkedHashMap<>();
        defaults.put("name", "&7Оглушающий удар");
        defaults.put("biome", "FROZEN_PEAKS");
        defaults.put("chance", 0.08);
        defaults.put("luck", 0.4);
        defaults.put("maxlvl", 3);
        return defaults;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
