package org.pythonchik.dfanchovments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CEnchantment {
    protected NamespacedKey id;
    protected CEnchantment(NamespacedKey id) {
        this.id = id;
        try {
            this.getBiomes();
        } catch (Exception e) {
            DFanchovments.plugin.getLogger().warning("Enchantment " + id + " failed to load biomes: " + e);
        }
        DFanchovments.CEnchantments.add(this);
        if (this instanceof Listener listener && DFanchovments.plugin != null) {
            DFanchovments.plugin.getServer().getPluginManager().registerEvents(listener, DFanchovments.plugin);
        }
    }
    public String getName() {
        return DFanchovments.config.getString(this.getId().getKey() + ".name");
    }
    public List<String> getTragers() {
        List<String> retu = new ArrayList<>();
        retu.add(Material.ENCHANTED_BOOK.name());
        return retu;
    }
    public List<NamespacedKey> getConflicts() {
        return DFanchovments.config.getStringList(this.getId().getKey() + ".conflicts").stream().map(ceid -> NamespacedKey.fromString(ceid, DFanchovments.plugin)).toList();
    }
    public boolean conflictsWith(CEnchantment cEnchantment) {
        return this.conflictsWith(cEnchantment.getId());
    }
    public boolean conflictsWith(NamespacedKey namespacedKey) {
        return this.conflictsWith(namespacedKey.getKey());
    }
    public boolean conflictsWith(String key) {
        return this.getConflicts().contains(key);
    }
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "&7" + this.getId().getKey()); // string
        defaults.put("biomes", List.of()); // list<NamespacedKey of biomes, e.g. strings>
        defaults.put("chance", 0); // double
        defaults.put("luck", 0); // double
        defaults.put("maxlvl", 1); // int
        defaults.put("conflicts", List.of()); // list<String> (id of enchant, not namespaced key
        return defaults;
    }
    public NamespacedKey getId() {
        return this.id;
    }
    public int getMaxLevel() {
        return DFanchovments.config.getInt(this.getId().getKey() + ".maxlvl");
    }
    public int getStartLevel() {
        return 1;
    }
    public double getChance() {
        return DFanchovments.config.getDouble(this.getId().getKey() + ".chance");
    }
    public double getLuck() {
        return DFanchovments.config.getDouble(this.getId().getKey() + ".luck");
    }
    public double getDropChance(double luck) {
        return this.getChance() + this.getLuck()*luck;
    }
    public boolean roll(double luck) {
        return this.getDropChance(luck) > Math.random() * 100;
    }
    public List<NamespacedKey> getBiomes() {
        return DFanchovments.config.getStringList(this.getId().getKey() + ".biomes").stream().map(NamespacedKey::fromString).toList();
    }
    public boolean isInBiome(Biome biome) {
        return this.getBiomes().contains(biome.getKeyOrNull());
    }
    public boolean isInBiome(NamespacedKey biome) {
        return this.getBiomes().contains(biome);
    }
    public void onDisable() {
        // intentionally empty
    }

    /**
     * ChatGPT 5.2 written function, but I like the output it gives, DM me for tables if you are interested.
     * Geometric weighted level roll:
     * weight(levelIndex i) = q^i, i=0..levels-1
     * q interpolates between qLow..qHigh using softcapped luck01.
     */
    public int rollLevel(double luck) {
        int min = getStartLevel();
        int max = getMaxLevel();
        if (max < min) max = min;

        int levels = max - min + 1;
        if (levels == 1) {
            return 1;
        }
        double luck01 = Util.softcapLuck(luck);
        // 2) map luck01 to q in (0..1). Higher q => more weight to higher levels
        final double qLow  = 0.55; // luck=0
        final double qHigh = 0.90; // big luck (approaches, but never reaches)
        double q = qLow + (qHigh - qLow) * luck01;

        // 3) sample from the discrete distribution using inverse CDF without allocating arrays
        //    weights: 1, q, q^2, ... q^(levels-1)
        //    total = (1 - q^levels) / (1 - q)  (for q != 1)
        double total;
        if (Math.abs(1.0 - q) < 1e-12) {
            // extremely close to 1 => almost uniform weights
            total = levels;
        } else {
            total = (1.0 - Math.pow(q, levels)) / (1.0 - q);
        }

        double r = DFanchovments.random.nextDouble() * total;

        double term = 1.0;
        double cum = 0.0;
        int chosenIndex = levels - 1; // fallback to max

        for (int i = 0; i < levels; i++) {
            cum += term;
            if (r <= cum) {
                chosenIndex = i;
                break;
            }
            term *= q;
        }

        return min + chosenIndex;
    }

    public ItemStack createRandomBook(double luck) {
        return createBook(rollLevel(luck));
    }

    public ItemStack createBook(int level) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();

        if (meta == null) return book;

        int finalLevel = Math.max(
                this.getStartLevel(),
                Math.min(level, this.getMaxLevel())
        );

        meta.getPersistentDataContainer().set(this.getId(), PersistentDataType.INTEGER, finalLevel);

        meta.setLore(List.of(DFanchovments.message.hex(this.getName() + " " + Util.toRoman(finalLevel))));

        meta.setEnchantmentGlintOverride(true);
        book.setItemMeta(meta);

        return book;
    }


}
