package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.List;

public class godkiller extends CEnchantment implements Listener {

    DFanchovments plugin = (DFanchovments) Bukkit.getPluginManager().getPlugin("DFanchovments");

    @EventHandler
    public void onhit(EntityDamageByEntityEvent event){
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player){
            if (((Player) event.getDamager()).getInventory().getItemInMainHand().getType().equals(Material.HEARTBREAK_POTTERY_SHERD) && ((Player) event.getDamager()).getInventory().getItemInMainHand().getItemMeta() != null){
                if (((Player) event.getDamager()).getInventory().getItemInMainHand().getItemMeta().hasEnchant(DFanchovments.godkiller) && ((Player) event.getDamager()).getInventory().getItemInMainHand().getEnchantmentLevel(DFanchovments.godkiller)==1){
                    ((Player) event.getDamager()).getInventory().setItemInMainHand(new ItemStack(Material.AIR));

                    ((Player) event.getDamager()).damage(2147483647);
                    ((Player) event.getDamager()).setHealth(0);

                    ((Player) event.getEntity()).removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    ((Player) event.getEntity()).damage(2147483647);
                    ((Player) event.getEntity()).setHealth(0);
                }
            }
        }
    }

    public godkiller(NamespacedKey id) {
        super(id);
    }

    public NamespacedKey getId(){
        return new NamespacedKey(plugin,"godkiller");
    }
    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("HEARTBREAK_POTTERY_SHERD");
        return retu;
    }
    @Override
    public String getName() {
        return DFanchovments.getConfig1().getString("godkiller.name");
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
