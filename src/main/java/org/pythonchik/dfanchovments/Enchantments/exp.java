package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.XP;

import java.util.ArrayList;
import java.util.List;

public class exp  extends CEnchantment implements Listener {

    NamespacedKey id;

    @EventHandler
    public void ondKillEvent(EntityDeathEvent event){

        if (event.getEntity().getKiller() == null){
            return;
        }
        Player player = event.getEntity().getKiller();
        if (player.getInventory().getItemInMainHand().getItemMeta() == null){
            return;
        }
        if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(id)) {
            int a = XP.getTotalExperience(player) + (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(id, PersistentDataType.INTEGER)*15);
            XP.setTotalExperience(player, a);
        }

    }

    public exp(NamespacedKey id) {
        this.id = id;
    }
    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("WOODEN_SWORD");
        retu.add("STONE_SWORD");
        retu.add("IRON_SWORD");
        retu.add("DIAMOND_SWORD");
        retu.add("GOLDEN_SWORD");
        retu.add("NETHERITE_SWORD");
        return retu;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
