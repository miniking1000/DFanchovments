package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.pythonchik.dfanchovments.Bosses;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ToCopyEnch extends CEnchantment implements Listener {
    public ToCopyEnch(NamespacedKey id) {
        super(id);
    }

    @Override
    public List<String> getTragers() {
        List<String> retu = new ArrayList<>();
        retu.add(Material.ENCHANTED_BOOK.name());
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7name");
        defaults.put("biomes", List.of("THE_VOID"));
        defaults.put("chance", 0);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 1);

        Map<String, Object> bosses = new LinkedHashMap<>();
        bosses.put(Bosses.Rarity.COMMON.getName(), 0);
        bosses.put(Bosses.Rarity.UNCOMMON.getName(), 0);
        bosses.put(Bosses.Rarity.RARE.getName(), 0);
        bosses.put(Bosses.Rarity.EPIC.getName(), 0);
        bosses.put(Bosses.Rarity.LEGENDARY.getName(), 0);
        bosses.put(Bosses.Rarity.MYTHIC.getName(), 0);

        defaults.put("bosses", bosses);
        return defaults;
    }
}
