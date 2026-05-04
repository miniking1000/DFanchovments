package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;
import org.pythonchik.dfanchovments.Util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ebow extends CEnchantment implements Listener {
    public ebow(NamespacedKey id) {
        super(id);
    }
    private final NamespacedKey arrowKey = new NamespacedKey(DFanchovments.plugin, "earrow");

    @EventHandler
    public void onSpecialBowShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getProjectile() instanceof Arrow arrow)) return;

        ItemStack bow = event.getBow();
        if (bow == null || bow.getItemMeta() == null) return;

        ItemMeta meta = bow.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (!pdc.has(this.id, PersistentDataType.INTEGER)) return;
        int level = pdc.getOrDefault(this.id, PersistentDataType.INTEGER, 0);
        level = Math.max(1, Math.min(11, level));

        int cooldownSeconds = Math.max(0, 11 - level);
        arrow.getPersistentDataContainer().set(this.arrowKey, PersistentDataType.INTEGER, level);
        arrow.setShooter(player);
        player.setCooldown(bow.getType(), cooldownSeconds * 20);
    }

    @EventHandler
    public void onSpecialArrowLand(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow arrow)) return;

        PersistentDataContainer pdc = arrow.getPersistentDataContainer();

        if (!pdc.has(this.arrowKey, PersistentDataType.INTEGER)) return;
        int level = pdc.getOrDefault(this.arrowKey, PersistentDataType.INTEGER, 1);
        level = Math.max(1, Math.min(11, level));

        if (!(arrow.getShooter() instanceof Player player)) {
            arrow.remove();
            return;
        }

        Location teleportLocation = arrow.getLocation();

        // Safer teleport position
        teleportLocation.setPitch(player.getLocation().getPitch());
        teleportLocation.setYaw(player.getLocation().getYaw());

        player.damage(Math.max(0.0, 5.25 - (level * 0.25)), DamageSource.builder(DamageType.ENDER_PEARL).build());
        player.teleport(teleportLocation);

        arrow.remove();
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add(Material.BOW.name());
        return retu;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Стрела края");
        defaults.put("biomes", List.of("dripstone_caves"));
        defaults.put("chance", 0.1);
        defaults.put("luck", 0.2);
        defaults.put("maxlvl", 1);
        return defaults;
    }
}
