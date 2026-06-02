package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;
import org.pythonchik.dfanchovments.Util;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class soulbound extends CEnchantment implements Listener {
    public soulbound(NamespacedKey id) {
        super(id);
    }
    private final static Map<UUID, List<ItemStack>> itemsToKeep = new HashMap<UUID, List<ItemStack>>();
    private final File returnableItemsFile = new File(DFanchovments.plugin.getDataFolder(), "soulbound_items.yml");

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        List<ItemStack> soulbound = new ArrayList<ItemStack>();
        for (ItemStack i : e.getDrops()) {
            if (i.getItemMeta() != null && i.getItemMeta().getPersistentDataContainer().has(id)) {
                soulbound.add(i);
            }
        }
        e.getDrops().removeAll(soulbound);
        itemsToKeep.put(e.getEntity().getUniqueId(), soulbound);
    }
    @EventHandler
    public void onPlayerRes(PlayerRespawnEvent e){
        if (itemsToKeep.containsKey(e.getPlayer().getUniqueId())) {
            for (ItemStack stack : itemsToKeep.get(e.getPlayer().getUniqueId())) e.getPlayer().getInventory().addItem(stack);
            itemsToKeep.remove(e.getPlayer().getUniqueId());
        }
    }
    @Override
    public List<String> getTragers() {
        List<String> retu = new ArrayList<>();

        retu.add(Material.ENCHANTED_BOOK.name());
        retu.add(Material.CROSSBOW.name());
        retu.add(Material.BOW.name());
        retu.add(Material.MACE.name());
        retu.add(Material.TRIDENT.name());
        retu.add(Material.TURTLE_HELMET.name());

        retu.addAll(Util.swords());
        retu.addAll(Util.spears());
        retu.addAll(Util.pickaxes());
        retu.addAll(Util.axes());
        retu.addAll(Util.shovels());
        retu.addAll(Util.hoes());

        retu.addAll(Util.armors());
        // adding items swords/picks/armor
        return retu;
    }

    @Override
    public void onDisable() {
        if (itemsToKeep.isEmpty()) {
            if (returnableItemsFile.exists()) {
                returnableItemsFile.delete();
            }
            return;
        }

        YamlConfiguration config = new YamlConfiguration();

        int playerIndex = 0;

        for (Map.Entry<UUID, List<ItemStack>> entry : itemsToKeep.entrySet()) {
            UUID player = entry.getKey();
            List<ItemStack> items = entry.getValue();

            if (items == null || items.isEmpty()) {
                continue;
            }

            config.set(player + ".items", items);

            playerIndex++;
        }

        try {
            config.save(returnableItemsFile);
            DFanchovments.plugin.getLogger().info(
                    "Saved " + playerIndex + " players with soulbound items to " + returnableItemsFile.getName()
            );
        } catch (IOException e) {
            DFanchovments.plugin.getLogger().severe("Failed to save soulbound items!");
        }
    }

    @Override
    public void onEnable() {
        if (!returnableItemsFile.exists()) {
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(returnableItemsFile);

        for (String uuidString : config.getKeys(false)) {
            UUID uuid;

            try {
                uuid = UUID.fromString(uuidString);
            } catch (IllegalArgumentException e) {
                DFanchovments.plugin.getLogger().warning("Invalid UUID in returnable items file: " + uuidString);
                continue;
            }

            List<?> rawItems = config.getList(uuidString + ".items");
            if (rawItems == null || rawItems.isEmpty()) {
                continue;
            }
            List<ItemStack> items = new ArrayList<>();
            for (Object obj : rawItems) {
                if (obj instanceof ItemStack item && item.getType() != Material.AIR) {
                    items.add(item);
                }
            }
            itemsToKeep.put(uuid, items);
        }

        DFanchovments.plugin.getLogger().info("Loaded soulbound items from " + returnableItemsFile.getName());
        returnableItemsFile.delete();
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Связь Души");
        defaults.put("biomes", java.util.List.of("SOUL_SAND_VALLEY"));
        defaults.put("chance", 0.55);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 1);
        return defaults;
    }
}
