package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.*;

public class headless extends CEnchantment implements Listener {
    NamespacedKey id;
    public headless(NamespacedKey id) {
        this.id = id;
    }
    private static final Map<EntityType, Material> HEADS = new HashMap<>();
    private static final Random RANDOM = new Random();

    static {
        HEADS.put(EntityType.ZOMBIE, Material.ZOMBIE_HEAD);
        HEADS.put(EntityType.SKELETON, Material.SKELETON_SKULL);
        HEADS.put(EntityType.WITHER_SKELETON, Material.WITHER_SKELETON_SKULL);
        HEADS.put(EntityType.CREEPER, Material.CREEPER_HEAD);
        HEADS.put(EntityType.PIGLIN, Material.PIGLIN_HEAD);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            ItemStack weapon = killer.getInventory().getItemInMainHand();
            if (weapon.getItemMeta() != null && weapon.getItemMeta().getPersistentDataContainer().has(id) && HEADS.containsKey(event.getEntityType())) {
                double dropChance = 0.2;
                int lootingLevel = weapon.getEnchantmentLevel(Enchantment.LOOTING);
                if (lootingLevel > 0) {
                    dropChance += lootingLevel * 0.05;
                }

                if (RANDOM.nextDouble() < dropChance) {
                    event.getDrops().clear();
                    event.getDrops().add(new ItemStack(HEADS.get(event.getEntityType())));
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            ItemStack weapon = player.getInventory().getItemInMainHand();
            if (weapon.getItemMeta() != null && weapon.getItemMeta().getPersistentDataContainer().has(id)) {
                event.setDamage(event.getDamage() - 3);
            }
        }
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");

        retu.add("WOODEN_AXE");
        retu.add("STONE_AXE");
        retu.add("IRON_AXE");
        retu.add("DIAMOND_AXE");
        retu.add("GOLDEN_AXE");
        retu.add("NETHERITE_AXE");

        return retu;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
