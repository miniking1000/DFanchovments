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

import java.util.*;

public class fireworks extends CEnchantment implements Listener {

    @EventHandler
    private void HAHAH(ProjectileHitEvent event){
        if (!(event.getEntity().getShooter() instanceof Player player)) {
            return;
        }
        if (player.getInventory().getItemInMainHand().getItemMeta() == null){
            return;
        }
        if ((event.getEntity().getPersistentDataContainer().has(new NamespacedKey(DFanchovments.plugin,"exploded"), PersistentDataType.BOOLEAN))){
            return;
        }
        if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(id, PersistentDataType.INTEGER)) {
            if (Math.random() <= player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().getOrDefault(id, PersistentDataType.INTEGER, 0)*0.25){
                final Collection<Entity> list = event.getEntity().getNearbyEntities(2,2,2);
                for (final Entity entity : list)
                {
                    if (!(entity instanceof Damageable))
                        continue;

                    Damageable le = ((Damageable) entity);
                    event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), 0F);
                    event.getEntity().getPersistentDataContainer().set(new NamespacedKey(DFanchovments.plugin,"exploded"), PersistentDataType.BOOLEAN,true);
                    le.damage(player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().getOrDefault(id, PersistentDataType.INTEGER, 0)*2);
                }
            }
        }
    }

    public fireworks(NamespacedKey id) {
        super(id);
    }
    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("BOW");
        return retu;
    }
    @Override
    public java.util.Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Пороховой заряд");
        defaults.put("biomes", List.of("BADLANDS"));
        defaults.put("chance", 2);
        defaults.put("luck", 2);
        defaults.put("maxlvl", 3);
        return defaults;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }

}
