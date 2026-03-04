package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.Util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class victor extends CEnchantment implements Listener {
    public victor(NamespacedKey id) {
        super(id);
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.addAll(Util.instruments());
        retu.addAll(Util.armors());
        retu.addAll(Util.spears());
        retu.addAll(Util.swords());
        retu.add(Material.CROSSBOW.name());
        retu.add(Material.BOW.name());
        retu.add(Material.TRIDENT.name());
        retu.add(Material.ELYTRA.name());
        retu.add(Material.SHIELD.name());
        retu.add(Material.MACE.name());
        retu.add(Material.FISHING_ROD.name());
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Предохранитель");
        defaults.put("biomes", List.of("sunflower_plains"));
        defaults.put("chance", 0.05);
        defaults.put("luck", 0.03);
        defaults.put("maxlvl", 3);
        return defaults;
    }

    @Override
    public NamespacedKey getId(){
        return this.id;
    }

    @EventHandler
    public void onDamange(PlayerItemDamageEvent event) {
        ItemStack stack = event.getItem();
        Damageable meta = (Damageable) stack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (!pdc.has(this.id, PersistentDataType.INTEGER)) return;
        int maxDamage = meta.hasMaxDamage() ? meta.getMaxDamage() : stack.getType().getMaxDurability();
        if (event.getDamage() + meta.getDamage() >= maxDamage) {
            int level = pdc.get(this.id, PersistentDataType.INTEGER);
            if (level == 1) {
                pdc.remove(this.id);
                Util.updateCustomLore(meta);
            } else {
                pdc.set(this.id, PersistentDataType.INTEGER, level - 1);
                Util.updateCustomLore(meta);
            }
            stack.setItemMeta(meta);
            event.setDamage(-50 * level);
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_TOTEM_USE, 0.5F, 0.95F);
        }
    }

    @EventHandler
    public void onItemBreak(PlayerItemBreakEvent event) {
        ItemStack stack = event.getBrokenItem();
        ItemMeta pre_meta = stack.getItemMeta();
        if (pre_meta instanceof Damageable meta) {
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            if (!pdc.has(this.id, PersistentDataType.INTEGER)) return;
            int level = pdc.get(this.id, PersistentDataType.INTEGER);
            if (level == 1) {
                pdc.remove(this.id);
            } else {
                pdc.set(this.id, PersistentDataType.INTEGER, level-1);
            }
            meta.setDamage(Math.max(0, meta.getDamage() - (50*level)));
            stack.setItemMeta(meta);
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_TOTEM_USE, 0.5F, 0.95F);
            stack.setAmount(stack.getAmount()+1);
        }
        // 50 durability per level
        // plays sound on activate
        // remove a level of enchant on activate
    }
}
