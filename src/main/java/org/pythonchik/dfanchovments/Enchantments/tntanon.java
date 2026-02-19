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

import java.util.*;

public class tntanon extends CEnchantment implements Listener {
    public tntanon(NamespacedKey id) {
        super(id);
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
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Взрывной заряд");
        defaults.put("biomes", List.of("THE_VOID"));
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

