package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.pythonchik.dfanchovments.CEnchantment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.List;

public class telepathy extends CEnchantment implements Listener {

    DFanchovments plugin = (DFanchovments) Bukkit.getPluginManager().getPlugin("DFanchovments");
    @EventHandler
    public void onBlockMine(BlockBreakEvent event){
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getItemMeta() != null){
            if (player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(DFanchovments.telepathy)) {
                if (player.getInventory().firstEmpty() != -1) {
                    player.getInventory().addItem(event.getBlock().getDrops(player.getInventory().getItemInMainHand()).toArray(new ItemStack[0]));
                    event.setDropItems(false);
                }
            }
        }
    }

    public telepathy(NamespacedKey id) {
        super(id);
    }

    public NamespacedKey getId(){
        return new NamespacedKey(plugin,"telepathy");
    }
    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("WOODEN_PICKAXE");
        retu.add("STONE_PICKAXE");
        retu.add("IRON_PICKAXE");
        retu.add("DIAMOND_PICKAXE");
        retu.add("GOLDEN_PICKAXE");
        retu.add("NETHERITE_PICKAXE");
        return retu;
    }
    @Override
    public String getName() {
        return "telepathy";
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
