package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
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
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.*;

public class poshot extends CEnchantment implements Listener {
    public poshot(NamespacedKey id) {
        super(id);
    }

    @EventHandler
    public void CrossEvents(ProjectileLaunchEvent event){
        if (event.getEntity().getShooter() instanceof Player){
            Player player = (Player) event.getEntity().getShooter();
            if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
                if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(id)) {
                    if (player.getInventory().getItemInOffHand().getType().equals(Material.SPLASH_POTION) || player.getInventory().getItemInOffHand().getType().equals(Material.LINGERING_POTION)) {
                        ThrownPotion potion;
                        if (player.getInventory().getItemInOffHand().getType().equals(Material.SPLASH_POTION)) {
                            potion = (ThrownPotion) player.getLocation().getWorld().spawnEntity(player.getLocation().add(0, 1, 0), EntityType.SPLASH_POTION);
                        } else if (player.getInventory().getItemInOffHand().getType().equals(Material.LINGERING_POTION)) {
                            potion = (ThrownPotion) player.getLocation().getWorld().spawnEntity(player.getLocation().add(0, 1, 0), EntityType.LINGERING_POTION);
                        } else {
                            return; // never happens
                        }
                        potion.setItem(player.getInventory().getItemInOffHand());
                        potion.setVelocity(event.getEntity().getVelocity());
                        player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() - 1);
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
        defaults.put("name", "&7Зельестрел");
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
