package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.pythonchik.dfanchovments.CEnchantment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class soulbound extends CEnchantment implements Listener {

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
    @Override
    public List<String> getTragers() {
        List<String> retu = new ArrayList<>();
        {
        retu.add("ENCHANTED_BOOK");
        retu.add("CROSSBOW");
        retu.add("BOW");
        //swords
        retu.add("WOODEN_SWORD");
        retu.add("STONE_SWORD");
        retu.add("IRON_SWORD");
        retu.add("DIAMOND_SWORD");
        retu.add("GOLDEN_SWORD");
        retu.add("NETHERITE_SWORD");
        //picks
        retu.add("WOODEN_PICKAXE");
        retu.add("STONE_PICKAXE");
        retu.add("IRON_PICKAXE");
        retu.add("DIAMOND_PICKAXE");
        retu.add("GOLDEN_PICKAXE");
        retu.add("NETHERITE_PICKAXE");
        //hellmets
        retu.add("LEATHER_HELMET");
        retu.add("CHAINMAIL_HELMET");
        retu.add("IRON_HELMET");
        retu.add("DIAMOND_HELMET");
        retu.add("GOLDEN_HELMET");
        retu.add("NETHERITE_HELMET");
        //chestplaTES
        retu.add("LEATHER_CHESTPLATE");
        retu.add("CHAINMAIL_CHESTPLATE");
        retu.add("IRON_CHESTPLATE");
        retu.add("DIAMOND_CHESTPLATE");
        retu.add("GOLDEN_CHESTPLATE");
        retu.add("NETHERITE_CHESTPLATE");
        //laggings
        retu.add("LEATHER_LEGGINGS");
        retu.add("CHAINMAIL_LEGGINGS");
        retu.add("IRON_LEGGINGS");
        retu.add("DIAMOND_LEGGINGS");
        retu.add("GOLDEN_LEGGINGS");
        retu.add("NETHERITE_LEGGINGS");
        //bots
        retu.add("LEATHER_BOOTS");
        retu.add("CHAINMAIL_BOOTS");
        retu.add("IRON_BOOTS");
        retu.add("DIAMOND_BOOTS");
        retu.add("GOLDEN_BOOTS");
        retu.add("NETHERITE_BOOTS");
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
        return DFanchovments.getConfig1().getString("soulbound.name");
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
}
