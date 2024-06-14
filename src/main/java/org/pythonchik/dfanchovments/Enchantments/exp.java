package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.pythonchik.dfanchovments.CEnchantment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.pythonchik.dfanchovments.DFanchovments;
import org.pythonchik.dfanchovments.XP;

import java.util.ArrayList;
import java.util.List;

public class exp extends CEnchantment implements Listener {

    DFanchovments plugin = (DFanchovments) Bukkit.getPluginManager().getPlugin("DFanchovments");

    @EventHandler
    public void ondKillEvent(EntityDeathEvent event){

        if (event.getEntity().getKiller() == null){
            return;
        }
        Player player = event.getEntity().getKiller();
        if (player.getInventory().getItemInMainHand().getItemMeta() == null){
            return;
        }
        if (player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(this)) {
            int a = XP.getTotalExperience(player) + (player.getInventory().getItemInMainHand().getItemMeta().getEnchants().get(this)*2);
            XP.setTotalExperience(player, a);
        }

    }

    public exp(NamespacedKey id) {
        super(id);
    }

    public NamespacedKey getId(){
        return new NamespacedKey(plugin,"exp");
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
        return DFanchovments.getConfig1().getString("exp.name");
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

    @Override
    public String getTranslationKey() {
        return null;
    }
}
