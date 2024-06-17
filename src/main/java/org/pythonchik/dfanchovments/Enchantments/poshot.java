package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.List;

public class poshot extends CEnchantment implements Listener {
    NamespacedKey id;

    public poshot(NamespacedKey id) {
        this.id = id;
    }

    @EventHandler
    public void CrossEvents(ProjectileLaunchEvent event){
        if (event.getEntity().getShooter() instanceof Player){
            Player player = (Player) event.getEntity().getShooter();
            if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
                if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(id)) {
                    if (player.getInventory().getItemInOffHand().getType().equals(Material.SPLASH_POTION) || player.getInventory().getItemInOffHand().getType().equals(Material.LINGERING_POTION)) {
                        ThrownPotion potion = (ThrownPotion) player.getLocation().getWorld().spawnEntity(player.getLocation().add(0, 1, 0), EntityType.POTION);
                        potion.setItem(player.getInventory().getItemInOffHand());
                        potion.setVelocity(event.getEntity().getVelocity());
                        player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() - 1);
                        event.getEntity().teleport(new Location(event.getEntity().getWorld(),0,-9999,0));
                    }
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
