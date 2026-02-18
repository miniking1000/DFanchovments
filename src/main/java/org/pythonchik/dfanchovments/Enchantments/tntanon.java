package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.List;

public class tntanon extends CEnchantment implements Listener {
    NamespacedKey id;
    public tntanon(NamespacedKey id) {
        this.id = id;
    }

    @EventHandler
    public void CrossEvents(ProjectileLaunchEvent event){
        if (event.getEntity().getShooter() instanceof Player){
            Player player = (Player) event.getEntity().getShooter();
            if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
                if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(id)) {
                    if (player.getInventory().getItemInOffHand().getType().equals(Material.TNT)) {
                        player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() - 1);
                        player.getLocation().getWorld().spawnEntity(player.getLocation().add(0, 1, 0), EntityType.TNT).setVelocity(event.getEntity().getVelocity());
                        Bukkit.getScheduler().runTask(DFanchovments.plugin, event.getEntity()::remove);
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

