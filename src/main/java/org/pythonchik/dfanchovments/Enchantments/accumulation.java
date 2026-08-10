package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.persistence.PersistentDataType;
import org.pythonchik.dfanchovments.Bosses;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;
import org.pythonchik.dfanchovments.Util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class accumulation extends CEnchantment implements Listener {


    private final NamespacedKey comboKey;
    private final NamespacedKey hitTimeKey;
    private final NamespacedKey swingTimeKey;

    public accumulation(NamespacedKey id) {
        super(id);
        this.comboKey = new NamespacedKey(DFanchovments.plugin, id.getKey() + "_combo");
        this.hitTimeKey = new NamespacedKey(DFanchovments.plugin, id.getKey() + "_hit_time");
        this.swingTimeKey = new NamespacedKey(DFanchovments.plugin, id.getKey() + "_swing_time");
    }

    // --- ВСПОМОГАТЕЛЬНЫЙ МЕТОД: ВЫЧИСЛЕНИЕ ДИНАМИЧЕСКОГО КУЛДАУНА ---
    private long getDynamicCooldownMs(Player player) {
        // Получаем атрибут скорости атаки через Registry (безопасно для 1.21.11)
        Attribute speedAttr = Attribute.ATTACK_SPEED;
        double attackSpeed = 1.6; // Дефолтное значение для меча

        if (speedAttr != null && player.getAttribute(speedAttr) != null) {
            attackSpeed = player.getAttribute(speedAttr).getValue();
        }

        // Формула: (1.0 / скорость_атаки) * 1000 = время в миллисекундах.
        // Вычитаем 40 мс (примерно 1 тик) для компенсации пинга и задержек сервера,
        // чтобы игрок не терял комбо, если ударит на долю секунды раньше из-за рассинхрона.
        return (long) ((1.0 / attackSpeed) * 1000) - 40;
    }

    // --- 1. ОТСЛЕЖИВАНИЕ СПАМА В ВОЗДУХ ---
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerSwing(PlayerAnimationEvent event) {
        if (event.getAnimationType() != PlayerAnimationType.ARM_SWING) return;

        Player player = event.getPlayer();
        ItemStack mainHand = player.getInventory().getItemInMainHand();

        if (mainHand.getType().isAir() || !mainHand.hasItemMeta()) return;
        if (!mainHand.getType().name().endsWith("_SWORD")) return;
        if (!mainHand.getItemMeta().getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) return;

        long currentTime = System.currentTimeMillis();
        long lastSwing = player.getPersistentDataContainer().getOrDefault(swingTimeKey, PersistentDataType.LONG, 0L);

        // Получаем идеальное время перезарядки ИМЕННО ДЛЯ ЭТОГО игрока в данный момент
        long requiredCooldownMs = getDynamicCooldownMs(player);

        // Если клик произошел до того, как шкала заполнилась — это спам!
        if (currentTime - lastSwing < requiredCooldownMs) {
            player.getPersistentDataContainer().set(comboKey, PersistentDataType.INTEGER, 0);
        }

        player.getPersistentDataContainer().set(swingTimeKey, PersistentDataType.LONG, currentTime);
    }

    // --- 2. ОТСЛЕЖИВАНИЕ ПОПАДАНИЙ ---
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onAccumulationHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity victim)) return;

        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (mainHand.getType().isAir() || !mainHand.hasItemMeta()) return;
        if (!mainHand.getType().name().endsWith("_SWORD")) return;
        if (!mainHand.getItemMeta().getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) return;

        int level = mainHand.getItemMeta().getPersistentDataContainer().get(this.id, PersistentDataType.INTEGER);

        long currentTime = System.currentTimeMillis();
        long lastHit = player.getPersistentDataContainer().getOrDefault(hitTimeKey, PersistentDataType.LONG, 0L);
        int combo = player.getPersistentDataContainer().getOrDefault(comboKey, PersistentDataType.INTEGER, 0);

        long hitTimeDiff = currentTime - lastHit;

        // Защита от Размашистого удара (Sweeping Edge)
        boolean isSweepHit = (hitTimeDiff < 50);

        if (!isSweepHit) {

            // Если с прошлого удара прошло больше 3 секунд (3000 мс) - комбо остыло
            if (hitTimeDiff > 3000) {
                combo = 0;
            }

            // Увеличиваем счетчик
            combo++;

            player.getPersistentDataContainer().set(hitTimeKey, PersistentDataType.LONG, currentTime);
            player.getPersistentDataContainer().set(comboKey, PersistentDataType.INTEGER, combo);
        }

        // --- 3. ЭФФЕКТЫ ---
        if (combo > 1) {

            double baseBonus = 0.05 * level;
            double multiplier = Math.pow(1.0 + baseBonus, combo - 1);

            event.setDamage(event.getDamage() * multiplier);

            float pitch = Math.min(2.0f, 0.5f + (combo * 0.1f));
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.6f, pitch);
            victim.getWorld().spawnParticle(Particle.WAX_OFF, victim.getLocation().add(0, 1, 0), combo * 2, 0.4, 0.4, 0.4, 0.1);

            // --- 4. ПЛАТА ПРОЧНОСТЬЮ ---
            if (!isSweepHit) {
                org.bukkit.inventory.meta.ItemMeta meta = mainHand.getItemMeta();
                if (meta instanceof Damageable damageable) {

                    int currentDamage = damageable.getDamage();
                    int extraDamage = combo - 1;

                    damageable.setDamage(currentDamage + extraDamage);
                    mainHand.setItemMeta(meta);

                    if (damageable.getDamage() >= mainHand.getType().getMaxDurability()) {
                        player.getInventory().setItemInMainHand(null);
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                    }
                }
            }
        }
    }

    @Override
    public List<String> getTragers() {
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.addAll(Util.swords());
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&5Накопление");
        defaults.put("biomes", List.of("END_HIGHLANDS")); // Идеальное зачарование для Энда
        defaults.put("chance", 0.005);
        defaults.put("luck", 0.0);
        defaults.put("maxlvl", 3); // 3 уровня прокачки (5%, 10%, 15% за удар)

        Map<String, Object> bosses = new LinkedHashMap<>();
        bosses.put(Bosses.Rarity.MYTHIC.getName(), 10);

        defaults.put("bosses", bosses);
        return defaults;
    }

    @Override
    public NamespacedKey getId() {
        return this.id;
    }
}