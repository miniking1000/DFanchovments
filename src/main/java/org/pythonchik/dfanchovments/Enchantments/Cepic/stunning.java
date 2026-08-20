package org.pythonchik.dfanchovments.Enchantments.Cepic;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.pythonchik.dfanchovments.Bosses;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class stunning extends CEnchantment implements Listener {
    public stunning(NamespacedKey id) {
        super(id);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        ItemStack item;
        if (player.getItemInUse() != null) {
            item = player.getItemInUse();
        } else {
            item = player.getInventory().getItemInMainHand();
        }
        if (item.getType() != Material.MACE) return;
        if (player.getFallDistance() <= 5.0f) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK
                && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().has(this.id)) return;

        int level = meta.getPersistentDataContainer().get(this.id, PersistentDataType.INTEGER);
        if (event.getEntity() instanceof LivingEntity target) {
            target.getWorld().spawnParticle(Particle.CRIT, target.getLocation(), 50, 0.2, 0.5, 0.2);
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 5 * 20, (2 * level) - 1));
            target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 5 * 20, (2 * level) - 1));
            target.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 5 * 20, (2 * level) - 1));
            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 0));
        }
    }

    @Override
    public List<String> getTragers() {
        List<String> retu = new ArrayList<>();
        retu.add(Material.ENCHANTED_BOOK.name());
        retu.add(Material.MACE.name());
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Оглушающий удар");
        defaults.put("biomes", List.of("FROZEN_PEAKS"));
        defaults.put("chance", 0.08);
        defaults.put("luck", 0.4);
        defaults.put("maxlvl", 3);
        defaults.put("conflicts", List.of("myolner"));

        Map<String, Object> bosses = new LinkedHashMap<>();
        bosses.put(Bosses.Rarity.EPIC.getName(), 10);

        defaults.put("bosses", bosses);
        return defaults;
    }

    @Override
    public NamespacedKey getId() {
        return this.id;
    }
}
