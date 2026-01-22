package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.*;

public class breakerOrIsIt extends CEnchantment implements Listener {
    NamespacedKey id;
    public breakerOrIsIt(NamespacedKey id) {
        this.id = id;
    }

    private static final Set<Material> ORES = EnumSet.of(
            Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE, Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE,
            Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE, Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE,
            Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE, Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE,
            Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE, Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE,
            Material.NETHER_GOLD_ORE, Material.NETHER_QUARTZ_ORE, Material.ANCIENT_DEBRIS, Material.RAW_IRON_BLOCK,
            Material.RAW_COPPER_BLOCK, Material.RAW_GOLD_BLOCK, Material.GLOWSTONE, Material.AMETHYST_CLUSTER
    );

    @EventHandler
    public void onPlayerChopAnOre(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block start = event.getBlock();
        if (player.isSneaking()) return;
        if (!ORES.contains(start.getType())) return;

        ItemStack pickaxe = player.getInventory().getItemInMainHand();
        if (!(pickaxe.getItemMeta() instanceof Damageable meta)) return;
        if (!meta.getPersistentDataContainer().has(this.id)) return; // не зачарованный топор

        event.setCancelled(true);

        Set<Block> visited = new HashSet<>();
        Queue<Block> queue = new ArrayDeque<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            Block b = queue.poll();
            if (!ORES.contains(b.getType()) || !visited.add(b)) continue;
            if (visited.size() >= 64) break;

            for (BlockFace face : Arrays.asList(
                    BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH,
                    BlockFace.EAST, BlockFace.WEST, BlockFace.DOWN,
                    BlockFace.NORTH_EAST, BlockFace.NORTH_WEST,
                    BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST
            )) {
                Block rel = b.getRelative(face);
                if (!visited.contains(rel) && ORES.contains(rel.getType())) queue.add(rel);
            }
        }
        if (visited.size() == 1) {
            event.setCancelled(false);
            return;
        }
        // Проверяем доступную прочность

        double multiplier = 1.0 / (1 + pickaxe.getEnchantmentLevel(Enchantment.UNBREAKING));
        int cost = (int) Math.ceil(visited.size() * 2 * multiplier);
        int remaining = meta.hasMaxDamage() ? meta.getMaxDamage() : pickaxe.getType().getMaxDurability() - meta.getDamage();

        if (remaining <= 1) return; // топор почти сломан, ничего не делаем

        // Если не хватает прочности — уменьшаем количество добываемых блоков
        if (cost > remaining) {
            int maxBlocks = Math.max(0, (remaining) / 2); // чтобы не сломать
            while (visited.size() > maxBlocks) {
                Iterator<Block> it = visited.iterator();
                Block last = null;
                while (it.hasNext()) last = it.next();
                if (last != null) visited.remove(last);
            }
            cost = (int) Math.ceil(visited.size() * 2 * multiplier);
        }

        // Применяем урон топору
        meta.setDamage(meta.getDamage() + cost);
        pickaxe.setItemMeta(meta);

        // Ломаем собранные блоки
        for (Block b : visited) b.breakNaturally(pickaxe);
    }


    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");

        // all pickaxes
        retu.add("WOODEN_PICKAXE");
        retu.add("STONE_PICKAXE");
        retu.add("IRON_PICKAXE");
        retu.add("DIAMOND_PICKAXE");
        retu.add("GOLDEN_PICKAXE");
        retu.add("NETHERITE_PICKAXE");

        return retu;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
