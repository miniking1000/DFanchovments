package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.dfanchovments.CEnchantment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.List;

public class rain extends CEnchantment implements Listener {

    DFanchovments plugin = (DFanchovments) Bukkit.getPluginManager().getPlugin("DFanchovments");

    public rain(NamespacedKey id) {
        super(id);
    }


    @EventHandler
    public void onArrowHit(ProjectileHitEvent event){
        if (event.getHitEntity() == null) {
            return;
        }
        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }
        if (((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta() == null) {
            return;
        }
        if ((event.getEntity().getPersistentDataContainer().has(new NamespacedKey(plugin,"rained"),PersistentDataType.BOOLEAN))) {
            return;
        }
        if (((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta().hasEnchant(this)) {
            if (Math.random() <= ((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta().getEnchants().get(this).intValue()*0.06) {
                EntityType arrow = EntityType.ARROW;
                for (int i=0;i<((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta().getEnchants().get(this).intValue();i++) {
                    event.getHitEntity().getLocation().getWorld().spawnEntity(event.getHitEntity().getLocation().add(Math.random()*1-0.5,30-4*((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta().getEnchants().get(this).intValue(),Math.random()*1-0.5),arrow).setVelocity(new Vector(0,-2.5*((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta().getEnchants().get(this).intValue(),0));
                }
                event.getHitEntity().getLocation().getWorld().spawnEntity(event.getHitEntity().getLocation().add(0,5+5*((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta().getEnchants().get(this).intValue(),0),arrow);
                event.getEntity().getPersistentDataContainer().set(new NamespacedKey(plugin,"rained"), PersistentDataType.BOOLEAN,true);
            }
        }
    }

    public NamespacedKey getId(){
        return new NamespacedKey(plugin,"rain");
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("BOW");
        return retu;
    }
    @Override
    public String getName() {
        return DFanchovments.getConfig1().getString("rain.name");
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
    public boolean conflictsWith(@NotNull Enchantment other) {
        return false;
    }

    @Override
    public boolean conflictsWith(CEnchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return false;

    }
}
