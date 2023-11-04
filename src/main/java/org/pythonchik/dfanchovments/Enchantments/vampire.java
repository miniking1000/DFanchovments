package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.pythonchik.dfanchovments.CEnchantment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.List;

public class vampire extends CEnchantment implements Listener {

    DFanchovments plugin = (DFanchovments) Bukkit.getPluginManager().getPlugin("DFanchovments");


    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){
        if (!(event.getDamager() instanceof Player)){
            return;
        }
        Player player = (Player) event.getDamager();
        if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getItemMeta() != null) {
            if (player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(DFanchovments.vampire)) {
                double hp = player.getHealth() + player.getInventory().getItemInMainHand().getItemMeta().getEnchants().get(DFanchovments.vampire).doubleValue();
                if (Math.random() <= player.getInventory().getItemInMainHand().getItemMeta().getEnchants().get(DFanchovments.vampire).doubleValue()*0.10) {
                    if (hp > player.getMaxHealth())
                        hp = player.getMaxHealth();
                    player.setHealth(hp);
                    player.getWorld().spawnParticle(Particle.HEART, event.getEntity().getLocation(), 5,1,1,1);
                }
            }
        }
    }

    public vampire(NamespacedKey id) {
        super(id);
    }

    public NamespacedKey getId(){
        return new NamespacedKey(plugin,"vampire");
    }
    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("WOODEN_SWORD");
        retu.add("STONE_SWORD");
        retu.add("IRON_SWORD");
        retu.add("DIAMOND_SWORD");
        retu.add("GOLDEN_SWORD");
        retu.add("NETHERITE_SWORD");
        return retu;
    }
    @Override
    public String getName() {
        return DFanchovments.getConfig1().getString("vampire.name");
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
