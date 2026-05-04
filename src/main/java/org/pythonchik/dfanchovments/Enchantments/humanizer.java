package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
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
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.addAll(Util.helmets());
        return retu;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Человечность");
        defaults.put("biomes", List.of("THE_VOID"));
        defaults.put("chance", 0.5);
        defaults.put("luck", 0.05);
        defaults.put("maxlvl", 1);
        return defaults;
    }
}
