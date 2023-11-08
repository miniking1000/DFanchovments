package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.List;

public class grib extends CEnchantment implements Listener {

    DFanchovments plugin = (DFanchovments) Bukkit.getPluginManager().getPlugin("DFanchovments");
    @EventHandler
    public void onProjectile(ProjectileHitEvent event){
        if (event.getEntity().getShooter() instanceof Player){
            if (event.getHitBlock() != null) {
                Player player = (Player) event.getEntity().getShooter();
                if (player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(DFanchovments.grib)) {
                    if (Math.random() <= 0.001) { //0.1%
                        Location location = event.getHitBlock().getLocation();
                        location.add(0,1,0).getBlock().setType(Material.MUSHROOM_STEM);
                        location.add(0,1,0).getBlock().setType(Material.MUSHROOM_STEM);
                        location.add(0,1,0).getBlock().setType(Material.MUSHROOM_STEM);
                        location.add(0,1,0).getBlock().setType(Material.MUSHROOM_STEM);
                        location.add(0,1,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(-3,0,-3).getBlock().setType(Material.AIR);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.AIR);
                        location.add(-6,0,1).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(-6,0,1).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(-6,0,1).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(-6,0,1).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(-6,0,1).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(-6,0,1).getBlock().setType(Material.AIR);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.BROWN_MUSHROOM_BLOCK);
                        location.add(1,0,0).getBlock().setType(Material.AIR);
                    }
                }
            }
        }
    }

    public grib(NamespacedKey id) {
        super(id);
    }

    public NamespacedKey getId(){
        return new NamespacedKey(plugin,"grib");
    }
    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("BOW");
        retu.add("ENCHANTED_BOOK");
        return retu;
    }
    @Override
    public String getName() {
        return DFanchovments.getConfig1().getString("grib.name");
    }

    @Override
    public int getMaxLevel() {
        return 1;
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
