package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.Util;

import java.util.*;

public class frut extends CEnchantment implements Listener {
    private static final Random RANDOM = new Random();
    public frut(NamespacedKey id) {
        super(id);
    }
    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.addAll(Util.chestplates());
        return retu;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }

    @EventHandler
    public void onHurt(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ItemStack chestplate = player.getInventory().getChestplate();
        if (chestplate != null && chestplate.hasItemMeta() && chestplate.getItemMeta().getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) {
            double threshold = 2.0 * chestplate.getItemMeta().getPersistentDataContainer().getOrDefault(this.id, PersistentDataType.INTEGER, 0); // one heart
            double healthAfterHit = player.getHealth() - event.getFinalDamage();
            if (healthAfterHit <= threshold && healthAfterHit > 0) {
                chorusTeleport(player);
            }
        }
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Вуаль пустоты");
        defaults.put("biomes", List.of("SMALL_END_ISLANDS"));
        defaults.put("chance", 999);
        defaults.put("luck", 3);
        defaults.put("maxlvl", 3);
        return defaults;
    }

    public static boolean chorusTeleport(Player player) {
        Location from = player.getLocation();
        World world = player.getWorld();

        for (int i = 0; i < 16; i++) {
            double x = from.getX() + (RANDOM.nextDouble() - 0.5D) * 16.0D; // -8..+8
            double y = from.getY() + (RANDOM.nextInt(16) - 8);              // -8..+7
            double z = from.getZ() + (RANDOM.nextDouble() - 0.5D) * 16.0D; // -8..+8

            y = Math.max(world.getMinHeight(), Math.min(y, world.getMaxHeight() - 1));

            Location target = new Location(world, x, y, z, from.getYaw(), from.getPitch());

            Location safe = findSafeLocation(target);
            if (safe == null) continue;

            // Звук до телепорта
            world.playSound(from, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1.0f, 1.0f);

            boolean success = player.teleport(safe, PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT);
            if (!success) return false;

            // Звук после телепорта
            world.playSound(safe, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1.0f, 1.0f);
            return true;
        }

        return false;
    }

    private static Location findSafeLocation(Location loc) {
        World world = loc.getWorld();
        if (world == null) return null;

        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        // Ищем вниз ближайший "пол"
        while (y > world.getMinHeight()) {
            Block blockBelow = world.getBlockAt(x, y - 1, z);
            if (blockBelow.getType().isSolid()) {
                break;
            }
            y--;
        }

        Block feet = world.getBlockAt(x, y, z);
        Block head = world.getBlockAt(x, y + 1, z);
        Block below = world.getBlockAt(x, y - 1, z);

        if (!below.getType().isSolid()) return null;
        if (!feet.isPassable()) return null;
        if (!head.isPassable()) return null;

        return new Location(world, x + 0.5, y, z + 0.5, loc.getYaw(), loc.getPitch());
    }

}
