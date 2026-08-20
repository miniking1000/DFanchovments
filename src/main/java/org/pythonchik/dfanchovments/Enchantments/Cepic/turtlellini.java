package org.pythonchik.dfanchovments.Enchantments.Cepic;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityKnockbackEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.pythonchik.dfanchovments.Bosses;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.Util;

import java.util.*;

public class turtlellini extends CEnchantment implements Listener {
    public turtlellini(NamespacedKey id) {
        super(id);
    }

    @EventHandler
    public void onKnockforward(EntityKnockbackEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getFinalKnockback().isZero()) return;
        ItemStack leggings = player.getInventory().getLeggings();
        if (leggings == null || leggings.getType().isAir()) return;

        ItemMeta meta = leggings.getItemMeta();
        if (meta == null) return;
        if (!meta.getPersistentDataContainer().has(this.getId(), PersistentDataType.INTEGER)) return;
        int level = meta.getPersistentDataContainer().getOrDefault(this.getId(), PersistentDataType.INTEGER, 1);

        Vector knockback = event.getFinalKnockback();
        double length = knockback.length();

        double multiplier = 2.0 - (0.25 * level);
        double damage = Math.max(0.0, length * multiplier);
        if (damage > 0.0) {
            player.damage(damage);
        }
        event.setFinalKnockback(new Vector(0,0,0));

    }

    @Override
    public List<String> getTragers() {
        List<String> retu = new ArrayList<>();
        retu.add(Material.ENCHANTED_BOOK.name());
        retu.addAll(Util.leggings());
        return retu;
    }


    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Крепкость");
        defaults.put("biomes", List.of("WINDSWEPT_FOREST"));
        defaults.put("chance", 0.06);
        defaults.put("luck", 0.08347824);
        defaults.put("maxlvl", 4);

        Map<String, Object> bosses = new LinkedHashMap<>();
        bosses.put(Bosses.Rarity.EPIC.getName(), 10);

        defaults.put("bosses", bosses);
        return defaults;
    }
}
