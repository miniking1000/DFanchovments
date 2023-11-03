package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.List;

public class bamboom extends CEnchantment implements Listener {

    DFanchovments plugin = (DFanchovments) Bukkit.getPluginManager().getPlugin("DFanchovments");
    public bamboom(NamespacedKey id) {
        super(id);
    }
    public NamespacedKey getId(){
        return new NamespacedKey(plugin,"bamboom");
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
            if (player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(DFanchovments.bamboom)){
                if (Math.random() <= player.getInventory().getItemInMainHand().getItemMeta().getEnchants().get(DFanchovments.bamboom).doubleValue()*0.05) {
                    event.getEntity().getLocation().getWorld().spawnParticle(Particle.SONIC_BOOM, event.getEntity().getLocation(), 1);
                    ((Arrow) event.getEntity()).setVelocity(event.getEntity().getVelocity().multiply(1 + (0.5 * player.getInventory().getItemInMainHand().getEnchantmentLevel(DFanchovments.bamboom))));
                }
            }
        }
    }
    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("CROSSBOW");
        return retu;
    }
    @Override
    public String getName() {
        return DFanchovments.getConfig1().getString("bamboom.name");
    }

    @Override
    public int getMaxLevel() {
        return 3;
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
