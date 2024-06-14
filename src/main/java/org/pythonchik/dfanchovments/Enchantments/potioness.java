package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.pythonchik.dfanchovments.CEnchantment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.List;

public class potioness extends CEnchantment implements Listener {

    DFanchovments plugin = (DFanchovments) Bukkit.getPluginManager().getPlugin("DFanchovments");

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){
        if (!(event.getEntity() instanceof Player)){
            return;
        }
        if (!(event.getDamager() instanceof Player)){
            return;
        }
        int lvl = 0;
        if (((Player) event.getEntity()).getInventory().getHelmet() != null){
            if (((Player) event.getEntity()).getInventory().getHelmet().getItemMeta().hasEnchant(DFanchovments.potioness)){
                lvl = lvl +((Player) event.getEntity()).getInventory().getHelmet().getItemMeta().getEnchants().get(DFanchovments.potioness).intValue();
            }
        }
        if (((Player) event.getEntity()).getInventory().getChestplate() != null){
            if (((Player) event.getEntity()).getInventory().getChestplate().getItemMeta().hasEnchant(DFanchovments.potioness)){
                lvl = lvl + ((Player) event.getEntity()).getInventory().getChestplate().getItemMeta().getEnchants().get(DFanchovments.potioness).intValue();
            }
        }
        if (((Player) event.getEntity()).getInventory().getLeggings() != null){
            if (((Player) event.getEntity()).getInventory().getLeggings().getItemMeta().hasEnchant(DFanchovments.potioness)){
                lvl = lvl + ((Player) event.getEntity()).getInventory().getLeggings().getItemMeta().getEnchants().get(DFanchovments.potioness).intValue();
            }
        }
        if (((Player) event.getEntity()).getInventory().getBoots() != null){
            if (((Player) event.getEntity()).getInventory().getBoots().getItemMeta().hasEnchant(DFanchovments.potioness)){
                lvl = lvl +((Player) event.getEntity()).getInventory().getBoots().getItemMeta().getEnchants().get(DFanchovments.potioness).intValue();
            }
        }
        if (lvl >0) {
            ((Player) event.getDamager()).addPotionEffect(new PotionEffect(PotionEffectType.POISON,25*lvl,lvl==1?0:lvl==2||lvl==3? 1 : lvl==4||lvl==5||lvl==6? 2 : lvl==7||lvl==8||lvl==9||lvl==10 ? 4 : lvl==11||lvl==12||lvl==13||lvl==14||lvl==15 ? 6 : 7));
        }
    }

    public potioness(NamespacedKey id) {
        super(id);
    }

    public NamespacedKey getId(){
        return new NamespacedKey(plugin,"potioness");
    }
    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        //hellmets
        retu.add("LEATHER_HELMET");
        retu.add("CHAINMAIL_HELMET");
        retu.add("IRON_HELMET");
        retu.add("DIAMOND_HELMET");
        retu.add("GOLDEN_HELMET");
        retu.add("NETHERITE_HELMET");
        //chestplates
        retu.add("LEATHER_CHESTPLATE");
        retu.add("CHAINMAIL_CHESTPLATE");
        retu.add("IRON_CHESTPLATE");
        retu.add("DIAMOND_CHESTPLATE");
        retu.add("GOLDEN_CHESTPLATE");
        retu.add("NETHERITE_CHESTPLATE");
        //laggings
        retu.add("LEATHER_LEGGINGS");
        retu.add("CHAINMAIL_LEGGINGS");
        retu.add("IRON_LEGGINGS");
        retu.add("DIAMOND_LEGGINGS");
        retu.add("GOLDEN_LEGGINGS");
        retu.add("NETHERITE_LEGGINGS");
        //bots
        retu.add("LEATHER_BOOTS");
        retu.add("CHAINMAIL_BOOTS");
        retu.add("IRON_BOOTS");
        retu.add("DIAMOND_BOOTS");
        retu.add("GOLDEN_BOOTS");
        retu.add("NETHERITE_BOOTS");
        return retu;
    }
    @Override
    public String getName() {
        return DFanchovments.getConfig1().getString("potioness.name");
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment other) {
        return false;
    }

    @Override
    public boolean conflictsWith(CEnchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return false;

    }

    @NotNull
    @Override
    public String getTranslationKey() {
        return null;
    }
}
