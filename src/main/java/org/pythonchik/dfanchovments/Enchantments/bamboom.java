package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.List;

public class bamboom extends CEnchantment implements Listener {
    NamespacedKey id;
    public bamboom(NamespacedKey id) {
        this.id = id;
    }

    @EventHandler
    public void onCrossShoot(ProjectileLaunchEvent event){
        if (!(event.getEntity() instanceof Arrow)){
            return;
        }
        if (!(event.getEntity().getShooter() instanceof Player)){
            return;
        }
        Player player = (Player) event.getEntity().getShooter();
        if (player.getInventory().getItemInMainHand().getItemMeta() != null){
            ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
            assert meta != null;
            if (meta.getPersistentDataContainer().has(id)){
                if (Math.random() <= meta.getPersistentDataContainer().get(id, PersistentDataType.INTEGER)*0.05) {
                    ((Player) event.getEntity().getShooter()).getLocation().getWorld().spawnParticle(Particle.SONIC_BOOM, event.getEntity().getLocation(), 1);
                    ((Arrow) event.getEntity()).setVelocity(event.getEntity().getVelocity().multiply(1 + (0.5 * meta.getPersistentDataContainer().get(id, PersistentDataType.INTEGER))));
                }
            }
        }
    }
    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("CROSSBOW");
        return retu;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
