package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.List;

public class Arrain extends Enchantment implements Listener {

    DFanchovments plugin = (DFanchovments) Bukkit.getPluginManager().getPlugin("DFanchovments");

    public Arrain(NamespacedKey id) {
        super(id);
    }


    @EventHandler
    public void onArrowHit(ProjectileHitEvent event){
        if (event.getHitEntity() == null){
            return;
        }
        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }
        if (((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta().hasEnchant(DFanchovments.ARrain)) {
            if (Math.random() <= ((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta().getEnchants().get(this).intValue()*1){
                EntityType arrow = EntityType.ARROW;
                for (int i=0;i<((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta().getEnchants().get(this).intValue()*3;i++){
                    event.getHitEntity().getLocation().getWorld().spawnEntity(event.getHitEntity().getLocation().add(Math.random()*3-1.5,30-5*((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta().getEnchants().get(this).intValue(),Math.random()*3-1.5),arrow).setVelocity(new Vector(0,-2*((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta().getEnchants().get(this).intValue(),0));
                }
                event.getHitEntity().getLocation().getWorld().spawnEntity(event.getHitEntity().getLocation().add(0,5+5*((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta().getEnchants().get(this).intValue(),0),arrow);
            }
        }
    }

    public NamespacedKey getId(){
        return new NamespacedKey(plugin,"ARrain");
    }

    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("bow");
        return retu;
    }
    @Override
    public String getName() {
        return "ARrain";
    }

    @Override
    public int getMaxLevel() {
        return 5;
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
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return false;

    }
}
