package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.List;

public class leviosa extends CEnchantment implements Listener {

    DFanchovments plugin = (DFanchovments) Bukkit.getPluginManager().getPlugin("DFanchovments");

    public leviosa(NamespacedKey id) {
        super(id);
    }

    public NamespacedKey getId(){
        return new NamespacedKey(plugin,"leviosa");
    }

    /*
    @EventHandler
    public void onTridenHit(ProjectileHitEvent event){
        if (!(event.getEntity() instanceof Trident)){
            return;
        }

        if (!(event.getEntity().getPersistentDataContainer().has(new NamespacedKey(plugin,"leviosa"),PersistentDataType.BOOLEAN))){
            return;
        }
        event.getEntity().getPersistentDataContainer().remove(new NamespacedKey(plugin,"leviosa"));
        if (!(event.getEntity().getShooter() instanceof Player)){
            return;
        }

        Player player = (Player) event.getEntity().getShooter();
        if (event.getHitEntity() != null && event.getHitEntity() instanceof Player) {
            for (Entity entity : event.getHitEntity().getNearbyEntities(2 * (player.getInventory().getItemInMainHand().getEnchantmentLevel(DFanchovments.leviosa)), 2 * (player.getInventory().getItemInMainHand().getEnchantmentLevel(DFanchovments.leviosa)), 2 * (player.getInventory().getItemInMainHand().getEnchantmentLevel(DFanchovments.leviosa)))){
                if (entity instanceof Player) {
                    ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 150 * player.getInventory().getItemInMainHand().getEnchantmentLevel(DFanchovments.leviosa), player.getInventory().getItemInMainHand().getEnchantmentLevel(DFanchovments.leviosa)));
                }
            }

            ((Player) event.getHitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 150 *(player.getInventory().getItemInMainHand().getEnchantmentLevel(DFanchovments.leviosa)+1), player.getInventory().getItemInMainHand().getEnchantmentLevel(DFanchovments.leviosa)+1));
        }
    }
    @EventHandler
    public void onTridentShoot(ProjectileLaunchEvent event){
        if (!(event.getEntity() instanceof Trident)){
            return;
        }
        if (!(event.getEntity().getShooter() instanceof Player)){
            return;
        }
        Player player = (Player) event.getEntity().getShooter();
        if (player.getInventory().getItemInMainHand().getItemMeta() != null){
            if (player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(DFanchovments.leviosa)) {
                event.getEntity().getPersistentDataContainer().set(new NamespacedKey(plugin,"leviosa"), PersistentDataType.BOOLEAN,true);
            }
        }
    }

    */

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("TRIDENT");
        return retu;
    }
    @Override
    public String getName() {
        return DFanchovments.getConfig1().getString("leviosa.name");
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(@NotNull CEnchantment other) {
        return false;
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return false;

    }
}
