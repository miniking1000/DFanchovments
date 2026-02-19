package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.*;

public class phishing extends CEnchantment implements Listener {
    public phishing(NamespacedKey id) {
        super(id);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPhishing(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH)
            return;
        if (event.getHand() == null)
            return; // should never happen
        Player player = event.getPlayer();
        ItemStack rod = player.getInventory().getItem(event.getHand());
        if (rod == null)
            return;
        ItemMeta meta = rod.getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER))
            return;
        int level = meta.getPersistentDataContainer().get(this.id, PersistentDataType.INTEGER);
        int chance = 5 * level;
        Entity caught = event.getCaught();
        if (!(caught instanceof Item caughtItem)) return;

        ItemStack caughtStack = caughtItem.getItemStack();
        if (caughtStack.getType().isAir()) return;
        if (!(Math.random() * 100 <= chance)) return;
        caughtStack.setAmount(caughtStack.getAmount()+1);
        caughtItem.setItemStack(caughtStack);
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add(Material.FISHING_ROD.name());
        return retu;
    }
    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Удача в воде");
        defaults.put("biomes", List.of("nether_wastes"));
        defaults.put("chance", 0.08);
        defaults.put("luck", 0.05);
        defaults.put("maxlvl", 5);
        return defaults;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
