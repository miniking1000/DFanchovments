package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.Util;

import java.util.ArrayList;
import java.util.List;

public class potioness extends CEnchantment implements Listener {

    NamespacedKey id;

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){
        if (!(event.getEntity() instanceof Player player)){
            return;
        }
        if (!(event.getDamager() instanceof LivingEntity entity)){
            return;
        }
        int lvl = 0;
        if (player.getInventory().getHelmet() != null){
            if (player.getInventory().getHelmet().getItemMeta() != null){
                if (player.getInventory().getHelmet().getItemMeta().getPersistentDataContainer().has(id)){
                    lvl = lvl +player.getInventory().getHelmet().getItemMeta().getPersistentDataContainer().get(id, PersistentDataType.INTEGER);
                }
            }
        }
        if (player.getInventory().getChestplate() != null){
            if (player.getInventory().getChestplate().getItemMeta() != null) {
                if (player.getInventory().getChestplate().getItemMeta().getPersistentDataContainer().has(id)) {
                    lvl = lvl + player.getInventory().getChestplate().getItemMeta().getPersistentDataContainer().get(id, PersistentDataType.INTEGER);
                }
            }
        }
        if (player.getInventory().getLeggings() != null){
            if (player.getInventory().getLeggings().getItemMeta() != null) {
                if (player.getInventory().getLeggings().getItemMeta().getPersistentDataContainer().has(id)) {
                    lvl = lvl + player.getInventory().getLeggings().getItemMeta().getPersistentDataContainer().get(id, PersistentDataType.INTEGER);
                }
            }
        }
        if (player.getInventory().getBoots() != null){
            if (player.getInventory().getBoots().getItemMeta() != null) {
                if (player.getInventory().getBoots().getItemMeta().getPersistentDataContainer().has(id)) {
                    lvl = lvl + player.getInventory().getBoots().getItemMeta().getPersistentDataContainer().get(id, PersistentDataType.INTEGER);
                }
            }
        }
        if (lvl >0) {
            entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON,20*lvl,lvl==1?0:lvl==2||lvl==3||lvl==4? 1 : lvl==5||lvl==6||lvl==7? 2 : lvl==8||lvl==9||lvl==10||lvl==11? 3 : lvl==12||lvl==13||lvl==14||lvl==15 ? 4 : 5));
        }
    }

    public potioness(NamespacedKey id) {
        this.id = id;
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");

        retu.addAll(Util.armors());
        return retu;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
