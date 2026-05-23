package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.Util;

import java.util.*;

public class harvester extends CEnchantment implements Listener {
    public harvester(NamespacedKey id) {
        super(id);
    }

    private static final Set<Material> CROPS = EnumSet.of(
            Material.WHEAT, Material.CARROTS, Material.POTATOES,
            Material.BEETROOTS, Material.NETHER_WART, Material.COCOA
    );

    @EventHandler
    public void onPlayerHarvestCrop(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block start = event.getBlock();

        if (player.isSneaking()) return;
        if (!CROPS.contains(start.getType())) return;

        ItemStack hoe = player.getInventory().getItemInMainHand();
        if (!(hoe.getItemMeta() instanceof Damageable meta)) return;
        if (!meta.getPersistentDataContainer().has(this.id)) return;

        int level = meta.getPersistentDataContainer().getOrDefault(this.id, PersistentDataType.INTEGER, 0);

        event.setCancelled(true);

        Set<Block> visited = new HashSet<>();
        Queue<Block> queue = new ArrayDeque<>();
        queue.add(start);

        Material cropType = start.getType();

        while (!queue.isEmpty()) {
            Block b = queue.poll();
            if (b.getType() != cropType || !visited.add(b)) continue;
            if (visited.size() >= 128 * level) break;

            for (BlockFace face : Arrays.asList(
                    BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST,
                    BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST
            )) {
                for (int dy = -1; dy <= 1; dy++) {
                    Block rel = b.getRelative(face.getModX(), dy, face.getModZ());
                    if (!visited.contains(rel) && rel.getType() == cropType) {
                        if (isFullyGrown(rel)) {
                            queue.add(rel);
                        }
                    }
                }
            }
        }

        if (visited.size() == 1) {
            event.setCancelled(false);
            return;
        }

        double multiplier = 1.0 / (1 + hoe.getEnchantmentLevel(Enchantment.UNBREAKING));
        int cost = (int) Math.ceil(visited.size() * 1 * multiplier);
        int remaining = meta.hasMaxDamage() ? meta.getMaxDamage() : hoe.getType().getMaxDurability() - meta.getDamage();

        if (remaining <= 1) return;
        if (cost > remaining) {
            int maxBlocks = Math.max(0, remaining);
            while (visited.size() > maxBlocks) {
                Iterator<Block> it = visited.iterator();
                Block last = null;
                while (it.hasNext()) last = it.next();
                if (last != null) visited.remove(last);
            }
            cost = (int) Math.ceil(visited.size() * 1 * multiplier);
        }

        if (!meta.isUnbreakable()) {
            meta.setDamage(meta.getDamage() + cost);
        }
        hoe.setItemMeta(meta);

        for (Block b : visited) {
            b.breakNaturally(hoe);
        }
    }

    private boolean isFullyGrown(Block block) {
        if (block.getBlockData() instanceof Ageable ageable) {
            return ageable.getAge() == ageable.getMaximumAge();
        }
        return true;
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.addAll(Util.hoes());
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Чревоугодие");
        defaults.put("biomes", List.of("PLAINS", "SUNFLOWER_PLAINS"));
        defaults.put("chance", 0.35);
        defaults.put("luck", 0.05);
        defaults.put("maxlvl", 1);
        return defaults;
    }

    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}