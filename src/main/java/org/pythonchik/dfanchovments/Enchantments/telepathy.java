package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class telepathy extends CEnchantment implements Listener {
    NamespacedKey id;
    public telepathy(NamespacedKey id) {
        this.id = id;
    }
    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");

        retu.add("WOODEN_PICKAXE");
        retu.add("STONE_PICKAXE");
        retu.add("IRON_PICKAXE");
        retu.add("DIAMOND_PICKAXE");
        retu.add("GOLDEN_PICKAXE");
        retu.add("NETHERITE_PICKAXE");

        retu.add("WOODEN_SHOVEL");
        retu.add("STONE_SHOVEL");
        retu.add("IRON_SHOVEL");
        retu.add("DIAMOND_SHOVEL");
        retu.add("GOLDEN_SHOVEL");
        retu.add("NETHERITE_SHOVEL");

        retu.add("WOODEN_AXE");
        retu.add("STONE_AXE");
        retu.add("IRON_AXE");
        retu.add("DIAMOND_AXE");
        retu.add("GOLDEN_AXE");
        retu.add("NETHERITE_AXE");

        retu.add("WOODEN_HOE");
        retu.add("STONE_HOE");
        retu.add("IRON_HOE");
        retu.add("DIAMOND_HOE");
        retu.add("GOLDEN_HOE");
        retu.add("NETHERITE_HOE");
        return retu;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }

    @EventHandler
    public void onBlockDropItem(BlockDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack toolInHand = player.getInventory().getItemInMainHand();
        if (toolInHand.getItemMeta() == null || !toolInHand.getItemMeta().getPersistentDataContainer().has(id)) {
            return;
        }
        Iterator<Item> dropIterator = event.getItems().iterator();
        while (dropIterator.hasNext()) {
            Item itemEntity = dropIterator.next();
            ItemStack dropStack = itemEntity.getItemStack();
            HashMap<Integer, ItemStack> leftoverMap = player.getInventory().addItem(dropStack);
            if (leftoverMap.isEmpty()) {
                dropIterator.remove();
            } else {
                ItemStack leftoverStack = leftoverMap.values().iterator().next();
                if (leftoverStack.getAmount() > 0) {
                    itemEntity.setItemStack(leftoverStack);
                } else {
                    dropIterator.remove();
                }
            }
        }
    }
}
