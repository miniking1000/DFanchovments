package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.List;

public class ToCopyEnch extends Enchantment implements Listener {

    DFanchovments plugin = (DFanchovments) Bukkit.getPluginManager().getPlugin("DFanchovments");

    public ToCopyEnch(NamespacedKey id) {
        super(id);
    }

    public NamespacedKey getId(){
        return new NamespacedKey(plugin,"name");
    }

    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("bedrock");
        return retu;
    }
    @Override
    public String getName() {
        return "name";
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
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return false;

    }
}
