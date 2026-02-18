package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.List;

public class steveoshot extends CEnchantment implements Listener {
    public steveoshot(NamespacedKey id) {
        super(id);
    }

    @EventHandler
    public void onCrossShoot(ProjectileLaunchEvent event){
        if (!(event.getEntity().getShooter() instanceof Player)){
            return;
        }
        Player player = (Player) event.getEntity().getShooter();
        if (player.getInventory().getItemInMainHand().getItemMeta() != null){
            if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(id)) {
                Entity guy = sendFlying(player.getPassengers(),null);
                if (guy != null) {
                    guy.getVehicle().eject();
                    guy.setVelocity(event.getEntity().getVelocity().multiply(2));
                    Bukkit.getScheduler().runTask(DFanchovments.plugin, event.getEntity()::remove);
                }
            }
        }
    }

    private Entity sendFlying(List<Entity> list, Entity result) {
        for (Entity entity : list){
            if (entity instanceof Player) {
                return entity;
            } else {
                result = sendFlying(entity.getPassengers(), result);
            }
        }
        return result;
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("BOW");
        return retu;
    }
    @Override
    public java.util.Map<String, Object> getDefaultConfig() {
        java.util.Map<String, Object> defaults = new java.util.LinkedHashMap<>();
        defaults.put("name", "&7Стиво-стрел");
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
