package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Location;
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
    NamespacedKey id;

    public steveoshot(NamespacedKey id) {
        this.id = id;
    }

    @EventHandler
    public void onCrossShoot(ProjectileLaunchEvent event){
        if (!(event.getEntity().getShooter() instanceof Player)){
            return;
        }
        Player player = (Player) event.getEntity().getShooter();
        if (player.getInventory().getItemInMainHand().getItemMeta() != null){
            if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(id)){
                Entity guy = sendFlying(player.getPassengers(),null);
                if (guy != null) {
                    guy.getVehicle().eject();
                    guy.setVelocity(event.getEntity().getVelocity().multiply(2));
                    event.getEntity().teleport(new Location(event.getEntity().getWorld(), 0, -9999, 0));
                }
            }
        }
    }

    private Entity sendFlying(List<Entity> list, Entity result){
        for (Entity entity : list){
            if (entity instanceof Player){
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
    public NamespacedKey getId(){
        return this.id;
    }
}
