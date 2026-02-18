package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.List;

public class dodge extends CEnchantment implements Listener {
    @EventHandler
    public void DbtxHxRvieptrLyoLwdzVpjTdXTYTVTYR(EntityDamageByEntityEvent event){ //this name is not random btw
        if(!(event.getEntity() instanceof Player)){
            return;
        }
        if(((Player) event.getEntity()).getInventory().getChestplate() != null && ((Player) event.getEntity()).getInventory().getChestplate().getItemMeta() != null && ((Player) event.getEntity()).getInventory().getChestplate().getItemMeta().getPersistentDataContainer().has(id)){
            if (Math.random() <0.15) {
                event.setDamage(0);
            }
        }
    }

    public dodge(NamespacedKey id) {
        super(id);
    }
    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        //chestplates
        retu.add("LEATHER_CHESTPLATE");
        retu.add("CHAINMAIL_CHESTPLATE");
        retu.add("IRON_CHESTPLATE");
        retu.add("DIAMOND_CHESTPLATE");
        retu.add("GOLDEN_CHESTPLATE");
        retu.add("NETHERITE_CHESTPLATE");
        return retu;
    }
    @Override
    public java.util.Map<String, Object> getDefaultConfig() {
        java.util.Map<String, Object> defaults = new java.util.LinkedHashMap<>();
        defaults.put("name", "&7Уклонение");
        defaults.put("biome", "THE_VOID");
        defaults.put("chance", 0);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 1);
        return defaults;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
