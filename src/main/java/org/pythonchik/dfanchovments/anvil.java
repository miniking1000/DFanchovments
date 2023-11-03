package org.pythonchik.dfanchovments;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class anvil implements Listener {

    Message message = DFanchovments.getMessage();
    @EventHandler
    public void AnviListener(PrepareAnvilEvent event){
        AnvilInventory anvil = event.getInventory();
        if (anvil.getItem(0) == null || anvil.getItem(1) == null) return;
        if (anvil.getItem(0).getItemMeta() == null || anvil.getItem(1).getItemMeta() ==null) return;
        //if (anvil.getItem(1).getType() != Material.ENCHANTED_BOOK) return;
        ItemStack slot1 = anvil.getItem(0);
        ItemStack slot2 = anvil.getItem(1);
        ItemStack item = slot1.clone();
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        for (CEnchantment ench : DFanchovments.CEnchantments){
            if (slot2.containsEnchantment(ench)){
                int lvl = slot1.getEnchantmentLevel(ench)!=slot2.getEnchantmentLevel(ench)?Math.max(slot1.getEnchantmentLevel(ench),slot2.getEnchantmentLevel(ench)): slot1.getEnchantmentLevel(ench)+1<=ench.getMaxLevel()?slot1.getEnchantmentLevel(ench)+1:slot1.getEnchantmentLevel(ench);
                if (!(slot1.containsEnchantment(ench))) meta.addEnchant(ench,slot2.getEnchantmentLevel(ench),false);
                else meta.addEnchant(ench, lvl, true);

                if (lore != null){
                    if (lore.contains(message.hex(ench.getName() + (lvl==0 ?"" : (" " + (lvl - 1 == 1 ? "I" : lvl - 1 == 2 ? "II" : lvl - 1 == 3 ? "III" : lvl - 1 == 4 ? "IV" : "V")))))) {
                        lore.set(lore.lastIndexOf(message.hex(ench.getName() + (lvl==0 ?"" : (" " + (lvl - 1 == 1 ? "I" : lvl - 1 == 2 ? "II" : lvl - 1 == 3 ? "III" : lvl - 1 == 4 ? "IV" : "V"))))), message.hex(ench.getName() + " " + (lvl == 1 ? "I" : lvl == 2 ? "II" : lvl == 3 ? "III" : lvl == 4 ? "IV" : "V")));
                    } else {
                        lore.add(message.hex(ench.getName() + " " + (lvl == 1 ? "I" : lvl == 2 ? "II" : lvl == 3 ? "III" : lvl == 4 ? "IV" : "V")));
                    }
                } else {
                    lore = new ArrayList<String>();
                    lore.add(message.hex(ench.getName() + " " + (lvl == 1 ? "I" : lvl == 2 ? "II" : lvl == 3 ? "III" : lvl == 4 ? "IV" : "V")));
                }
                meta.setLore(lore);
                item.setItemMeta(meta);
                event.getInventory().setRepairCostAmount(20);
                for (String gooditem : ench.getTragers()) {
                    if ((Material.getMaterial(gooditem) != null ? Material.getMaterial(gooditem) : Material.BARRIER).equals(item.getType())) {
                        event.setResult(item);
                        event.getInventory().setRepairCost(30);
                    }
                }

            }
        }


    }
}
