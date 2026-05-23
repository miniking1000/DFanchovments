package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
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

public class spider extends CEnchantment {

    public spider(NamespacedKey id) {
        super(id);

        Bukkit.getScheduler().runTaskTimer(DFanchovments.plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {

                if (player.isFlying() || player.isGliding()) continue;

                ItemStack boots = player.getInventory().getBoots();
                if (boots == null || boots.getItemMeta() == null) continue;

                if (!boots.getItemMeta().getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) continue;

                if (player.isOnGround()) continue;

                boolean chestWall = false;
                boolean legWall = false;
                Location loc = player.getLocation();

                Vector[] checks = {
                        new Vector(0.45, 0, 0), new Vector(-0.45, 0, 0),
                        new Vector(0, 0, 0.45), new Vector(0, 0, -0.45)
                };

                for (Vector v : checks) {
                    Block chestBlock = loc.clone().add(v).add(0, 1.2, 0).getBlock();
                    Block legBlock = loc.clone().add(v).add(0, 0.1, 0).getBlock();

                    if (chestBlock.getType().isSolid()) chestWall = true;
                    if (legBlock.getType().isSolid()) legWall = true;
                }

                Vector vel = player.getVelocity();
                if (chestWall) {
                    double climbSpeed = 0.2;

                    if (loc.getPitch() < -20) {
                        player.setFallDistance(0);
                        player.setVelocity(new Vector(vel.getX(), climbSpeed, vel.getZ()));
                    }
                    else if (loc.getPitch() > 20) {
                        player.setFallDistance(0);
                        player.setVelocity(new Vector(vel.getX(), -climbSpeed, vel.getZ()));
                    }
                }
                else if (legWall && loc.getPitch() < -20) {
                    player.setFallDistance(0);
                    player.setVelocity(new Vector(vel.getX(), 0.25, vel.getZ()));
                }
            }
        }, 0L, 1L);
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("LEATHER_BOOTS");
        retu.add("CHAINMAIL_BOOTS");
        retu.add("IRON_BOOTS");
        retu.add("GOLDEN_BOOTS");
        retu.add("DIAMOND_BOOTS");
        retu.add("NETHERITE_BOOTS");
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Поступь ткача");
        defaults.put("biomes", List.of("DARK_FOREST", "LUSH_CAVES", "DRIPSTONE_CAVES"));
        defaults.put("chance", 0.25);
        defaults.put("luck", 0.05);
        defaults.put("maxlvl", 1);
        return defaults;
    }

    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}