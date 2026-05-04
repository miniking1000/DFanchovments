package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.*;

public class dodge extends CEnchantment implements Listener {
    @EventHandler
    public void DbtxHxRvieptrLyoLwdzVpjTdXTYTVTYR(EntityDamageByEntityEvent event) { //this name is not random btw
        if(!(event.getEntity() instanceof Player player)){
            return;
        }
        ItemStack chestplate = player.getInventory().getChestplate();
        if(chestplate != null && chestplate.getItemMeta() != null && chestplate.getItemMeta().getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) {
            int level = chestplate.getItemMeta().getPersistentDataContainer().getOrDefault(this.id, PersistentDataType.INTEGER, 0);
            if (Math.random() < (0.10 + (0.5 * level))) {
                event.setDamage(0);
            }
        }
    }

    public dodge(NamespacedKey id) {
        super(id);
    }
    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        //chestplates
        retu.add("LEATHER_CHESTPLATE");
        retu.add("CHAINMAIL_CHESTPLATE");
        retu.add("IRON_CHESTPLATE");
        retu.add("DIAMOND_CHESTPLATE");
        retu.add("GOLDEN_CHESTPLATE");
        retu.add("NETHERITE_CHESTPLATE");
        return retu;
    }
    @Override
    public Map<String, Object> getDefaultConfig() {
      Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Уклонение");
        defaults.put("biomes", List.of("CHERRY_GROVE"));
        defaults.put("chance", 1);
        defaults.put("luck", 2);
        defaults.put("maxlvl", 1);
        return defaults;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
