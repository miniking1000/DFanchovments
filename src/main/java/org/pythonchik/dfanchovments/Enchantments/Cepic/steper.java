package org.pythonchik.dfanchovments.Enchantments.Cepic;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.pythonchik.dfanchovments.Bosses;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.Util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class steper extends CEnchantment {
    public steper(NamespacedKey id) {
        super(id);
    }

    @Override
    public List<EnchantmentAttribute> getAttributeEnchantments() {
        return List.of(new EnchantmentAttribute(Attribute.STEP_HEIGHT, 0.5D, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS));
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
        defaults.put("name", "&7Шагоход");
        defaults.put("biomes", List.of("MUSHROOM_FIELDS"));
        defaults.put("chance", 1.0);
        defaults.put("luck", 0.34);
        defaults.put("maxlvl", 2);

        Map<String, Object> bosses = new LinkedHashMap<>();
        bosses.put(Bosses.Rarity.RARE.getName(), 10);

        defaults.put("bosses", bosses);
        return defaults;
    }


}
