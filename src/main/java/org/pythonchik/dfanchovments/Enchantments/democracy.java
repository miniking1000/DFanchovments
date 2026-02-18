package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class democracy extends CEnchantment implements Listener {
    NamespacedKey id;
    public democracy(NamespacedKey id) {
        this.id = id;
    }
    @Override
    public List<String> getTragers() {
        List<String> retu = new ArrayList<>();
        return retu;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
    //FOR future me, this book adds .5 to height using attributes, that's why nothing is here

    @EventHandler
    public void onbreak(PlayerItemBreakEvent event) {
        ItemMeta meta = event.getBrokenItem().getItemMeta();
        if (meta != null && meta.getPersistentDataContainer().has(id)) {
            ItemStack replacement = meta.getUseRemainder();
            if (replacement != null && replacement.getType() != Material.AIR) {
                HashMap<Integer, ItemStack> leftover = event.getPlayer().getInventory().addItem(replacement);
                if (!leftover.isEmpty()) {
                    leftover.values().forEach((item) -> {
                        event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), item);
                    });
                }
            }
        }
    }
}
