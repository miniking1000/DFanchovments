package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class updraft extends CEnchantment {

    public updraft(NamespacedKey id) {
        super(id);

        Bukkit.getScheduler().runTaskTimer(DFanchovments.plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.isGliding()) continue;

                ItemStack chestplate = player.getInventory().getChestplate();
                if (chestplate == null || !chestplate.getType().name().equals("ELYTRA")) continue;
                if (chestplate.getItemMeta() == null || !chestplate.getItemMeta().getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) continue;

                int level = chestplate.getItemMeta().getPersistentDataContainer().getOrDefault(this.id, PersistentDataType.INTEGER, 1);

                Location loc = player.getLocation();
                boolean heatFound = false;

                for (int i = 1; i <= 15; i++) {
                    Block b = loc.clone().subtract(0, i, 0).getBlock();
                    Material type = b.getType();

                    if (type == Material.LAVA || type == Material.MAGMA_BLOCK ||
                            type == Material.CAMPFIRE || type == Material.SOUL_CAMPFIRE ||
                            type == Material.FIRE || type == Material.SOUL_FIRE) {
                        heatFound = true;
                        break;
                    }
                    else if (type.isOccluding()) {
                        break;
                    }
                }

                if (heatFound) {
                    Vector vel = player.getVelocity();

                    vel.setY(vel.getY() + (0.12 * level));
                    player.setVelocity(vel);

                    player.getWorld().spawnParticle(
                            Particle.CAMPFIRE_COSY_SMOKE,
                            player.getLocation().subtract(0, 0.5, 0),
                            2, 0.2, 0.1, 0.2, 0.02
                    );
                }
            }
        }, 0L, 5L);
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("ELYTRA");
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Восходящий поток");
        defaults.put("biomes", List.of("NETHER_WASTES", "CRIMSON_FOREST", "WARPED_FOREST", "BASALT_DELTAS", "SOUL_SAND_VALLEY")); // Идеально подходит для Незера
        defaults.put("chance", 0.25);
        defaults.put("luck", 0.05);
        defaults.put("maxlvl", 2);
        return defaults;
    }

    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}