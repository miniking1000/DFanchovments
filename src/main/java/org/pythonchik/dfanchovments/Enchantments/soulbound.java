package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class soulbound extends CEnchantment implements Listener {
    NamespacedKey id;
    public soulbound(NamespacedKey id) {
        this.id = id;
    }
    private final static Map<Player, List<ItemStack>> itemsToKeep = new HashMap<Player, List<ItemStack>>();
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        List<ItemStack> soulbound = new ArrayList<ItemStack>();
        for (ItemStack i : e.getDrops()) {
            if (i.getItemMeta().getPersistentDataContainer().has(id)) {
                soulbound.add(i);
            }
        }
        e.getDrops().removeAll(soulbound);
        itemsToKeep.put(e.getEntity(), soulbound);
    }
    @EventHandler
    public void onPlayerRes(PlayerRespawnEvent e){
        if (itemsToKeep.containsKey(e.getPlayer())) {
            for (ItemStack stack : itemsToKeep.get(e.getPlayer())) e.getPlayer().getInventory().addItem(stack);
            itemsToKeep.remove(e.getPlayer());
        }
    }
    @Override
    public List<String> getTragers() {
        List<String> retu = new ArrayList<>();
        {
        retu.add("ENCHANTED_BOOK");
        retu.add("CROSSBOW");
        retu.add("BOW");

        //five very important items.
        retu.add("DEAD_FIRE_CORAL_FAN");
        retu.add("COOKED_CHICKEN");
        retu.add("WRITTEN_BOOK");
        retu.add("YELLOW_GLAZED_TERRACOTTA");
        retu.add("IRON_HOE");

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


    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
