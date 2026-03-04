package org.pythonchik.dfanchovments;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class fishing implements Listener {

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH)
            return;
        if (event.getHand() == null)
            return; // should never happen
        Player player = event.getPlayer();
        ItemStack rod = player.getInventory().getItem(event.getHand());
        if (rod == null)
            return;
        Biome biome = player.getWorld().getBiome(player.getLocation());
        if (!DFanchovments.allBiomes.contains(biome))
            return; // no enchants has this biome, so just skip everything
        Entity entity = event.getCaught();
        if (!(entity instanceof Item item))
            return;
        ItemStack baseCaught = item.getItemStack();
        if (baseCaught.getType().isAir()) return;
        int rolls = Math.max(1, baseCaught.getAmount());

        double luck = 0.0;

        //Add enchantment luck
        ItemMeta meta = rod.getItemMeta();
        if (meta != null && meta.hasEnchant(Enchantment.LUCK_OF_THE_SEA)) {
            luck += meta.getEnchantLevel(Enchantment.LUCK_OF_THE_SEA);
        }

        //Add attribute luck
        AttributeInstance attr = player.getAttribute(Attribute.LUCK);
        if (attr != null) {
            luck += attr.getValue();
        }

        // Для случая "кандидата нет": мы не хотим оставить amount=N (иначе будут N рыб в одном стаке).
        // Раз уж мы "считаем" N как N попыток — базовый предмет тоже нужно распилить по 1 штуке.
        ItemStack fallbackOne = baseCaught.clone();
        fallbackOne.setAmount(1);

        // Первая попытка — в текущий Item entity
        ItemStack firstDrop = rollOnceOrFallback(biome, luck, fallbackOne);
        item.setItemStack(firstDrop);

        // Остальные попытки — отдельными Item entity
        if (rolls > 1) {
            var world = item.getWorld();
            var loc = item.getLocation();

            for (int i = 1; i < rolls; i++) {
                ItemStack extraDrop = rollOnceOrFallback(biome, luck, fallbackOne);
                Item spawned = world.dropItemNaturally(loc, extraDrop);
                spawned.setPickupDelay(item.getPickupDelay());
                spawned.setOwner(item.getOwner());
                item.addPassenger(spawned);
            }
        }
    }

    private ItemStack rollOnceOrFallback(Biome biome, double luck, ItemStack fallbackOne) {
        CEnchantment candidate = pickCandidate(biome, luck);
        if (candidate != null) {
            return candidate.createRandomBook(luck);
        }
        return fallbackOne.clone();
    }

    private CEnchantment pickCandidate(Biome biome, double luck) {
        CEnchantment candidate = null;

        for (CEnchantment enchantmentCandidate : DFanchovments.CEnchantments) {
            if (!enchantmentCandidate.isInBiome(biome)) continue;

            if (candidate == null) {
                if (enchantmentCandidate.roll(luck)) {
                    candidate = enchantmentCandidate;
                }
            } else {
                if (candidate.getDropChance(luck) < enchantmentCandidate.getDropChance(luck)
                        && enchantmentCandidate.roll(luck)) {
                    candidate = enchantmentCandidate;
                }
            }
        }
        return candidate;
    }
}
