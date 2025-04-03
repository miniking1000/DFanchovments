package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.List;

public class neverended extends CEnchantment implements Listener {
    NamespacedKey id;
    public neverended(NamespacedKey id) {
        this.id = id;
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent event){
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getItemMeta() != null && event.getItem().equals(player.getInventory().getItemInMainHand())) {
            if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(id)) {
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() + 1);
                return;
            }
        }
        if (player.getInventory().getItemInOffHand().getItemMeta() != null && event.getItem().equals(player.getInventory().getItemInOffHand())) {
            if (player.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(id)) {
                player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() + 1);
            }
        }
    }

    @EventHandler
    public void onDeath(EntityResurrectEvent event){
        if (!(event.getEntity() instanceof Player player)){
            return;
        }
        if (player.getInventory().getItemInOffHand().getItemMeta() != null && player.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING)) {
            if (player.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(id)) {
                player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() + 1);
                return;
            }
        }

        if (player.getInventory().getItemInMainHand().getItemMeta() != null && player.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING)) {
            if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(id)) {
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() + 1);
            }
        }

    }

    @EventHandler
    public void onThrow(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player player)){
            return;
        }
        if (!(event.getEntity().getType().equals(EntityType.WIND_CHARGE))) {
            return;
        }

        if (player.getInventory().getItemInOffHand().getItemMeta() != null && player.getInventory().getItemInOffHand().getType().equals(Material.WIND_CHARGE) && !player.getInventory().getItemInMainHand().getType().equals(Material.WIND_CHARGE)) {
            if (player.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(id)) {
                player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() + 1);
                player.updateInventory();
                return;
            }
        }

        if (player.getInventory().getItemInMainHand().getItemMeta() != null && player.getInventory().getItemInMainHand().getType().equals(Material.WIND_CHARGE)) {
            if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(id)) {
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() + 1);
                player.updateInventory();
            }
        }
    }



    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("ROTTEN_FLESH");
        retu.add("ENCHANTED_GOLDEN_APPLE");
        retu.add("TOTEM_OF_UNDYING");
        retu.add("CHORUS_FLOWER");
        return retu;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
