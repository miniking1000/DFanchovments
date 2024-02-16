package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.pythonchik.dfanchovments.CEnchantment;
import org.pythonchik.dfanchovments.DFanchovments;

import java.util.ArrayList;
import java.util.List;

public class holyexcalibruh extends CEnchantment implements Listener {

    DFanchovments plugin = (DFanchovments) Bukkit.getPluginManager().getPlugin("DFanchovments");

    public holyexcalibruh(NamespacedKey id) {
        super(id);
    }

    public NamespacedKey getId(){
        return new NamespacedKey(plugin,"holyexcalibruh");
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event){
        if (!event.getDamager().getType().equals(EntityType.PLAYER))return;
        Player damager = (Player) event.getDamager();
        if (!damager.getInventory().getItemInMainHand().containsEnchantment(this)) return;
        if (!damager.getPersistentDataContainer().has(new NamespacedKey(plugin,"holycd"), PersistentDataType.INTEGER) || damager.getPersistentDataContainer().get(new NamespacedKey(plugin, "holycd"), PersistentDataType.INTEGER) <= 0) {
            Entity entity = event.getEntity();
            Firework firework = (Firework) entity.getLocation().getWorld().spawnEntity(entity.getLocation().add(0,3.1,0), EntityType.FIREWORK);
            FireworkMeta firemeta = firework.getFireworkMeta();
            firemeta.setPower(3);
            firework.setMaxLife(20);
            firework.setLife(20);

            firework.addPassenger(entity);
            damager.playSound(entity,Sound.ENTITY_ENDER_DRAGON_HURT,1,1);

            firemeta.addEffects(FireworkEffect.builder().flicker(false).trail(false).withColor(Color.BLACK).withFade(Color.BLACK).with(FireworkEffect.Type.BALL_LARGE).build(),
                    FireworkEffect.builder().flicker(false).trail(false).withColor(Color.BLACK).withFade(Color.BLACK).with(FireworkEffect.Type.BALL).build(),
                    FireworkEffect.builder().flicker(false).trail(false).withColor(Color.BLACK).withFade(Color.BLACK).with(FireworkEffect.Type.STAR).build());
            firework.setSilent(true);
            firework.setFireworkMeta(firemeta);
            entity.setGlowing(true);
            damager.getPersistentDataContainer().set(new NamespacedKey(plugin, "holycd"), PersistentDataType.INTEGER, 300);
            startCD(damager);

            BukkitTask runtask = new BukkitRunnable() {
                int i = 1;
                @Override
                public void run() {
                    i++;
                    firework.setVelocity(new Vector().setY(i*0.3));
                    entity.setVelocity(new Vector().setY(i*0.3));
                }
            }.runTaskTimer(plugin, 0, 1);

            new BukkitRunnable() {
                @Override
                public void run() {
                    entity.setGlowing(false);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as " + entity.getUniqueId() + " run kill @s");
                    runtask.cancel();

                }
            }.runTaskLater(plugin, 20);


        } else {
            if (damager.getWorld().getGameRuleValue(GameRule.LOG_ADMIN_COMMANDS)) {
                damager.getWorld().setGameRule(GameRule.LOG_ADMIN_COMMANDS, false);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as " + damager.getUniqueId() + String.format(" run title @s actionbar {\"text\":\"Подожди еще %d сек. перед использованием\",\"color\":\"gold\"}", damager.getPersistentDataContainer().get(new NamespacedKey(plugin, "holycd"), PersistentDataType.INTEGER)));
                damager.getWorld().setGameRule(GameRule.LOG_ADMIN_COMMANDS, true);
            } else{
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as " + damager.getUniqueId() + String.format(" run title @s actionbar {\"text\":\"Подожди еще %d сек. перед использованием\",\"color\":\"gold\"}", damager.getPersistentDataContainer().get(new NamespacedKey(plugin, "holycd"), PersistentDataType.INTEGER)));
            }
            //damager.sendMessage(ChatColor.translateAlternateColorCodes('&',String.format("Необходимо подождать еще %d секунд перед использованием способности",damager.getPersistentDataContainer().get(new NamespacedKey(plugin,"holycd"),PersistentDataType.INTEGER))));
            //notify user about CD
        }
    }


    @EventHandler
    public void opj(PlayerJoinEvent event) {
        if (event.getPlayer().getScoreboard().getObjective("holycd").getScore(event.getPlayer()).getScore() >0) {
            if (event.getPlayer().getPersistentDataContainer().has(new NamespacedKey(plugin,"holycd"),PersistentDataType.INTEGER)){
                event.getPlayer().getPersistentDataContainer().set(new NamespacedKey(plugin,"holycd"),PersistentDataType.INTEGER, (event.getPlayer().getPersistentDataContainer().get(new NamespacedKey(plugin,"holycd"),PersistentDataType.INTEGER)-event.getPlayer().getScoreboard().getObjective("holycd").getScore(event.getPlayer()).getScore()));
                event.getPlayer().getScoreboard().getObjective("holycd").getScore(event.getPlayer()).setScore(0);
            }
        }
        if (event.getPlayer().getPersistentDataContainer().has(new NamespacedKey(plugin,"holycd"),PersistentDataType.INTEGER) && event.getPlayer().getPersistentDataContainer().get(new NamespacedKey(plugin,"holycd"),PersistentDataType.INTEGER) > 0) {
            startCD(event.getPlayer());
        }
    }

    public void startCD(Player damager){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (damager.isOnline()) {
                    if (damager.getPersistentDataContainer().get(new NamespacedKey(plugin, "holycd"), PersistentDataType.INTEGER) > 0) {
                        damager.getPersistentDataContainer().set(new NamespacedKey(plugin, "holycd"), PersistentDataType.INTEGER, damager.getPersistentDataContainer().get(new NamespacedKey(plugin, "holycd"), PersistentDataType.INTEGER) - 1);
                    } else {
                        this.cancel();
                    }
                } else {
                    if (damager.getScoreboard().getObjective("holycd").getScore(damager).getScore() < 300){
                        damager.getScoreboard().getObjective("holycd").getScore(damager).setScore(1+damager.getScoreboard().getObjective("holycd").getScore(damager).getScore());
                    } else{
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }
    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("GOLDEN_SWORD");
        return retu;
    }
    @Override
    public String getName() {
        return DFanchovments.getConfig1().getString("holyexcalibruh.name");
    }

    @Override
    public int getMaxLevel() {
        return 1;
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
    public boolean conflictsWith(@NotNull CEnchantment other) {
        return false;
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return false;

    }
}
