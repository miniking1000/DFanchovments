package org.pythonchik.dfanchovments.Enchantments.Auncommon;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.pythonchik.dfanchovments.Bosses;
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

        Vector playerLook = defender.getLocation().getDirection().setY(0).normalize();
        Vector vectorToAttacker = event.getDamager().getLocation().toVector().subtract(defender.getLocation().toVector()).setY(0).normalize();

        if (playerLook.dot(vectorToAttacker) < 0) return;

        ItemMeta meta = shield.getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().has(this.getId(), PersistentDataType.INTEGER)) return;
        int level = meta.getPersistentDataContainer().getOrDefault(this.getId(), PersistentDataType.INTEGER, 0);

        if (!(event.getDamager() instanceof LivingEntity attacker)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK
                && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            return;
        }

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
    public List<String> getTragers() {
        List<String> retu = new ArrayList<>();
        retu.add(Material.ENCHANTED_BOOK.name());
        retu.add(Material.SHIELD.name());
        return retu;
    }



    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Отпор");
        defaults.put("biomes", List.of("end_highlands"));
        defaults.put("chance", 0.04);
        defaults.put("luck", 0.02);
        defaults.put("maxlvl", 3);

        Map<String, Object> bosses = new LinkedHashMap<>();
        bosses.put(Bosses.Rarity.UNCOMMON.getName(), 10);

        defaults.put("bosses", bosses);
        return defaults;
    }
}
