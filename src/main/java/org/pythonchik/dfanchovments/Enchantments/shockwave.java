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

import java.util.*;

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
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Тяжелая стрела");
        defaults.put("biomes", List.of("STONY_SHORE"));
        defaults.put("chance", 2);
        defaults.put("luck", 2);
        defaults.put("maxlvl", 4);
        return defaults;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
