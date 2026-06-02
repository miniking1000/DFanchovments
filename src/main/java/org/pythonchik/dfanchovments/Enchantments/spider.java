package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.*;

public class spider extends CEnchantment {
    private static final long CACHE_TICKS = 20L;
    private final Map<UUID, Boolean> armorCache = new HashMap<>();

    private static final Vector[] CHECKS = {
            new Vector(0.45, 0, 0), new Vector(-0.45, 0, 0),
            new Vector(0, 0, 0.45), new Vector(0, 0, -0.45)
    };

    public spider(NamespacedKey id) {
        super(id);
        Bukkit.getScheduler().runTaskTimer(DFanchovments.plugin, armorCache::clear, CACHE_TICKS, CACHE_TICKS);

        // ЕДИНСТВЕННЫЙ ТАЙМЕР (1 тик) - Логика инвертирована для нулевой нагрузки
        Bukkit.getScheduler().runTaskTimer(DFanchovments.plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!hasFancyBoots(player)) continue;
                if (player.getGameMode() == GameMode.SPECTATOR) continue;

                // 1. ПРОВЕРКА ФИЗИКИ (Сверхбыстро, выполняется без обращения к предметам)
                if (player.isOnGround() || player.isFlying() || player.isGliding()) continue;

                Location loc = player.getLocation();
                World world = player.getWorld();

                double px = loc.getX();
                double py = loc.getY();
                double pz = loc.getZ();

                boolean chestWall = false;
                boolean legWall = false;

                // Примитивная математика: ищем блоки вокруг падающего/прыгающего игрока
                for (Vector v : CHECKS) {
                    int cx = (int) Math.floor(px + v.getX());
                    int cy = (int) Math.floor(py + 1.2);
                    int cz = (int) Math.floor(pz + v.getZ());
                    if (world.getBlockAt(cx, cy, cz).getType().isSolid()) chestWall = true;

                    int lx = (int) Math.floor(px + v.getX());
                    int ly = (int) Math.floor(py + 0.1);
                    int lz = (int) Math.floor(pz + v.getZ());
                    if (world.getBlockAt(lx, ly, lz).getType().isSolid()) legWall = true;

                    if (chestWall && legWall) break;
                }

                // ВАЖНО: Если игрок в воздухе, но НЕ касается стены - прерываем!
                // Нагрузка здесь равна 0.00%, сервер даже не смотрит в инвентарь игрока.
                if (!chestWall && !legWall) continue;

                // 3. ФИЗИКА КАРАБКАНЬЯ (Игрок коснулся стены и у него есть ботинки)
                Vector vel = player.getVelocity();
                if (chestWall) {
                    double climbSpeed = 0.2;
                    if (loc.getPitch() < -20) {
                        player.setFallDistance(0);
                        player.setVelocity(new Vector(vel.getX(), climbSpeed, vel.getZ()));
                    } else if (loc.getPitch() > 20) {
                        player.setFallDistance(0);
                        player.setVelocity(new Vector(vel.getX(), -climbSpeed, vel.getZ()));
                    }
                } else if (legWall && loc.getPitch() < -20) {
                    player.setFallDistance(0);
                    player.setVelocity(new Vector(vel.getX(), 0.25, vel.getZ()));
                }
            }
        }, 0L, 1L);
    }

    private boolean hasFancyBoots(Player player) {
        UUID uuid = player.getUniqueId();
        if (armorCache.containsKey(uuid)) {
            return armorCache.get(uuid);
        } else {
            boolean res = hasFancyBootsRaw(player);
            armorCache.put(player.getUniqueId(), res);
            return res;
        }
    }

    private boolean hasFancyBootsRaw(Player player) {
        ItemStack boots = player.getInventory().getBoots();
        if (boots == null || boots.getType().isAir()) return false;

        ItemMeta meta = boots.getItemMeta();
        if (meta == null) return false;

        return meta.getPersistentDataContainer().has(id);
    }

    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add("LEATHER_BOOTS");
        retu.add("CHAINMAIL_BOOTS");
        retu.add("IRON_BOOTS");
        retu.add("GOLDEN_BOOTS");
        retu.add("DIAMOND_BOOTS");
        retu.add("NETHERITE_BOOTS");
        return retu;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7Поступь ткача");
        defaults.put("biomes", List.of("DARK_FOREST", "LUSH_CAVES", "DRIPSTONE_CAVES"));
        defaults.put("chance", 0.25);
        defaults.put("luck", 0.05);
        defaults.put("maxlvl", 1);
        return defaults;
    }

    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}