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
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.Util;

import java.util.*;

public class leave extends CEnchantment implements Listener {
    public leave(NamespacedKey id) {
        super(id);
    }

    private static final Set<Material> LOGS = EnumSet.of(
            Material.OAK_LEAVES, Material.SPRUCE_LEAVES, Material.BIRCH_LEAVES,
            Material.JUNGLE_LEAVES, Material.ACACIA_LEAVES, Material.DARK_OAK_LEAVES,
            Material.MANGROVE_LEAVES, Material.CHERRY_LEAVES, Material.AZALEA_LEAVES,
            Material.FLOWERING_AZALEA_LEAVES, Material.PALE_OAK_LEAVES
    );


    @EventHandler
    public void onPlayerChopALeave(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block start = event.getBlock();
        if (player.isSneaking()) return;
        if (!LOGS.contains(start.getType())) return;

        ItemStack axe = player.getInventory().getItemInMainHand();
        if (!(axe.getItemMeta() instanceof Damageable meta)) return;
        if (!meta.getPersistentDataContainer().has(this.id)) return; // не зачарованный топор

        int level = meta.getPersistentDataContainer().getOrDefault(this.id, PersistentDataType.INTEGER, 0);

        event.setCancelled(true);

        Set<Block> visited = new HashSet<>();
        Queue<Block> queue = new ArrayDeque<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            Block b = queue.poll();
            if (!LOGS.contains(b.getType()) || !visited.add(b)) continue;
            if (visited.size() >= 128*level) break;

            for (BlockFace face : Arrays.asList(
                    BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH,
                    BlockFace.EAST, BlockFace.WEST, BlockFace.DOWN,
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
        if (!meta.isUnbreakable()) {
            meta.setDamage(meta.getDamage() + cost);
        }

        axe.setItemMeta(meta);

        // Ломаем собранные блоки
        for (Block b : visited) {
            b.breakNaturally(axe);
        }

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
        defaults.put("name", "&7Листопад");
        defaults.put("biomes", List.of("jungle"));
        defaults.put("chance", 0.1);
        defaults.put("luck", 0.05);
        defaults.put("maxlvl", 1);
        return defaults;
    }

    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
