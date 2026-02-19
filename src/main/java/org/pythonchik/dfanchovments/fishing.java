package org.pythonchik.dfanchovments;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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
        double luck = 0;
        ItemMeta meta = rod.getItemMeta();
        //Add enchantment luck
        if (meta != null) {
            if (meta.hasEnchant(Enchantment.LUCK_OF_THE_SEA)) {
                luck += meta.getEnchantLevel(Enchantment.LUCK_OF_THE_SEA);
            }
        }

        //Add attribute luck
        AttributeInstance attr = player.getAttribute(Attribute.LUCK);
        if (attr != null) {
            luck += attr.getValue();
        }

        CEnchantment candidate = null;
        for (CEnchantment enchantmentCandidate : DFanchovments.CEnchantments) {
            if (!enchantmentCandidate.isInBiome(biome)) {
                // we cant fish it here.
                continue;
            }
            if (candidate == null) {
                // we did not yet catch anything - just try and if yes - good!
                if (enchantmentCandidate.roll(luck)) {
                    // success !
                    candidate = enchantmentCandidate;
                }
            } else {
                // we DID fish something, but now, maybe, we can fish bigger!
                if (candidate.getDropChance(luck) < enchantmentCandidate.getDropChance(luck)
                        && enchantmentCandidate.roll(luck)) {
                    // we caught something bigger!
                    candidate = enchantmentCandidate;
                }
            }
        }
        if (candidate != null) {
            item.setItemStack(candidate.createRandomBook(luck));
        }
    }
}
