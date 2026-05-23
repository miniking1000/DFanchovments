package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.*;

public class steveoshot extends CEnchantment implements Listener {
    public steveoshot(NamespacedKey id) {
        super(id);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onCrossShoot(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();

        if (!(projectile.getShooter() instanceof Player player)) {
            return;
        }
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (mainHand.getType().isAir() || !mainHand.hasItemMeta()) {
            return;
        }
        ItemMeta meta = mainHand.getItemMeta();
        if (meta.getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) {
            Entity guy = sendFlying(player.getPassengers(), null);
            if (guy != null && guy.getVehicle() != null) {
                guy.getVehicle().eject();
                guy.setVelocity(projectile.getVelocity().multiply(2));
                Bukkit.getScheduler().runTask(DFanchovments.plugin, projectile::remove);
            }
        }
    }

    private Entity sendFlying(List<Entity> list, Entity result) {
        for (Entity entity : list) {
            if (entity instanceof Player) {
                return entity;
            } else {
                result = sendFlying(entity.getPassengers(), result);
            }
        }
        return result;
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("BOW");
        return retu;
    }
    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Стиво-стрел");
        defaults.put("biomes", List.of("DESERT"));
        defaults.put("chance", 0.45);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 1);
        return defaults;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
