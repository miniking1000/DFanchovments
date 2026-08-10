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
import org.bukkit.event.entity.EntityDamageEvent;
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
    private final NamespacedKey timeKey;

    public accumulation(NamespacedKey id) {
        super(id);
        // Сохраняем комбо и время в самом игроке, чтобы счетчик не сбрасывался при Размашистом ударе
        this.comboKey = new NamespacedKey(DFanchovments.plugin, id.getKey() + "_combo");
        this.timeKey = new NamespacedKey(DFanchovments.plugin, id.getKey() + "_time");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onAccumulationHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity victim)) return;

        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (mainHand.getType().isAir() || !mainHand.hasItemMeta()) return;

        // Работает только для мечей
        if (!mainHand.getType().name().endsWith("_SWORD")) return;
        if (!mainHand.getItemMeta().getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) return;

        int level = mainHand.getItemMeta().getPersistentDataContainer().get(this.id, PersistentDataType.INTEGER);

        long currentTime = System.currentTimeMillis();
        long lastTime = player.getPersistentDataContainer().getOrDefault(timeKey, PersistentDataType.LONG, 0L);
        int combo = player.getPersistentDataContainer().getOrDefault(comboKey, PersistentDataType.INTEGER, 0);

        // --- ЗАЩИТА ОТ РАЗМАШИСТОГО УДАРА (Sweeping Edge) ---
        // Ивент срабатывает несколько раз в одну миллисекунду, если мы бьем толпу.
        // Чтобы комбо не накручивалось х10 за один взмах, проверяем разницу во времени.
        boolean isSweepHit = (currentTime - lastTime < 50);

        if (!isSweepHit) {
            // --- 1. ПРОВЕРКА ИДЕАЛЬНОГО УДАРА ---
            // getCooledAttackStrength возвращает шкалу от 0.0 до 1.0.
            // Берем 0.95f для компенсации микро-задержек пинга
            boolean isPerfect = player.getAttackCooldown() >= 0.95f;

            // Если игрок спамил (не идеальный удар) ИЛИ прошло больше 3 секунд (3000 мс)
            if (!isPerfect || (currentTime - lastTime > 3000)) {
                combo = 0; // Наказание: полное обнуление комбо!
            }

            // Если удар идеальный - увеличиваем комбо
            if (isPerfect) {
                combo++;
            }

            // Обновляем данные в памяти игрока
            player.getPersistentDataContainer().set(timeKey, PersistentDataType.LONG, currentTime);
            player.getPersistentDataContainer().set(comboKey, PersistentDataType.INTEGER, combo);
        }

        // --- 2. ПРИМЕНЕНИЕ ЭФФЕКТОВ НАКОПЛЕНИЯ ---
        // Комбо начинает работать со 2-го удара (combo > 1)
        if (combo > 1) {

            // ГЕОМЕТРИЧЕСКАЯ ПРОГРЕССИЯ
            // Базовый бонус: 5% за каждый уровень зачарования
            double baseBonus = 0.05 * level;
            // Формула: Урон * (1 + бонус) ^ (комбо - 1)
            double multiplier = Math.pow(1.0 + baseBonus, combo - 1);

            event.setDamage(event.getDamage() * multiplier);

            // АУДИО И ВИЗУАЛ (Зарядка)
            // Чем выше комбо, тем звонче звук (максимальный питч = 2.0)
            float pitch = Math.min(2.0f, 0.5f + (combo * 0.1f));
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.6f, pitch);
            victim.getWorld().spawnParticle(Particle.WAX_OFF, victim.getLocation().add(0, 1, 0), combo * 2, 0.4, 0.4, 0.4, 0.1);

            // --- 3. РАСПЛАТА ПРОЧНОСТЬЮ ---
            // Применяем износ только к основной цели, чтобы размашистый удар не сломал меч за секунду
            if (!isSweepHit) {
                org.bukkit.inventory.meta.ItemMeta meta = mainHand.getItemMeta();
                if (meta instanceof Damageable damageable) {

                    int currentDamage = damageable.getDamage();
                    // Экспоненциальный рост износа: 10-й удар нанесет мечу 9 единиц урона прочности за раз!
                    int extraDamage = combo - 1;

                    damageable.setDamage(currentDamage + extraDamage);
                    mainHand.setItemMeta(damageable);

                    // Проверяем, не сломался ли меч от перенапряжения
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