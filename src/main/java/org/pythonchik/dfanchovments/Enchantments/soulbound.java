package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;
import org.pythonchik.dfanchovments.Util;

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

        retu.add("ENCHANTED_BOOK");
        retu.add("CROSSBOW");
        retu.add("BOW");
        retu.add("MACE");
        retu.add("TRIDENT");
        retu.add("TURTLE_HELMET");


        retu.addAll(Util.swords());
        retu.addAll(Util.spears());
        retu.addAll(Util.pickaxes());
        retu.addAll(Util.axes());
        retu.addAll(Util.shovels());
        retu.addAll(Util.hoes());

        retu.addAll(Util.armors());
        // adding items swords/picks/armor
        return retu;
    }


    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
