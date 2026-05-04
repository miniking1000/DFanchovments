package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.*;
import java.util.stream.StreamSupport;

public class xxxxxxxxxx extends CEnchantment implements Listener {
    private final Random random = new Random();
    private static final List<PotionEffectType> RANDOM_EFFECTS =
            StreamSupport.stream(Registry.EFFECT.spliterator(), false)
                    .filter(Objects::nonNull)
                    .filter(type -> !type.isInstant())
                    .filter(type -> !type.equals(PotionEffectType.RAID_OMEN))
                    .filter(type -> !type.equals(PotionEffectType.HERO_OF_THE_VILLAGE))
                    .toList();

    public xxxxxxxxxx(NamespacedKey id) {
        super(id);

        Bukkit.getScheduler().runTaskTimer(DFanchovments.plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                int level = getHighestLevel(player);
                if (level <= 0) continue;

                int effectsAmount = level * 3;

                for (int i = 0; i < effectsAmount; i++) {
                    PotionEffectType type = RANDOM_EFFECTS.get(random.nextInt(RANDOM_EFFECTS.size()));

                    player.addPotionEffect(new PotionEffect(
                            type,
                            10 * 20,
                            level,
                            true,
                            true,
                            true
                    ));
                }
            }
        }, 0L, 5 * 20L);
    }

    private int getHighestLevel(Player player) {
        int highest = 0;

        PlayerInventory inv = player.getInventory();

        highest = Math.max(highest, getLevel(inv.getHelmet()));
        highest = Math.max(highest, getLevel(inv.getChestplate()));
        highest = Math.max(highest, getLevel(inv.getLeggings()));
        highest = Math.max(highest, getLevel(inv.getBoots()));

        highest = Math.max(highest, getLevel(inv.getItemInMainHand()));
        highest = Math.max(highest, getLevel(inv.getItemInOffHand()));

        return highest;
    }

    private int getLevel(ItemStack item) {
        if (item == null || item.getType().isAir()) return 0;
        if (!item.hasItemMeta()) return 0;

        return item.getItemMeta().getPersistentDataContainer().getOrDefault(this.id, PersistentDataType.INTEGER, 0);
    }


    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        retu.add(Material.GRAY_STAINED_GLASS_PANE.name());
        return retu;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&0&kxxxxxxxxxx");
        defaults.put("biomes", List.of("pale_garden"));
        defaults.put("chance", 0.005);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 1);
        return defaults;
    }
}
