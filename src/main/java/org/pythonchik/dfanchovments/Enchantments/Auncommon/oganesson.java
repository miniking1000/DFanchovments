package org.pythonchik.dfanchovments.Enchantments.Auncommon;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.dfanchovments.Bosses;
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
        ItemStack item;
        if (player.getItemInUse() != null) {
            item = player.getItemInUse();
        } else {
            item = player.getInventory().getItemInMainHand();
        }
        if (item.getItemMeta() == null) return;

        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK
                && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            return;
        }

        ItemMeta meta = item.getItemMeta();

        Integer level = meta.getPersistentDataContainer().get(this.id, PersistentDataType.INTEGER);

        if (level == null || level <= 0) return;

        double successChance = Math.min(0.95, 0.60 + 0.035 * level);
        double multiplier;
        if (Math.random() < successChance) {
            multiplier = 1.35 + 0.055 * level;
        } else {
            multiplier = 0.5 / (1.0 + 0.23 * level);
        }
        /*
      level | avg buff  | chance to hit | damage multipliers
        1) avg buff: 1.04, chance=0.64,   success=1.41, fail=0.41
        2) avg buff: 1.09, chance=0.67,   success=1.46, fail=0.34
        3) avg buff: 1.16, chance=0.70,   success=1.52, fail=0.30
        4) avg buff: 1.23, chance=0.74,   success=1.57, fail=0.26
        4) avg buff: 1.31, chance=0.78,   success=1.62, fail=0.23
         */

        event.setDamage(event.getDamage() * multiplier);
    }

    @Override
    public List<String> getTragers() {
        List<String> retu = new ArrayList<>();
        retu.add(Material.ENCHANTED_BOOK.name());
        retu.addAll(Util.swords());
        return retu;
    }

    @Override
    public NamespacedKey getId() {
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

        Map<String, Object> bosses = new LinkedHashMap<>();
        bosses.put(Bosses.Rarity.UNCOMMON.getName(), 10);

        defaults.put("bosses", bosses);
        return defaults;
    }
}
