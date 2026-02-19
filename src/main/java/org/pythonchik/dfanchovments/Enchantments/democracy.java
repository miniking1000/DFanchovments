package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.Util;

import java.util.*;

public class democracy extends CEnchantment implements Listener {
    public democracy(NamespacedKey id) {
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
        defaults.put("name", "&7Нормальность");
        defaults.put("biomes", List.of("THE_END"));
        defaults.put("chance", 30);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 3);
        return defaults;
    }

    @Override
    public List<EnchantmentAttribute> getAttributeEnchantments() {
        return List.of(new EnchantmentAttribute(Attribute.SCALE, 0.25D, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD));
    }

    @Override
    public NamespacedKey getId(){
        return this.id;
    }

    //this event handler is used NOT as part of this enchantment, but rather as a way to create 'upgradable' items
    @EventHandler
    public void onbreak(PlayerItemBreakEvent event) {
        ItemMeta meta = event.getBrokenItem().getItemMeta();
        if (meta != null && meta.getPersistentDataContainer().has(id)) {
            ItemStack replacement = meta.getUseRemainder();
            if (replacement != null && replacement.getType() != Material.AIR) {
                HashMap<Integer, ItemStack> leftover = event.getPlayer().getInventory().addItem(replacement);
                if (!leftover.isEmpty()) {
                    leftover.values().forEach((item) -> {
                        event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), item);
                    });
                }
            }
        }
    }
}
