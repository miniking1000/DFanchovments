package org.pythonchik.dfanchovments;

import org.bukkit.Material;
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
    public void AnviListener(PrepareAnvilEvent event) {
        AnvilInventory anvil = event.getInventory();
        if ((anvil.getItem(0) != null && anvil.getItem(1) != null)) { // have both items
            if (anvil.getItem(0).getItemMeta() == null || anvil.getItem(1).getItemMeta() == null) return; // both must have meta
            ItemStack slot1 = anvil.getItem(0);
            ItemStack slot2 = anvil.getItem(1);
            ItemStack item = slot1.clone();
            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore();
            boolean isApplied = false;
            for (CEnchantment ench : DFanchovments.CEnchantments) {
                if (slot2.getItemMeta().getPersistentDataContainer().has(ench.getId())) {
                    int num1 = slot1.getItemMeta().getPersistentDataContainer().has(ench.getId()) ? slot1.getItemMeta().getPersistentDataContainer().get(ench.getId(),PersistentDataType.INTEGER) : 0;
                    int num2 = slot2.getItemMeta().getPersistentDataContainer().has(ench.getId()) ? slot2.getItemMeta().getPersistentDataContainer().get(ench.getId(),PersistentDataType.INTEGER) : 0;
                    int lvl = num1 != num2 ?
                            //1 and 2 different
                            Math.max(num1, num2)
                            //taking maximium of those
                            : num1 + 1 <= ench.getMaxLevel() ?
                            //1 and 2 are equal AND (for example 2 with max 5) 2+1(static 1 bc +1 level)=3 <= 5
                            //will be 2+1=3 else, taking ench lvl
                            num1 + 1
                            : num1;
                    // if the item supports the enchantment in the first place, then do it
                    for (String gooditem : ench.getTragers()) {
                        if ((Material.getMaterial(gooditem) != null ? Material.getMaterial(gooditem) : Material.BARRIER).equals(item.getType())) {
                            isApplied = true;
                            if (!(slot1.getItemMeta().getPersistentDataContainer().has(ench.getId()))) {
                                meta.getPersistentDataContainer().set(ench.getId(), PersistentDataType.INTEGER, num2);
                                meta.setEnchantmentGlintOverride(true);
                            }
                            else {
                                meta.getPersistentDataContainer().set(ench.getId(),PersistentDataType.INTEGER,lvl);
                                meta.setEnchantmentGlintOverride(true);
                            }

                            if (lore != null) {
                                if (lore.contains(message.hex(ench.getName() + (lvl == 0 ? "" : (" " + (lvl - 1 == 1 ? "I" : lvl - 1 == 2 ? "II" : lvl - 1 == 3 ? "III" : lvl - 1 == 4 ? "IV" : "V")))))) {
                                    lore.set(lore.lastIndexOf(message.hex(ench.getName() + (lvl == 0 ? "" : (" " + (lvl - 1 == 1 ? "I" : lvl - 1 == 2 ? "II" : lvl - 1 == 3 ? "III" : lvl - 1 == 4 ? "IV" : "V"))))), message.hex(ench.getName() + " " + (lvl == 1 ? "I" : lvl == 2 ? "II" : lvl == 3 ? "III" : lvl == 4 ? "IV" : "V")));
                                } else {
                                    if (lore.contains(message.hex(ench.getName() + (lvl == 0 ? "" : (" " + (lvl == 1 ? "I" : lvl == 2 ? "II" : lvl == 3 ? "III" : lvl == 4 ? "IV" : "V")))))) {
                                        lore.set(lore.lastIndexOf(message.hex(ench.getName() + (lvl == 0 ? "" : (" " + (lvl == 1 ? "I" : lvl == 2 ? "II" : lvl == 3 ? "III" : lvl == 4 ? "IV" : "V"))))), message.hex(ench.getName() + " " + (lvl == 1 ? "I" : lvl == 2 ? "II" : lvl == 3 ? "III" : lvl == 4 ? "IV" : "V")));
                                    } else {
                                        lore.add(0,message.hex(ench.getName() + " " + (lvl == 1 ? "I" : lvl == 2 ? "II" : lvl == 3 ? "III" : lvl == 4 ? "IV" : "V")));
                                    }
                                }
                            } else {
                                lore = new ArrayList<String>();
                                lore.add(0,message.hex(ench.getName() + " " + (lvl == 1 ? "I" : lvl == 2 ? "II" : lvl == 3 ? "III" : lvl == 4 ? "IV" : "V")));
                            }
                        }
                    }
                    meta.setLore(lore);
                    meta.setDisplayName(message.hex(event.getView().getRenameText())); // event.getInventory().getRenameText()
                    // after all custom we also need to move regular enchants to the new item from second slot(e.g. do regular anvil thing)
                    for (Map.Entry<Enchantment, Integer> Entry : slot2.getItemMeta().getEnchants().entrySet()) {
                        meta.addEnchant(Entry.getKey(), Math.max(meta.getEnchantLevel(Entry.getKey()), Entry.getValue()), false);
                    }

                    item.setItemMeta(meta);
                    //for (String gooditem : ench.getTragers()) {
                    //    if ((Material.getMaterial(gooditem) != null ? Material.getMaterial(gooditem) : Material.BARRIER).equals(item.getType())) {
                    if (isApplied) {
                        event.getView().setRepairItemCountCost(1);
                        event.setResult(item);
                        event.getView().setRepairCost(30);
                        //event.getInventory().setRepairCost(30);
                    }
                    //    }
                    //}

                }
            }
        } else if (anvil.getItem(0) != null && anvil.getItem(1) == null) {
            ItemStack slot1 = anvil.getItem(0);
            ItemStack item = slot1.clone();
            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore();
            for (CEnchantment ench : DFanchovments.CEnchantments) {
                if (slot1.getItemMeta().getPersistentDataContainer().has(ench.getId())) {
                    int num1 = slot1.getItemMeta().getPersistentDataContainer().has(ench.getId()) ? slot1.getItemMeta().getPersistentDataContainer().get(ench.getId(),PersistentDataType.INTEGER) : 1;
                    int lvl = num1;
                    if (!(slot1.getItemMeta().getPersistentDataContainer().has(ench.getId()))) {
                        meta.getPersistentDataContainer().set(ench.getId(),PersistentDataType.INTEGER, num1);
                        meta.setEnchantmentGlintOverride(true);
                    }
                    if (lore != null) {
                        if (lore.contains(message.hex(ench.getName() + (lvl == 0 ? "" : (" " + (lvl - 1 == 1 ? "I" : lvl - 1 == 2 ? "II" : lvl - 1 == 3 ? "III" : lvl - 1 == 4 ? "IV" : "V")))))) {
                            lore.set(lore.lastIndexOf(message.hex(ench.getName() + (lvl == 0 ? "" : (" " + (lvl - 1 == 1 ? "I" : lvl - 1 == 2 ? "II" : lvl - 1 == 3 ? "III" : lvl - 1 == 4 ? "IV" : "V"))))), message.hex(ench.getName() + " " + (lvl == 1 ? "I" : lvl == 2 ? "II" : lvl == 3 ? "III" : lvl == 4 ? "IV" : "V")));
                        } else {
                            if (lore.contains(message.hex(ench.getName() + (lvl == 0 ? "" : (" " + (lvl == 1 ? "I" : lvl == 2 ? "II" : lvl == 3 ? "III" : lvl == 4 ? "IV" : "V")))))) {
                                lore.set(lore.lastIndexOf(message.hex(ench.getName() + (lvl == 0 ? "" : (" " + (lvl == 1 ? "I" : lvl == 2 ? "II" : lvl == 3 ? "III" : lvl == 4 ? "IV" : "V"))))), message.hex(ench.getName() + " " + (lvl == 1 ? "I" : lvl == 2 ? "II" : lvl == 3 ? "III" : lvl == 4 ? "IV" : "V")));
                            } else {
                                lore.add(0,message.hex(ench.getName() + " " + (lvl == 1 ? "I" : lvl == 2 ? "II" : lvl == 3 ? "III" : lvl == 4 ? "IV" : "V")));
                            }
                        }
                    } else {
                        lore = new ArrayList<String>();
                        lore.add(0,message.hex(ench.getName() + " " + (lvl == 1 ? "I" : lvl == 2 ? "II" : lvl == 3 ? "III" : lvl == 4 ? "IV" : "V")));
                    }

                    meta.setLore(lore);
                    meta.setDisplayName(message.hex(event.getView().getRenameText()));
                    item.setItemMeta(meta);
                    event.setResult(item);
                }
            }
        }
    }
}
