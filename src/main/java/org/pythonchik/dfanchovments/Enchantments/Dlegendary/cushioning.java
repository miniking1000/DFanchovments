package org.pythonchik.dfanchovments.Enchantments.Dlegendary;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.dfanchovments.Bosses;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class cushioning extends CEnchantment implements Listener {

    public cushioning(NamespacedKey id) {
        super(id);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onKineticImpact(EntityDamageEvent event) {
        // Проверяем кинетический урон от врезания в блоки на элитрах
        if (event.getCause() != EntityDamageEvent.DamageCause.FLY_INTO_WALL) return;
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack chestplate = player.getInventory().getChestplate();
        if (chestplate == null || chestplate.getType() != Material.ELYTRA || !chestplate.hasItemMeta()) return;
        if (!chestplate.getItemMeta().getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) return;

        int level = chestplate.getItemMeta().getPersistentDataContainer().get(this.id, PersistentDataType.INTEGER);

        // Снижение урона: 30% за уровень (I = 30%, II = 60%, III = 90%)
        double reduction = Math.min(1.0, 0.30 * level); // miniking1000 - changed 0.9 -> 1.0 so allow for creative levels
        double originalDamage = event.getDamage();
        double finalDamage = originalDamage * (1.0 - reduction);

        event.setDamage(finalDamage);

        // Эффект смягчения удара
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BREEZE_SLIDE, 1.0f, 1.2f);
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_ELYTRA, 1.0f, 0.8f);
        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation().add(0, 0.5, 0), 12 * level, 0.4, 0.2, 0.4, 0.05);
    }

    @Override
    public List<String> getTragers() {
        List<String> retu = new ArrayList<>();
        retu.add(Material.ENCHANTED_BOOK.name());
        retu.add(Material.ELYTRA.name());
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&bАмортизация");
        defaults.put("biomes", List.of("THE_END", "END_HIGHLANDS", "END_MIDLANDS"));
        defaults.put("chance", 0.25);
        defaults.put("luck", 0.05);
        defaults.put("maxlvl", 3);

        Map<String, Object> bosses = new LinkedHashMap<>();
        bosses.put(Bosses.Rarity.LEGENDARY.getName(), 10);

        defaults.put("bosses", bosses);

        return defaults;
    }


}