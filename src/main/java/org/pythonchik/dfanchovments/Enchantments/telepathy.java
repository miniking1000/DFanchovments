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
import org.pythonchik.dfanchovments.Util;

import java.util.*;

public class telepathy extends CEnchantment implements Listener {
    public telepathy(NamespacedKey id) {
        super(id);
    }
    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.addAll(Util.instruments());
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Сбор");
        defaults.put("biomes", List.of("SHATTERED_SAVANNA"));
        defaults.put("chance", 0.5);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 1);
        return defaults;
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
