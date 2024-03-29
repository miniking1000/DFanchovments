package org.pythonchik.dfanchovments;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;
import org.pythonchik.dfanchovments.Enchantments.*;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

public final class DFanchovments extends JavaPlugin {
    private static FileConfiguration config;
    static DFanchovments plugin;
    public static ArrayList<CEnchantment> CEnchantments = new ArrayList<>();
    public static exp exp;
    public static soulbreak soulbreak;
    public static soulbound soulbound;
    public static rain rain;
    public static shockwave shockwave;
    public static fireworks fireworks;
    public static vampire vampire;
    public static potioness potioness;
    public static dodge dodge;
    public static bamboom bamboom;
    public static godkiller godkiller;
    public static tntanon tntanon;
    public static fish fish;
    public static poshot poshot;
    public static grib grib;
    public static holyexcalibruh holyexcalibruh;

    //add a line to ench

    public static Message message;
    public static Message getMessage(){return message;}

    public void reload5(){
        Bukkit.getPluginManager().disablePlugin(plugin);
        Bukkit.getPluginManager().enablePlugin(plugin);
    }
    public static FileConfiguration getConfig1(){
        return config;
    }

    @Override
    public void onEnable() {
        message = new Message(this);
        plugin = this;

        soulbreak = new soulbreak(new NamespacedKey(plugin,"soulbreak"));
        soulbound = new soulbound(new NamespacedKey(plugin,"soulbound"));
        shockwave = new shockwave(new NamespacedKey(plugin,"shockwave"));
        fireworks = new fireworks(new NamespacedKey(plugin,"fireworks"));
        vampire = new vampire(new NamespacedKey(plugin, "vampire"));
        potioness = new potioness(new NamespacedKey(plugin,"potioness"));
        dodge = new dodge(new NamespacedKey(plugin, "dodge"));
        rain = new rain(new NamespacedKey(plugin,"rain"));
        exp = new exp(new NamespacedKey(plugin,"exp"));
        bamboom = new bamboom(new NamespacedKey(plugin,"bamboom"));
        godkiller = new godkiller(new NamespacedKey(plugin,"godkiller"));
        tntanon = new tntanon(new NamespacedKey(plugin,"tntanon"));
        fish = new fish(new NamespacedKey(plugin,"fish"));
        poshot = new poshot(new NamespacedKey(plugin,"poshot"));
        grib = new grib(new NamespacedKey(plugin,"grib"));
        holyexcalibruh = new holyexcalibruh(new NamespacedKey(plugin,"holyexcalibruh"));
        //add a line to ench

        CEnchantments.add(soulbreak);
        CEnchantments.add(soulbound);
        CEnchantments.add(shockwave);
        CEnchantments.add(fireworks);
        CEnchantments.add(vampire);
        CEnchantments.add(potioness);
        CEnchantments.add(dodge);
        CEnchantments.add(exp);
        CEnchantments.add(rain);
        CEnchantments.add(bamboom);
        CEnchantments.add(godkiller);
        CEnchantments.add(tntanon);
        CEnchantments.add(fish);
        CEnchantments.add(poshot);
        CEnchantments.add(grib);
        CEnchantments.add(holyexcalibruh);
        //add a lint to ench

        loadConfig();
        LoadEnchantments();


        getServer().getPluginManager().registerEvents(new fishing(),this);
        getServer().getPluginManager().registerEvents(new anvil(),this);

        getServer().getPluginManager().registerEvents(soulbound,this);
        getServer().getPluginManager().registerEvents(shockwave,this);
        getServer().getPluginManager().registerEvents(fireworks,this);
        getServer().getPluginManager().registerEvents(vampire,this);
        getServer().getPluginManager().registerEvents(potioness,this);
        getServer().getPluginManager().registerEvents(dodge,this);
        getServer().getPluginManager().registerEvents(exp,this);
        getServer().getPluginManager().registerEvents(rain,this);
        getServer().getPluginManager().registerEvents(bamboom,this);
        getServer().getPluginManager().registerEvents(godkiller,this);
        getServer().getPluginManager().registerEvents(tntanon,this);
        getServer().getPluginManager().registerEvents(fish,this);
        getServer().getPluginManager().registerEvents(poshot,this);
        getServer().getPluginManager().registerEvents(grib,this);
        getServer().getPluginManager().registerEvents(holyexcalibruh,this);
        //any listeners to ench

        getCommand("dfchants").setExecutor(new dfchants(config,this));
        getCommand("dfchants").setTabCompleter(new dfchants(config,this));
    }

    @Override
    public void onDisable() {
        CEnchantments = new ArrayList<>();
    }
    private void LoadEnchantments(){
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Enchantment.registerEnchantment(soulbound);
            Enchantment.registerEnchantment(soulbreak);
            Enchantment.registerEnchantment(shockwave);
            Enchantment.registerEnchantment(fireworks);
            Enchantment.registerEnchantment(vampire);
            Enchantment.registerEnchantment(potioness);
            Enchantment.registerEnchantment(dodge);
            Enchantment.registerEnchantment(exp);
            Enchantment.registerEnchantment(rain);
            Enchantment.registerEnchantment(bamboom);
            Enchantment.registerEnchantment(godkiller);
            Enchantment.registerEnchantment(tntanon);
            Enchantment.registerEnchantment(fish);
            Enchantment.registerEnchantment(poshot);
            Enchantment.registerEnchantment(grib);
            Enchantment.registerEnchantment(holyexcalibruh);
            //add line to ench
        }
        catch (IllegalArgumentException e){
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }
        config = null;
        config = YamlConfiguration.loadConfiguration(configFile);
    }
}
