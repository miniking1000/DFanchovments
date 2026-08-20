package org.pythonchik.dfanchovments.Enchantments.Emythic;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.pythonchik.dfanchovments.Bosses;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.Util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class spring extends CEnchantment implements Listener {

    public spring(NamespacedKey id) {
        super(id);
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack boots = player.getInventory().getBoots();
        if (boots == null || boots.getItemMeta() == null) return;

        if (!boots.getItemMeta().getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) return;

        event.setCancelled(true);

        Location loc = player.getLocation();
        World world = player.getWorld();

        if (player.isSneaking()) {
            world.playSound(loc, Sound.BLOCK_SLIME_BLOCK_FALL, 1.0f, 1.0f);
            return;
        }

        int level = boots.getItemMeta().getPersistentDataContainer().getOrDefault(this.id, PersistentDataType.INTEGER, 1);

        double fallDistance = event.getDamage() + 3.0;
        double bouncePower = (fallDistance * 0.15) + (level * 0.15);
        if (bouncePower > 3.0) bouncePower = 3.0;

        Vector bounceVelocity = new Vector(player.getVelocity().getX(), bouncePower, player.getVelocity().getZ());

        Vector lookDirection = loc.getDirection().setY(0);
        if (lookDirection.lengthSquared() > 0) {
            lookDirection.normalize().multiply(bouncePower * 0.25);
            bounceVelocity.add(lookDirection);
        }

        player.setVelocity(bounceVelocity);

        world.playSound(loc, Sound.ENTITY_SLIME_SQUISH, 1.0f, 0.8f);
        world.playSound(loc, Sound.ENTITY_SLIME_JUMP, 1.0f, 1.2f);
        world.spawnParticle(Particle.ITEM, loc.clone().add(0, 0.2, 0), 20, 0.3, 0.1, 0.3, 0.05, new ItemStack(Material.SLIME_BALL));
    }

    @Override
    public List<String> getTragers() {
        List<String> retu = new ArrayList<>();
        retu.add(Material.ENCHANTED_BOOK.name());
        retu.addAll(Util.boots());
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "#A8E4A0Слаймость");
        defaults.put("biomes", List.of("SWAMP", "MANGROVE_SWAMP")); // Идеально вписывается в болотную тематику
        defaults.put("chance", 0.003);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 3);

        Map<String, Object> bosses = new LinkedHashMap<>();
        bosses.put(Bosses.Rarity.MYTHIC.getName(), 10);

        defaults.put("bosses", bosses);
        return defaults;
    }


}