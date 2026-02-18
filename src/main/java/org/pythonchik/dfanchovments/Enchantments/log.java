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
import org.pythonchik.dfanchovments.Util;

import java.util.*;

public class log extends CEnchantment implements Listener {
    public log(NamespacedKey id) {
        super(id);
    }

    private static final Set<Material> LOGS = EnumSet.of(
            Material.OAK_LOG, Material.SPRUCE_LOG, Material.BIRCH_LOG,
            Material.JUNGLE_LOG, Material.ACACIA_LOG, Material.DARK_OAK_LOG,
            Material.MANGROVE_LOG, Material.CHERRY_LOG, Material.CRIMSON_STEM,
            Material.WARPED_STEM, Material.PALE_OAK_LOG
    );


    @EventHandler
    public void onPlayerChopALog(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block start = event.getBlock();
        if (player.isSneaking()) return;
        if (!LOGS.contains(start.getType())) return;

        ItemStack axe = player.getInventory().getItemInMainHand();
        if (!(axe.getItemMeta() instanceof Damageable meta)) return;
        if (!meta.getPersistentDataContainer().has(this.id)) return; // не зачарованный топор

        event.setCancelled(true);

        Set<Block> visited = new HashSet<>();
        Queue<Block> queue = new ArrayDeque<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            Block b = queue.poll();
            if (!LOGS.contains(b.getType()) || !visited.add(b)) continue;
            if (visited.size() >= 200) break;

            for (BlockFace face : Arrays.asList(
                    BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH,
                    BlockFace.EAST, BlockFace.WEST,
                    BlockFace.NORTH_EAST, BlockFace.NORTH_WEST,
                    BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST
            )) {
                Block rel = b.getRelative(face);
                if (!visited.contains(rel) && LOGS.contains(rel.getType())) queue.add(rel);
            }
        }
        if (visited.size() == 1) {
            event.setCancelled(false);
            return;
        }
        // Проверяем доступную прочность

        double multiplier = 1.0 / (1 + axe.getEnchantmentLevel(Enchantment.UNBREAKING));
        int cost = (int) Math.ceil(visited.size() * 2 * multiplier);
        int remaining = meta.hasMaxDamage() ? meta.getMaxDamage() : axe.getType().getMaxDurability() - meta.getDamage();

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
        axe.setItemMeta(meta);

        // Ломаем собранные блоки
        for (Block b : visited) b.breakNaturally(axe);
    }


    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");

        retu.addAll(Util.axes());
        return retu;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
