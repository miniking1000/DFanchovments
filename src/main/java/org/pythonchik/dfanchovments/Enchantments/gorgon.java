package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class gorgon extends CEnchantment implements Listener {

    public gorgon(NamespacedKey id) {
        super(id);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onShieldHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getDamager() instanceof LivingEntity attacker)) return;

        if (!player.isBlocking()) return;

        ItemStack shield = null;

        ItemStack offHand = player.getInventory().getItemInOffHand();
        if (offHand.getType().name().endsWith("SHIELD") && offHand.hasItemMeta()) {
            shield = offHand;
        } else {
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            if (mainHand.getType().name().endsWith("SHIELD") && mainHand.hasItemMeta()) {
                shield = mainHand;
            }
        }

        if (shield == null) return;

        if (!shield.getItemMeta().getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) return;

        Vector playerLook = player.getLocation().getDirection().setY(0).normalize();
        Vector vectorToAttacker = attacker.getLocation().toVector().subtract(player.getLocation().toVector()).setY(0).normalize();

        if (playerLook.dot(vectorToAttacker) < 0) return;

        double chance = 0.40;

        if (ThreadLocalRandom.current().nextDouble() <= chance) {

            int durationTicks = 200;

            attacker.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, durationTicks, 10, false, false, true));
            attacker.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, durationTicks, 10, false, false, true));

            player.getWorld().playSound(attacker.getLocation(), Sound.BLOCK_DEEPSLATE_BREAK, 1.0f, 0.5f);
            player.getWorld().spawnParticle(Particle.ASH, attacker.getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5, 0);
        }
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("SHIELD");
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&8Взгляд Горгоны");
        defaults.put("biomes", List.of("DRIPSTONE_CAVES", "STONY_PEAKS"));
        defaults.put("chance", 0.02);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 1);
        return defaults;
    }

    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}