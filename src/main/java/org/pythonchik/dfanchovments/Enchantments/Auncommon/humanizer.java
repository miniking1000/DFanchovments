package org.pythonchik.dfanchovments.Enchantments.Auncommon;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.pythonchik.dfanchovments.Bosses;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.Util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class humanizer extends CEnchantment {
    // empty because just having it in one of the hands or armor slots makes you 'human' in TrueYandere
    public humanizer(NamespacedKey id) {
        super(id);
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
        defaults.put("name", "&7Человечность");
        defaults.put("biomes", List.of("THE_VOID"));
        defaults.put("chance", 0.5);
        defaults.put("luck", 0.05);
        defaults.put("maxlvl", 1);

        Map<String, Object> bosses = new LinkedHashMap<>();
        bosses.put(Bosses.Rarity.UNCOMMON.getName(), 10);

        defaults.put("bosses", bosses);
        return defaults;
    }
}
