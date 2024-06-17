package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class fireworks extends CEnchantment implements Listener {

    DFanchovments plugin = (DFanchovments) Bukkit.getPluginManager().getPlugin("DFanchovments");
    NamespacedKey id;

    @EventHandler
    private void HAHAH(ProjectileHitEvent event){
        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }
        if (((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta() == null){
            return;
        }
        if ((event.getEntity().getPersistentDataContainer().has(new NamespacedKey(plugin,"exploded"),PersistentDataType.BOOLEAN))){
            return;
        }
        if (((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(id)) {
            if (Math.random() <= ((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(id,PersistentDataType.INTEGER)*0.25){
                final Collection<Entity> list = event.getEntity().getNearbyEntities(2,2,2);
                for (final Entity entity : list)
                {
                    if (!(entity instanceof Damageable))
                        continue;

                    Damageable le = ((Damageable) entity);
                    event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), 0F);
                    event.getEntity().getPersistentDataContainer().set(new NamespacedKey(plugin,"exploded"), PersistentDataType.BOOLEAN,true);
                    le.damage(((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(id,PersistentDataType.INTEGER)*2);
                }
            }
        }
    }

    public fireworks(NamespacedKey id) {
        this.id = id;
    }
    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("BOW");
        return retu;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }

}
