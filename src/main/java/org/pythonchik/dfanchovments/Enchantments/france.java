package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class france extends CEnchantment implements Listener {
    public france(NamespacedKey id) {
        super(id);
    }
    private static final String ARROW_TAG = "french_arrow";

    @EventHandler(ignoreCancelled = true)
    public void onShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getProjectile() instanceof AbstractArrow arrow)) return;

        ItemStack bow = event.getBow();
        if (bow == null || bow.getItemMeta() == null) return;

        Integer level = bow.getItemMeta().getPersistentDataContainer().get(this.id, PersistentDataType.INTEGER);

        if (level == null || level <= 0) return;

        // 7% chance on shot
        if (Math.random() >= 0.07 * level) return;

        arrow.getPersistentDataContainer().set(this.id,PersistentDataType.BYTE, (byte) 1);
    }

    @EventHandler(ignoreCancelled = true)
    public void onArrowHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity target)) return;
        if (!(event.getDamager() instanceof AbstractArrow arrow)) return;

        Byte marked = arrow.getPersistentDataContainer()
                .get(this.id, PersistentDataType.BYTE);

        if (marked == null || marked != (byte) 1) return;

        EntityEquipment eq = target.getEquipment();
        if (eq == null) return;

        Location loc = target.getLocation();
        World world = target.getWorld();

        dropAndClear(world, loc, eq.getItemInMainHand(), () -> eq.setItemInMainHand(null));
        dropAndClear(world, loc, eq.getItemInOffHand(), () -> eq.setItemInOffHand(null));

        dropAndClear(world, loc, eq.getHelmet(), () -> eq.setHelmet(null));
        dropAndClear(world, loc, eq.getChestplate(), () -> eq.setChestplate(null));
        dropAndClear(world, loc, eq.getLeggings(), () -> eq.setLeggings(null));
        dropAndClear(world, loc, eq.getBoots(), () -> eq.setBoots(null));
    }

    private void dropAndClear(World world, Location loc, ItemStack item, Runnable clearSlot) {
        if (item == null || item.getType().isAir()) return;

        world.dropItemNaturally(loc, item.clone());
        clearSlot.run();
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add(Material.CROSSBOW.name());
        return retu;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Разоружение");
        defaults.put("biomes", List.of("THE_VOID"));
        defaults.put("chance", 0);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 1);
        return defaults;
    }
}
