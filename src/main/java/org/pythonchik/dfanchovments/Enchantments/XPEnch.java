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
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.joml.Random;
import org.pythonchik.dfanchovments.DFanchovments;
import org.pythonchik.dfanchovments.XP;

import java.util.ArrayList;
import java.util.List;

public class XPEnch extends Enchantment implements Listener {

    DFanchovments plugin = (DFanchovments) Bukkit.getPluginManager().getPlugin("DFanchovments");
    XP xpclass = new XP();
    @EventHandler
    public void onBitchEvent(EntityDeathEvent event){

        if (event.getEntity().getKiller() == null){
            return;
        }
        Player player = event.getEntity().getKiller();
        //if (!player.getInventory().getItemInMainHand().equals(new ItemStack(Material.AIR)) && player.getInventory().getItemInMainHand().getItemMeta() != null) {
            if (player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(this)) {
                int a = xpclass.getTotalExperience(player) + (player.getInventory().getItemInMainHand().getItemMeta().getEnchants().get(this).intValue()*2);
                xpclass.setTotalExperience(player, a);
            }
        //}
    }

    public XPEnch(NamespacedKey id) {
        super(id);
    }

    public NamespacedKey getId(){
        return new NamespacedKey(plugin,"XPEnch");
    }

    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("wooden_sword");
        retu.add("stone_sword");
        retu.add("iron_sword");
        retu.add("diamond_sword");
        retu.add("golden_sword");
        retu.add("netherite_sword");
        return retu;
    }
    @Override
    public String getName() {
        return "Образование";
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
