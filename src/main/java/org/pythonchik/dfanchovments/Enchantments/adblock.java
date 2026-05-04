package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class adblock extends CEnchantment implements Listener {
    public adblock(NamespacedKey id) {
        super(id);
    }

    @EventHandler
    public void onShieldBlock(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player defender)) return;
        if (!defender.isBlocking()) return;
        ItemStack shield = defender.getItemInUse();
        if (shield == null) return;
        ItemMeta meta = shield.getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) return;
        int level = meta.getPersistentDataContainer().getOrDefault(this.id, PersistentDataType.INTEGER, 0);

        if (!(event.getDamager() instanceof LivingEntity attacker)) return;

        Vector knockback = attacker
                .getLocation()
                .getDirection()
                .normalize()
                .multiply(0.5) // at lvl 3 its 1.5 blocks/second
                .multiply(-level);

        knockback.add(new Vector(0, 0.25, 0));

        attacker.setVelocity(attacker.getVelocity().add(knockback));
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add(Material.SHIELD.name());
        return retu;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Отпор");
        defaults.put("biomes", List.of("end_highlands"));
        defaults.put("chance", 0.04);
        defaults.put("luck", 0.02);
        defaults.put("maxlvl", 3);
        return defaults;
    }
}
