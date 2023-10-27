package org.pythonchik.dfanchovments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;
import org.pythonchik.dfanchovments.Enchantments.Arrain;
import org.pythonchik.dfanchovments.Enchantments.XPEnch;
import org.pythonchik.dfanchovments.Enchantments.soulbound;
import org.pythonchik.dfanchovments.Enchantments.soulbreak;

import java.lang.reflect.Field;

public final class DFanchovments extends JavaPlugin {
    static DFanchovments plugin;
    public static org.pythonchik.dfanchovments.Enchantments.XPEnch XPEnch;
    public static soulbreak antisoul;
    public static soulbound soul;
    public static Arrain ARrain;


    public static Message message;
    public static Message getMessage(){return message;}

    @Override
    public void onEnable() {
        message = new Message(this);
        plugin = this;

        XPEnch = new XPEnch(new NamespacedKey(plugin,"XPEnch"));
        antisoul = new soulbreak(new NamespacedKey(plugin,"soulbreak"));
        soul = new soulbound(new NamespacedKey(plugin,"soulbound"));
        ARrain = new Arrain(new NamespacedKey(plugin,"ARrain"));

        LoadEnchantments();
        getServer().getPluginManager().registerEvents(soul,this);
        getServer().getPluginManager().registerEvents(XPEnch,this);
        getServer().getPluginManager().registerEvents(ARrain,this);
        getCommand("testcom").setExecutor(new testcom());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
            Enchantment.registerEnchantment(soul);
            Enchantment.registerEnchantment(antisoul);
            Enchantment.registerEnchantment(XPEnch);
            Enchantment.registerEnchantment(ARrain);
        }
        catch (IllegalArgumentException e){
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
