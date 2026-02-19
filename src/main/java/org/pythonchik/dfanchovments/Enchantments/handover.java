package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.*;

public class handover extends CEnchantment {
    public handover(NamespacedKey id) {
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
        defaults.put("name", "&7Длиннорук");
        defaults.put("biomes", List.of("lush_caves"));
        defaults.put("chance", 0.5);
        defaults.put("luck", 0.1);
        defaults.put("maxlvl", 3);
        return defaults;
    }

    @Override
    public List<EnchantmentAttribute> getAttributeEnchantments() {
        return List.of(
            new EnchantmentAttribute(Attribute.BLOCK_INTERACTION_RANGE, 0.5D, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.OFFHAND)
        );
    }

    @Override
    public NamespacedKey getId(){
        return this.id;
    }

}
