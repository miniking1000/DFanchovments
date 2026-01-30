package org.pythonchik.dfanchovments;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.pythonchik.dfanchovments.Enchantments.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;


public final class DFanchovments extends JavaPlugin {
    private static FileConfiguration config;
    public static DFanchovments plugin;
    public static ArrayList<CEnchantment> CEnchantments = new ArrayList<>();
    public static exp exp;
    public static soulbound soulbound;
    public static rain rain;
    public static shockwave shockwave;
    public static fireworks fireworks;
    public static potioness potioness;
    public static dodge dodge;
    public static bamboom bamboom;
    public static tntanon tntanon;
    public static fish fish;
    public static poshot poshot;
    public static steveoshot steveoshot;
    public static antiholy antiholy;
    public static neverended neverended;
    public static democracy democracy;
    public static telepathy telepathy;
    public static headless headless;
    public static axicia axicia;
    public static log log;
    public static breakerOrIsIt breakerOrIsIt;
    public static stunning stunning;
    public static myolner myolner;

    //add a line to ench

    public static Message message;
    public static Message getMessage(){return message;}

    public void reload5(){
        loadConfig();
    }
    public static FileConfiguration getConfig1(){
        return config;
    }

    @Override
    public void onEnable() {
        message = new Message(this);
        plugin = this;

        soulbound = new soulbound(new NamespacedKey(plugin,"soulbound"));
        shockwave = new shockwave(new NamespacedKey(plugin,"shockwave"));
        fireworks = new fireworks(new NamespacedKey(plugin,"fireworks"));
        potioness = new potioness(new NamespacedKey(plugin,"potioness"));
        dodge = new dodge(new NamespacedKey(plugin, "dodge"));
        rain = new rain(new NamespacedKey(plugin,"rain"));
        exp = new exp(new NamespacedKey(plugin,"exp"));
        bamboom = new bamboom(new NamespacedKey(plugin,"bamboom"));
        tntanon = new tntanon(new NamespacedKey(plugin,"tntanon"));
        fish = new fish(new NamespacedKey(plugin,"fish"));
        poshot = new poshot(new NamespacedKey(plugin,"poshot"));
        steveoshot = new steveoshot(new NamespacedKey(plugin,"steveoshot"));
        antiholy = new antiholy(new NamespacedKey(plugin,"antiholy"));
        neverended = new neverended(new NamespacedKey(plugin,"neverended"));
        democracy = new democracy(new NamespacedKey(plugin, "democracy"));
        telepathy = new telepathy(new NamespacedKey(plugin, "telepathy"));
        headless = new headless(new NamespacedKey(plugin, "headless"));
        axicia = new axicia(new NamespacedKey(plugin, "axicia"));
        log = new log(new NamespacedKey(plugin, "log"));
        breakerOrIsIt = new breakerOrIsIt(new NamespacedKey(plugin, "breakerorisit"));
        stunning = new stunning(new NamespacedKey(plugin, "stunning"));
        myolner = new myolner(new NamespacedKey(plugin, "myolner"));
        //add a line to ench

        CEnchantments.add(soulbound);
        CEnchantments.add(shockwave);
        CEnchantments.add(fireworks);
        CEnchantments.add(potioness);
        CEnchantments.add(dodge);
        CEnchantments.add(exp);
        CEnchantments.add(rain);
        CEnchantments.add(bamboom);
        CEnchantments.add(tntanon);
        CEnchantments.add(fish);
        CEnchantments.add(poshot);
        CEnchantments.add(steveoshot);
        CEnchantments.add(antiholy);
        CEnchantments.add(neverended);
        CEnchantments.add(democracy);
        CEnchantments.add(telepathy);
        CEnchantments.add(headless);
        CEnchantments.add(axicia);
        CEnchantments.add(log);
        CEnchantments.add(breakerOrIsIt);
        CEnchantments.add(stunning);
        CEnchantments.add(myolner);
        //add a lint to ench

        loadConfig();


        getServer().getPluginManager().registerEvents(new fishing(),this);
        getServer().getPluginManager().registerEvents(new anvil(),this);

        getServer().getPluginManager().registerEvents(soulbound,this);
        getServer().getPluginManager().registerEvents(shockwave,this);
        getServer().getPluginManager().registerEvents(fireworks,this);
        getServer().getPluginManager().registerEvents(potioness,this);
        getServer().getPluginManager().registerEvents(dodge,this);
        getServer().getPluginManager().registerEvents(exp,this);
        getServer().getPluginManager().registerEvents(rain,this);
        getServer().getPluginManager().registerEvents(bamboom,this);
        getServer().getPluginManager().registerEvents(tntanon,this);
        getServer().getPluginManager().registerEvents(fish,this);
        getServer().getPluginManager().registerEvents(poshot,this);
        getServer().getPluginManager().registerEvents(steveoshot,this);
        getServer().getPluginManager().registerEvents(antiholy,this);
        getServer().getPluginManager().registerEvents(neverended,this);
        getServer().getPluginManager().registerEvents(democracy,this);
        getServer().getPluginManager().registerEvents(telepathy, this);
        getServer().getPluginManager().registerEvents(headless, this);
        getServer().getPluginManager().registerEvents(axicia, this);
        getServer().getPluginManager().registerEvents(log, this);
        getServer().getPluginManager().registerEvents(breakerOrIsIt, this);
        getServer().getPluginManager().registerEvents(stunning, this);
        getServer().getPluginManager().registerEvents(myolner, this);

        //any listeners to ench

        getCommand("dfchants").setExecutor(new dfchants(this));
        getCommand("dfchants").setTabCompleter(new dfchants(this));
    }

    @Override
    public void onDisable() {
        CEnchantments = new ArrayList<>();
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
