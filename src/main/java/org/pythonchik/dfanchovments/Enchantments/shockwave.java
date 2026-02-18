package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class shockwave extends CEnchantment implements Listener {
    @EventHandler
    private void AHAHA(ProjectileHitEvent event){
        if (event.getHitBlock() == null){
            return;
        }
        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }
        if (((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta() == null){
            return;
        }
        ItemMeta meta = ((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta();
        if (meta.getPersistentDataContainer().has(id)) {
            if (Math.random() <= meta.getPersistentDataContainer().get(id,PersistentDataType.INTEGER).intValue()*0.25) {
                final Collection<Entity> list = event.getEntity().getWorld().getNearbyEntities(event.getEntity().getLocation(), meta.getPersistentDataContainer().get(id, PersistentDataType.INTEGER)+1, meta.getPersistentDataContainer().get(id, PersistentDataType.INTEGER)+1, meta.getPersistentDataContainer().get(id, PersistentDataType.INTEGER)+1);
                for (final Entity entity : list)
                {
                    if (!(entity instanceof Damageable))
                        continue;

                    Damageable le = ((Damageable) entity);
                    le.damage(meta.getPersistentDataContainer().get(id, PersistentDataType.INTEGER));
                }
            }
        }
    }

    public shockwave(NamespacedKey id) {
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
        java.util.Map<String, Object> defaults = new java.util.LinkedHashMap<>();
        defaults.put("name", "&7Тяжелая стрела");
        defaults.put("biome", "THE_VOID");
        defaults.put("chance", 0);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 4);
        return defaults;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
