package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.pythonchik.dfanchovments.DFanchovments;
import org.pythonchik.dfanchovments.Enchantments.soulbreak;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class soulbound extends Enchantment implements Listener {

    DFanchovments plugin = (DFanchovments) Bukkit.getPluginManager().getPlugin("DFanchovments");
    private final static Map<Player, List<ItemStack>> itemsToKeep = new HashMap<Player, List<ItemStack>>();
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player k = e.getEntity().getKiller();
        ItemStack imh = k == null ? null : k.getInventory().getItemInMainHand();
        int lvl = 0;
        if (imh != null && imh.getItemMeta() != null) {
            lvl = imh.getItemMeta().getEnchantLevel(new soulbreak(new NamespacedKey(plugin,"soulbreak")));
        }
        List<ItemStack> soulbound = new ArrayList<ItemStack>();
        for (ItemStack i : e.getDrops()) {
            if (i.getItemMeta().hasEnchant(this)) {
                if (lvl > 0){
                    if (Math.random()>0.5){
                        continue;
                    }
                }
                soulbound.add(i);
            }
        }
        e.getDrops().removeAll(soulbound);
        itemsToKeep.put(e.getEntity(), soulbound);
    }

    public List<String> getTragers() {
        List<String> retu = new ArrayList<>();
        {
        //swords
        retu.add("wooden_sword");
        retu.add("stone_sword");
        retu.add("iron_sword");
        retu.add("diamond_sword");
        retu.add("golden_sword");
        retu.add("netherite_sword");
        //picks
        retu.add("wooden_pickaxe");
        retu.add("stone_pickaxe");
        retu.add("iron_pickaxe");
        retu.add("diamond_pickaxe");
        retu.add("golden_pickaxe");
        retu.add("netherite_pickaxe");
        //hellmets
        retu.add("leather_helmet");
        retu.add("chainmail_helmet");
        retu.add("iron_helmet");
        retu.add("diamond_helmet");
        retu.add("golden_helmet");
        retu.add("netherite_helmet");
        //chestplates
        retu.add("leather_chestplate");
        retu.add("chainmail_chestplate");
        retu.add("iron_chestplate");
        retu.add("diamond_chestplate");
        retu.add("golden_chestplate");
        retu.add("netherite_chestplate");
        //laggings
        retu.add("leather_leggings");
        retu.add("chainmail_leggings");
        retu.add("iron_leggings");
        retu.add("diamond_leggings");
        retu.add("golden_leggings");
        retu.add("netherite_leggings");
        //bots
        retu.add("leather_boots");
        retu.add("chainmail_boots");
        retu.add("iron_boots");
        retu.add("diamond_boots");
        retu.add("golden_boots");
        retu.add("netherite_boots");
    } // adding items swords/picks/armor
        return retu;
    }
    @EventHandler
    public void onPlayerRes(PlayerRespawnEvent e){
        if (itemsToKeep.containsKey(e.getPlayer())) {
            for (ItemStack stack : itemsToKeep.get(e.getPlayer())) e.getPlayer().getInventory().addItem(stack);
            itemsToKeep.remove(e.getPlayer());
        }
    }

    public soulbound(NamespacedKey id) {
        super(id);
    }

    public NamespacedKey getId(){
        return new NamespacedKey(plugin,"soulbound");
    }
    @Override
    public String getName() {
        return "Свзязь души";
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
