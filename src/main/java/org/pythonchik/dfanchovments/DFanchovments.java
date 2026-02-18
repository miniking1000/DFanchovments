package org.pythonchik.dfanchovments;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.pythonchik.dfanchovments.Enchantments.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;


public final class DFanchovments extends JavaPlugin {
    public static FileConfiguration config;
    public static DFanchovments plugin;
    public static ArrayList<CEnchantment> CEnchantments = new ArrayList<>();
    public static Random random = new Random();
    public static HashSet<Biome> allBiomes = new HashSet<>();

    //add a line to ench

    public static Message message;
    public static Message getMessage(){return message;}

    public void reload5() {
        loadConfig();
    }

    public static FileConfiguration getConfig1() {
        return config;
    }

    @Override
    public void onEnable() {
        message = new Message();
        plugin = this;
        CEnchantments = new ArrayList<>();

        new soulbound(    new NamespacedKey(plugin,"soulbound"));
        new shockwave(    new NamespacedKey(plugin,"shockwave"));
        new fireworks(    new NamespacedKey(plugin,"fireworks"));
        new potioness(    new NamespacedKey(plugin,"potioness"));
        new dodge(        new NamespacedKey(plugin,"dodge"));
        new rain(         new NamespacedKey(plugin,"rain"));
        new exp(          new NamespacedKey(plugin,"exp"));
        new bamboom(      new NamespacedKey(plugin,"bamboom"));
        new tntanon(      new NamespacedKey(plugin,"tntanon"));
        new fish(         new NamespacedKey(plugin,"fish"));
        new poshot(       new NamespacedKey(plugin,"poshot"));
        new steveoshot(   new NamespacedKey(plugin,"steveoshot"));
        new antiholy(     new NamespacedKey(plugin,"antiholy"));
        new neverended(   new NamespacedKey(plugin,"neverended"));
        new democracy(    new NamespacedKey(plugin,"democracy"));
        new telepathy(    new NamespacedKey(plugin,"telepathy"));
        new headless(     new NamespacedKey(plugin,"headless"));
        new axicia(       new NamespacedKey(plugin,"axicia"));
        new log(          new NamespacedKey(plugin,"log"));
        new breakerOrIsIt(new NamespacedKey(plugin,"breakerorisit"));
        new stunning(     new NamespacedKey(plugin,"stunning"));
        new myolner(      new NamespacedKey(plugin,"myolner"));

        loadConfig();

        getServer().getPluginManager().registerEvents(new fishing(),this);
        getServer().getPluginManager().registerEvents(new anvil(),this);

        getCommand("dfchants").setExecutor(new dfchants(this));
        getCommand("dfchants").setTabCompleter(new dfchants(this));
    }

    @Override
    public void onDisable() {
        for (CEnchantment ench : CEnchantments) {
            ench.onDisable();
        }
        CEnchantments = new ArrayList<>();
    }

    public void loadConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }
        config = null;
        config = YamlConfiguration.loadConfiguration(configFile);
        boolean updated = false;
        for (CEnchantment enchantment : CEnchantments) {
            String rootKey = enchantment.getId().getKey();
            Map<String, Object> defaults = enchantment.getDefaultConfig();
            if (!config.isConfigurationSection(rootKey)) {
                if (defaults != null) {
                    for (Map.Entry<String, Object> entry : defaults.entrySet()) {
                        config.set(rootKey + "." + entry.getKey(), entry.getValue());
                    }
                }
                updated = true;
                continue;
            }
            if (defaults != null) {
                for (Map.Entry<String, Object> entry : defaults.entrySet()) {
                    String fullKey = rootKey + "." + entry.getKey();
                    if (!config.contains(fullKey)) {
                        config.set(fullKey, entry.getValue());
                        updated = true;
                    }
                }
            }
        }
        if (updated) {
            try {
                config.save(configFile);
            } catch (IOException ex) {
                getLogger().warning("Failed to save default enchantment config: " + ex.getMessage());
            }
        }
        allBiomes = new HashSet<>();
        for (CEnchantment enchantment : CEnchantments) {
            allBiomes.addAll(enchantment.getBiomes().stream().map(Registry.BIOME::get).toList());
        }
    }

    public static boolean isEnchantment(CEnchantment enchantment, String key) {
        return enchantment != null && enchantment.getId() != null
                && enchantment.getId().getKey().equals(key);
    }
}
