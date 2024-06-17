package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.List;

public class fish  extends CEnchantment implements Listener {

    NamespacedKey id;

    public fish(NamespacedKey id) {
        this.id = id;
    }
    @EventHandler
    public void CrossEvents(ProjectileLaunchEvent event){
        if (event.getEntity().getShooter() instanceof Player){
            Player player = (Player) event.getEntity().getShooter();
            if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
                if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(id)) {
                    if (player.getInventory().getItemInOffHand().getType().equals(Material.AXOLOTL_BUCKET)) {
                        AxolotlBucketMeta meta = (AxolotlBucketMeta) player.getInventory().getItemInOffHand().getItemMeta();
                        player.getInventory().setItemInOffHand(new ItemStack(Material.WATER_BUCKET));
                        Axolotl entity = (Axolotl) player.getLocation().getWorld().spawnEntity(player.getLocation().add(0, 1, 0), EntityType.AXOLOTL);
                        entity.setVelocity(event.getEntity().getVelocity());
                        entity.setVariant(meta.getVariant());
                        event.getEntity().teleport(new Location(event.getEntity().getWorld(),0,-9999,0));
                    } else if (player.getInventory().getItemInOffHand().getType().equals(Material.PUFFERFISH_BUCKET)) {
                        player.getInventory().setItemInOffHand(new ItemStack(Material.WATER_BUCKET));
                        player.getLocation().getWorld().spawnEntity(player.getLocation().add(0, 1, 0), EntityType.PUFFERFISH).setVelocity(event.getEntity().getVelocity());
                        event.getEntity().teleport(new Location(event.getEntity().getWorld(),0,-9999,0));
                    } else if (player.getInventory().getItemInOffHand().getType().equals(Material.SALMON_BUCKET)) {
                        player.getInventory().setItemInOffHand(new ItemStack(Material.WATER_BUCKET));
                        player.getLocation().getWorld().spawnEntity(player.getLocation().add(0, 1, 0), EntityType.SALMON).setVelocity(event.getEntity().getVelocity());
                        event.getEntity().teleport(new Location(event.getEntity().getWorld(),0,-9999,0));
                    } else if (player.getInventory().getItemInOffHand().getType().equals(Material.COD_BUCKET)) {
                        player.getInventory().setItemInOffHand(new ItemStack(Material.WATER_BUCKET));
                        player.getLocation().getWorld().spawnEntity(player.getLocation().add(0, 1, 0), EntityType.COD).setVelocity(event.getEntity().getVelocity());
                        event.getEntity().teleport(new Location(event.getEntity().getWorld(),0,-9999,0));
                    } else if (player.getInventory().getItemInOffHand().getType().equals(Material.TROPICAL_FISH_BUCKET)) {
                        player.getInventory().setItemInOffHand(new ItemStack(Material.WATER_BUCKET));
                        player.getLocation().getWorld().spawnEntity(player.getLocation().add(0, 1, 0), EntityType.TROPICAL_FISH).setVelocity(event.getEntity().getVelocity());
                        event.getEntity().teleport(new Location(event.getEntity().getWorld(),0,-9999,0));
                    }
                }
            }
        }
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("CROSSBOW");
        return retu;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
