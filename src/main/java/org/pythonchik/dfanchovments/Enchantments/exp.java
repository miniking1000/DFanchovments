package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.Util;
import org.pythonchik.dfanchovments.XP;

import java.util.ArrayList;
import java.util.List;

public class exp  extends CEnchantment implements Listener {
    @EventHandler
    public void ondKillEvent(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) {
            return;
        }
        Player player = event.getEntity().getKiller();
        if (player.getInventory().getItemInMainHand().getItemMeta() == null) {
            return;
        }
        if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(id)) {
            int a = XP.getTotalExperience(player) + (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(id, PersistentDataType.INTEGER)*15);
            XP.setTotalExperience(player, a);
        }

    }

    public exp(NamespacedKey id) {
        super(id);
    }
    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.addAll(Util.swords());
        return retu;
    }
    @Override
    public java.util.Map<String, Object> getDefaultConfig() {
        java.util.Map<String, Object> defaults = new java.util.LinkedHashMap<>();
        defaults.put("name", "&7Образование");
        defaults.put("biome", "THE_VOID");
        defaults.put("chance", 0);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 5);
        return defaults;
    }

    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
