package org.pythonchik.dfanchovments.Enchantments.Auncommon;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.dfanchovments.Bosses;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        ItemStack mainHand = player.getItemInUse();
        if (mainHand == null || !mainHand.hasItemMeta()) {
            return;
        }
        ItemMeta meta = mainHand.getItemMeta();
        assert meta != null;
        if (!meta.getPersistentDataContainer().has(this.getId(), PersistentDataType.INTEGER)) {
            return;
        }
        Entity guy = sendFlying(player.getPassengers(), null);
        if (guy != null && guy.getVehicle() != null) {
            guy.getVehicle().eject();
            guy.setVelocity(projectile.getVelocity().multiply(2));
            Bukkit.getScheduler().runTask(DFanchovments.plugin, projectile::remove);
        }

    }

    private Entity sendFlying(List<Entity> list, Entity result) {
        for (Entity entity : list) {
            if (entity instanceof LivingEntity) {
                return entity;
            } else {
                result = sendFlying(entity.getPassengers(), result);
            }
        }
        return result;
    }

    @Override
    public List<String> getTragers() {
        List<String> retu = new ArrayList<>();
        retu.add(Material.ENCHANTED_BOOK.name());
        retu.add(Material.BOW.name());
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Мобо-стрел");
        defaults.put("biomes", List.of("DESERT"));
        defaults.put("chance", 0.45);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 1);

        Map<String, Object> bosses = new LinkedHashMap<>();
        bosses.put(Bosses.Rarity.UNCOMMON.getName(), 10);

        defaults.put("bosses", bosses);
        return defaults;
    }


}
