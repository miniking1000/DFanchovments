package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.List;

public class antiholy extends CEnchantment implements Listener {
    NamespacedKey id;
    ArrayList<PotionEffectType> badeffects = new ArrayList<>();
    public antiholy(NamespacedKey id) {
        this.id = id;
        badeffects.add(PotionEffectType.SLOWNESS);
        badeffects.add(PotionEffectType.MINING_FATIGUE);
        badeffects.add(PotionEffectType.INSTANT_DAMAGE);
        badeffects.add(PotionEffectType.NAUSEA);
        badeffects.add(PotionEffectType.BLINDNESS);
        badeffects.add(PotionEffectType.HUNGER);
        badeffects.add(PotionEffectType.WEAKNESS);
        badeffects.add(PotionEffectType.POISON);
        badeffects.add(PotionEffectType.LEVITATION);
        badeffects.add(PotionEffectType.UNLUCK);
        badeffects.add(PotionEffectType.WEAVING);
        badeffects.add(PotionEffectType.WIND_CHARGED);
        badeffects.add(PotionEffectType.INFESTED);
        badeffects.add(PotionEffectType.OOZING);
        badeffects.add(PotionEffectType.WITHER);
        badeffects.add(PotionEffectType.DARKNESS);

    }
    @EventHandler
    public void event(EntityPotionEffectEvent event){
        if(!(event.getEntity() instanceof Player)){
            return;
        }

        if(((Player) event.getEntity()).getInventory().getChestplate() != null && ((Player) event.getEntity()).getInventory().getChestplate().getItemMeta().getPersistentDataContainer().has(id)){
            if (badeffects.contains(event.getModifiedType())){
                event.setCancelled(true);
            }
        }

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
    public NamespacedKey getId(){
        return this.id;
    }
}
