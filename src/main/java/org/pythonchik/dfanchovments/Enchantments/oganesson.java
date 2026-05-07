package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.Util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class oganesson extends CEnchantment implements Listener {
    public oganesson(NamespacedKey id) {
        super(id);
    }

    @EventHandler(ignoreCancelled = true)
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getItemMeta() == null) return;
        int cd = player.getCooldown(item.getType());
        if (cd >= 1) {
            event.setCancelled(true);
            return;
        }
        ItemMeta meta = item.getItemMeta();

        Integer level = meta.getPersistentDataContainer().get(this.id, PersistentDataType.INTEGER);

        if (level == null || level <= 0) return;

        double failChance = (100.0 - (10.0 * level)) / 100.0;
        failChance = Math.max(0.0, Math.min(1.0, failChance));

        if (Math.random() < failChance) {
            player.setCooldown(item.getType(), 20 * 3); // 3 seconds
            event.setCancelled(true);
            return;
        }

        double multiplier = 0.5 + (0.5 * level);
        event.setDamage(event.getDamage() * multiplier);
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.addAll(Util.swords());
        return retu;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Нестабильность");
        defaults.put("biomes", List.of("THE_VOID"));
        defaults.put("chance", 0);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 5);
        return defaults;
    }
}
