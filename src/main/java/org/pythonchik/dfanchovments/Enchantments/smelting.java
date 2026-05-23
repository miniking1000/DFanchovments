package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class smelting extends CEnchantment implements Listener {

    public smelting(NamespacedKey id) {
        super(id);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Material blockType = block.getType();

        Material dropMaterial;
        int baseAmount = 1;

        switch (blockType) {
            case SAND:
                dropMaterial = Material.GLASS;
                break;
            case RED_SAND:
                dropMaterial = Material.RED_STAINED_GLASS;
                break;
            case CLAY:
                dropMaterial = Material.BRICK;
                baseAmount = 4;
                break;
            case MUD:
                dropMaterial = Material.PACKED_MUD;
                break;
            default:
                return;
        }

        Player player = event.getPlayer();
        ItemStack shovel = player.getInventory().getItemInMainHand();

        if (shovel.getType().isAir() || !shovel.getType().name().endsWith("_SHOVEL")) return;
        if (!shovel.hasItemMeta()) return;

        if (!shovel.getItemMeta().getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) return;

        event.setDropItems(false);

        int fortuneLevel = 0;

        if (shovel.getItemMeta().hasEnchants()) {
            for (Map.Entry<Enchantment, Integer> entry : shovel.getEnchantments().entrySet()) {
                String enchKey = entry.getKey().getKey().getKey();

                if (enchKey.equals("fortune") || enchKey.equals("loot_bonus_blocks")) {
                    fortuneLevel = entry.getValue();
                    break;
                }
            }
        }

        int finalAmount = baseAmount;
        if (fortuneLevel > 0) {
            int r = ThreadLocalRandom.current().nextInt(fortuneLevel + 2);
            int multiplier = (r > 1) ? r : 1;
            finalAmount *= multiplier;
        }

        Location loc = block.getLocation().add(0.5, 0.5, 0.5);
        World world = block.getWorld();
        ItemStack dropItem = new ItemStack(dropMaterial, finalAmount);

        world.dropItemNaturally(loc, dropItem);

        world.playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 0.5f, 1.2f);
        world.spawnParticle(Particle.FLAME, loc, 10, 0.2, 0.2, 0.2, 0.02);
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("IRON_SHOVEL");
        retu.add("GOLDEN_SHOVEL");
        retu.add("DIAMOND_SHOVEL");
        retu.add("NETHERITE_SHOVEL");
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Плавление");
        defaults.put("biomes", List.of("DESERT", "BADLANDS", "NETHER_WASTES"));
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