package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.*;

public class rain extends CEnchantment implements Listener {

    DFanchovments plugin = (DFanchovments) Bukkit.getPluginManager().getPlugin("DFanchovments");
    public rain(NamespacedKey id) {
        super(id);
    }


    @EventHandler
    public void onArrowHit(ProjectileHitEvent event){
        if (event.getHitEntity() == null) {
            return;
        }
        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }
        if (((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta() == null) {
            return;
        }
        if ((event.getEntity().getPersistentDataContainer().has(new NamespacedKey(plugin,"rained"),PersistentDataType.BOOLEAN))) {
            return;
        }
        PersistentDataContainer container = ((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();
        if (container.has(id)) {
            if (Math.random() <= container.get(id,PersistentDataType.INTEGER)*0.06) {
                EntityType arrow = EntityType.ARROW;
                for (int i=0;i<container.get(id,PersistentDataType.INTEGER);i++) {
                    event.getHitEntity().getLocation().getWorld().spawnEntity(event.getHitEntity().getLocation().add(Math.random()*1-0.5,30-4*container.get(id,PersistentDataType.INTEGER),Math.random()*1-0.5),arrow).setVelocity(new Vector(0,-2.5*container.get(id,PersistentDataType.INTEGER),0));
                }
                event.getHitEntity().getLocation().getWorld().spawnEntity(event.getHitEntity().getLocation().add(0,5+5*container.get(id,PersistentDataType.INTEGER),0),arrow);
                event.getEntity().getPersistentDataContainer().set(new NamespacedKey(plugin,"rained"), PersistentDataType.BOOLEAN,true);
            }
        }
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
        defaults.put("name", "&7Шквал Стрел");
        defaults.put("biomes", List.of("WOODED_BADLANDS"));
        defaults.put("chance", 1);
        defaults.put("luck", 2);
        defaults.put("maxlvl", 5);
        return defaults;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
