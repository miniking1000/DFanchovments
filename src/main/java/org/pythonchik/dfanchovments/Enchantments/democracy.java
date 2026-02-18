package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class democracy extends CEnchantment implements Listener {
    public democracy(NamespacedKey id) {
        super(id);
    }
    @Override
    public List<String> getTragers() {
        List<String> retu = new ArrayList<>();
        return retu;
    }
    @Override
    public java.util.Map<String, Object> getDefaultConfig() {
        java.util.Map<String, Object> defaults = new java.util.LinkedHashMap<>();
        defaults.put("name", "&7Нормальность");
        defaults.put("biome", "PLAINS");
        defaults.put("chance", 100);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 9999999);
        return defaults;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
    //FOR future me, this book adds .5 to height using attributes, that's why nothing is here

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
