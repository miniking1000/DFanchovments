package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class spider extends CEnchantment {

    private static final Vector[] CHECKS = {
            new Vector(0.45, 0, 0), new Vector(-0.45, 0, 0),
            new Vector(0, 0, 0.45), new Vector(0, 0, -0.45)
    };

    public spider(NamespacedKey id) {
        super(id);

        // ЕДИНСТВЕННЫЙ ТАЙМЕР (1 тик) - Логика инвертирована для нулевой нагрузки
        Bukkit.getScheduler().runTaskTimer(DFanchovments.plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {

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

                // 2. ПРОВЕРКА ПРЕДМЕТОВ (Сработает ТОЛЬКО если игрок физически трется о стену)
                ItemStack boots = player.getInventory().getBoots();
                if (boots == null || !boots.hasItemMeta()) continue;
                if (!boots.getItemMeta().getPersistentDataContainer().has(this.id, PersistentDataType.INTEGER)) continue;

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