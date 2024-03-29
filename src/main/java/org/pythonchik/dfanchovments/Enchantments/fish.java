package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.jetbrains.annotations.NotNull;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.List;

public class fish extends CEnchantment implements Listener {

    DFanchovments plugin = (DFanchovments) Bukkit.getPluginManager().getPlugin("DFanchovments");

    public fish(NamespacedKey id) {
        super(id);
    }
    @EventHandler
    public void CrossEvents(ProjectileLaunchEvent event){
        if (event.getEntity().getShooter() instanceof Player){
            Player player = (Player) event.getEntity().getShooter();
            if (player.getInventory().getItemInMainHand().containsEnchantment(DFanchovments.fish)) {
                if (player.getInventory().getItemInOffHand().getType().equals(Material.AXOLOTL_BUCKET)){
                    AxolotlBucketMeta meta = (AxolotlBucketMeta) player.getInventory().getItemInOffHand().getItemMeta();
                    player.getInventory().setItemInOffHand(new ItemStack(Material.WATER_BUCKET));
                    Axolotl entity = (Axolotl) player.getLocation().getWorld().spawnEntity(player.getLocation().add(0,1,0),EntityType.AXOLOTL);
                    entity.setVelocity(event.getEntity().getVelocity());
                    entity.setVariant(meta.getVariant());
                    event.getEntity().remove();
                } else if (player.getInventory().getItemInOffHand().getType().equals(Material.PUFFERFISH_BUCKET)) {
                    player.getInventory().setItemInOffHand(new ItemStack(Material.WATER_BUCKET));
                    player.getLocation().getWorld().spawnEntity(player.getLocation().add(0,1,0), EntityType.PUFFERFISH).setVelocity(event.getEntity().getVelocity());
                    event.getEntity().remove();
                } else if (player.getInventory().getItemInOffHand().getType().equals(Material.SALMON_BUCKET)) {
                    player.getInventory().setItemInOffHand(new ItemStack(Material.WATER_BUCKET));
                    player.getLocation().getWorld().spawnEntity(player.getLocation().add(0,1,0), EntityType.SALMON).setVelocity(event.getEntity().getVelocity());
                    event.getEntity().remove();
                }
            }
        }
    }

    public NamespacedKey getId(){
        return new NamespacedKey(plugin,"fish");
    }
    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("CROSSBOW");
        return retu;
    }
    @Override
    public String getName() {
        return DFanchovments.getConfig1().getString("fish.name");
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
