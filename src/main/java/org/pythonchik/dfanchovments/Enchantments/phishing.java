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
import org.bukkit.inventory.meta.Damageable;
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
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
        if (event.getHand() == null) return;

        Player player = event.getPlayer();
        ItemStack rod = player.getInventory().getItem(event.getHand());
        if (rod == null) return;

        ItemMeta meta = rod.getItemMeta();
        if (meta == null) return;

        var pdc = meta.getPersistentDataContainer();
        Integer levelObj = pdc.get(this.id, PersistentDataType.INTEGER);
        if (levelObj == null) return;

        int level = levelObj;
        int chance = 5 * level;
        Entity caught = event.getCaught();
        if (!(caught instanceof Item caughtItem)) return;
        ItemStack caughtStack = caughtItem.getItemStack();
        if (caughtStack.getType().isAir()) return;

        if (!(Math.random() * 100.0 <= chance)) return;

        addOneMoreCaughtItem(caughtItem, caughtStack, caught);
    }


    private void addOneMoreCaughtItem(Item caughtItem, ItemStack caughtStack, Entity caught) {
        int max = caughtStack.getMaxStackSize();
        int amt = caughtStack.getAmount();

        // Если можно увеличить количество в этом entity-стаке — увеличиваем.
        if (max > 1 && amt < max) {
            caughtStack.setAmount(amt + 1);
            caughtItem.setItemStack(caughtStack);
            return;
        }

        // Иначе (нестакается ИЛИ стак переполнен) — спавним вторую сущность с 1 штукой.
        ItemStack extra = caughtStack.clone();
        extra.setAmount(1);

        var loc = caughtItem.getLocation();
        Item spawned = loc.getWorld().dropItemNaturally(loc, extra);

        spawned.setPickupDelay(caughtItem.getPickupDelay());
        spawned.setOwner(caughtItem.getOwner());
        caught.addPassenger(spawned);
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
