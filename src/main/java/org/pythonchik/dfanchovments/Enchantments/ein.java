package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;
import org.pythonchik.dfanchovments.Util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ein extends CEnchantment implements Listener {
    private static final String EXTRA_HIT_META = "awaiting_ein_hit";

    public ein(NamespacedKey id) {
        super(id);
    }

    @EventHandler(ignoreCancelled = true)
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity target)) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getItemMeta() == null) return;

        if (target.hasMetadata(EXTRA_HIT_META)) {
            target.removeMetadata(EXTRA_HIT_META, DFanchovments.plugin);
            return;
        }

        Integer level = item.getItemMeta().getPersistentDataContainer().get(this.id, PersistentDataType.INTEGER);

        if (level == null || level <= 0) return;

        double chance = 0.025 * level; // 2.5% * level
        if (Math.random() >= chance) return;

        double damage = event.getDamage();

        Bukkit.getScheduler().runTaskLater(DFanchovments.plugin, () -> {
            if (!target.isValid() || target.isDead()) return;
            if (!player.isOnline() || player.isDead()) return;

            target.setMetadata(EXTRA_HIT_META, new FixedMetadataValue(DFanchovments.plugin, true));
            target.damage(damage, player);
        }, 11L);
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add(Material.TRIDENT.name());
        retu.add(Material.MACE.name());
        retu.addAll(Util.swords());
        retu.addAll(Util.spears());
        retu.addAll(Util.axes());
        return retu;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Фантомный клинок");
        defaults.put("biomes", List.of("THE_VOID"));
        defaults.put("chance", 0);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 2);
        return defaults;
    }
}
