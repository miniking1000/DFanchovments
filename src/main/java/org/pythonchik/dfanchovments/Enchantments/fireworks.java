package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.pythonchik.dfanchovments.CEnchantment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class fireworks extends CEnchantment implements Listener {

    DFanchovments plugin = (DFanchovments) Bukkit.getPluginManager().getPlugin("DFanchovments");

    @EventHandler
    private void HAHAH(ProjectileHitEvent event){
        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }
        if (((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta() == null){
            return;
        }
        if (((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta().hasEnchant(DFanchovments.fireworks)) {
            if (Math.random() <= ((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta().getEnchants().get(this).intValue()*0.25){
                final Collection<Entity> list = event.getEntity().getWorld().getNearbyEntities(event.getEntity().getLocation(), 2, 2, 2);
                for (final Entity entity : list)
                {
                    if (!(entity instanceof Damageable))
                        continue;
                    EntityDamageEvent e = new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.PROJECTILE, 0);
                    Bukkit.getPluginManager().callEvent(e);
                    if (e.isCancelled())
                        continue;

                    Damageable le = ((Damageable) entity);
                    event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), 0F);
                    le.damage(((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand().getItemMeta().getEnchants().get(this).intValue()*2);
                }
            }
        }
    }

    public fireworks(NamespacedKey id) {
        super(id);
    }

    public NamespacedKey getId(){
        return new NamespacedKey(plugin,"fireworks");
    }
    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("BOW");
        return retu;
    }
    @Override
    public String getName() {
        return DFanchovments.getConfig1().getString("fireworks.name");
    }

    @Override
    public int getMaxLevel() {
        return 3;
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
}
