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

import java.util.*;

public class soulbound extends CEnchantment implements Listener {
    public soulbound(NamespacedKey id) {
        super(id);
    }
    private final static Map<Player, List<ItemStack>> itemsToKeep = new HashMap<Player, List<ItemStack>>();
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        List<ItemStack> soulbound = new ArrayList<ItemStack>();
        for (ItemStack i : e.getDrops()) {
            if (i.getItemMeta() != null && i.getItemMeta().getPersistentDataContainer().has(id)) {
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

        retu.add(Material.ENCHANTED_BOOK.name());
        retu.add(Material.CROSSBOW.name());
        retu.add(Material.BOW.name());
        retu.add(Material.MACE.name());
        retu.add(Material.TRIDENT.name());
        retu.add(Material.TURTLE_HELMET.name());

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
    public void onDisable() {
        if (!itemsToKeep.isEmpty()) {
            DFanchovments.plugin.getLogger().warning("Soulbound class contains some items that will NOT be returned! Here is raw map:");
            DFanchovments.plugin.getLogger().info(itemsToKeep.toString());
        }
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Связь Души");
        defaults.put("biomes", java.util.List.of("SOUL_SAND_VALLEY"));
        defaults.put("chance", 0.55);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 1);
        return defaults;
    }
}
