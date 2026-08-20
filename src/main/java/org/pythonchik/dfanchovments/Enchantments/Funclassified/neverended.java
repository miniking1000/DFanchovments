package org.pythonchik.dfanchovments.Enchantments.Funclassified;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class neverended extends CEnchantment implements Listener {
    public neverended(NamespacedKey id) {
        super(id);
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.CREATIVE)) return;
        ItemStack item = event.getItem();
        if (item.getItemMeta() != null && item.getItemMeta().getPersistentDataContainer().has(id)) {
            item.setAmount(item.getAmount() + 1);
            event.setItem(item);
            player.updateInventory();
        }

        /*
        if (player.getInventory().getItemInMainHand().getItemMeta() != null && event.getItem().equals(player.getInventory().getItemInMainHand())) {
            if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(id)) {
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() + 1);
                player.updateInventory();
                return;
            }
        }
        if (player.getInventory().getItemInOffHand().getItemMeta() != null && event.getItem().equals(player.getInventory().getItemInOffHand())) {
            if (player.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(id)) {
                player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() + 1);
                player.updateInventory();
            }
        }
         */
    }

    @EventHandler
    public void onDeath(EntityResurrectEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (player.getInventory().getItemInOffHand().getItemMeta() != null && player.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING)) {
            if (player.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(id)) {
                player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() + 1);
                return;
            }
        }

        if (player.getInventory().getItemInMainHand().getItemMeta() != null && player.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING)) {
            if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(id)) {
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() + 1);
            }
        }

    }

    @EventHandler
    public void onThrow(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player player)) {
            return;
        }
        if (player.getGameMode().equals(GameMode.CREATIVE)) return;

        if (!(event.getEntity().getType().equals(EntityType.WIND_CHARGE))) {
            return;
        }

        if (player.getInventory().getItemInOffHand().getItemMeta() != null && player.getInventory().getItemInOffHand().getType().equals(Material.WIND_CHARGE) && !player.getInventory().getItemInMainHand().getType().equals(Material.WIND_CHARGE)) {
            if (player.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(id)) {
                player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() + 1);
                player.updateInventory();
                return;
            }
        }

        if (player.getInventory().getItemInMainHand().getItemMeta() != null && player.getInventory().getItemInMainHand().getType().equals(Material.WIND_CHARGE)) {
            if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(id)) {
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() + 1);
                player.updateInventory();
            }
        }
    }

    @EventHandler
    public void oncrystal(EntityPlaceEvent event) {
        Player player = event.getPlayer();
        if (player == null || player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        ItemStack item = player.getInventory().getItem(event.getHand());
        if (item == null) {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.getPersistentDataContainer().has(id)) {
            player.getInventory().setItem(event.getHand(), item);
            player.updateInventory();
        }
    }


    @Override
    public List<String> getTragers() {
        List<String> retu = new ArrayList<>();
        retu.add(Material.ENCHANTED_BOOK.name());
        retu.add(Material.ROTTEN_FLESH.name());
        retu.add(Material.ENCHANTED_GOLDEN_APPLE.name());
        retu.add(Material.WIND_CHARGE.name());
        retu.add(Material.TOTEM_OF_UNDYING.name());
        retu.add(Material.CHORUS_FLOWER.name());
        retu.add(Material.END_CRYSTAL.name());
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Бескончаемость");
        defaults.put("biomes", List.of("THE_VOID"));
        defaults.put("chance", 0);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 1);
        return defaults;
    }


}
