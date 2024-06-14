package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
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

public class dodge extends CEnchantment implements Listener {
    DFanchovments plugin = (DFanchovments) Bukkit.getPluginManager().getPlugin("DFanchovments");

    @EventHandler
    public void DbtxHxRvieptrLyoLwdzVpjTdXTYTVTYR(EntityDamageByEntityEvent event){ //this name is not random btw
        if(!(event.getEntity() instanceof Player)){
            return;
        }
        if(((Player) event.getEntity()).getInventory().getChestplate() != null && ((Player) event.getEntity()).getInventory().getChestplate().getItemMeta().hasEnchant(DFanchovments.dodge)){
            if (Math.random() <0.15) {
                event.setDamage(0);
            }
        }
    }

    public dodge(NamespacedKey id) {
        super(id);
    }

    public NamespacedKey getId(){
        return new NamespacedKey(plugin,"dodge");
    }
    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        //chestplates
        retu.add("LEATHER_CHESTPLATE");
        retu.add("CHAINMAIL_CHESTPLATE");
        retu.add("IRON_CHESTPLATE");
        retu.add("DIAMOND_CHESTPLATE");
        retu.add("GOLDEN_CHESTPLATE");
        retu.add("NETHERITE_CHESTPLATE");
        return retu;
    }
    @Override
    public String getName() {
        return DFanchovments.getConfig1().getString("dodge.name");
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

    @NotNull
    @Override
    public String getTranslationKey() {
        return null;
    }
}
