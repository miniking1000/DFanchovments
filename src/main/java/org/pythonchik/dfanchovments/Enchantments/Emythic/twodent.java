package org.pythonchik.dfanchovments.Enchantments.Emythic;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.pythonchik.dfanchovments.Bosses;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class twodent extends CEnchantment implements Listener {

    public twodent(NamespacedKey id) {
        super(id);
    }

    @Override
    public List<String> getTragers() {
        List<String> retu = new ArrayList<>();
        retu.add(Material.ENCHANTED_BOOK.name());
        retu.add(Material.ELYTRA.name());
        return retu;
    }

    @EventHandler
    public void OnElytra(EntityToggleGlideEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!event.isGliding()) {
                return;
            }
            ItemStack chestplate = player.getInventory().getChestplate();
            if (chestplate == null) {
                return;
            }
            ItemMeta meta = chestplate.getItemMeta();
            if (meta == null) {
                return;
            }
            if (meta.getPersistentDataContainer().has(this.getId(), PersistentDataType.INTEGER)) {
                int level = meta.getPersistentDataContainer().get(this.getId(), PersistentDataType.INTEGER);
                float f7 = player.getLocation().getPitch();
                float f = player.getLocation().getYaw();
                float f1 = (float) (-Math.sin(f7 * ((float) Math.PI / 180F)) * Math.cos(f * ((float) Math.PI / 180F)));
                float f2 = (float) -Math.sin(f * ((float) Math.PI / 180F));
                float f3 = (float) (Math.cos(f7 * ((float) Math.PI / 180F)) * Math.cos(f * ((float) Math.PI / 180F)));
                float f4 = (float) Math.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
                float f5 = 3.0F * ((1.0F + (float) level) / 4.0F);
                f1 *= f5 / f4;
                f2 *= f5 / f4;
                f3 *= f5 / f4;
                Vector move = player.getVelocity();
                move.add(new Vector((double) f1, (double) f2, (double) f3));
                player.setVelocity(move);
                player.startRiptideAttack(10 + (10 * level), 0, chestplate);
            }
        }
    }

    @Override
    public NamespacedKey getId() {
        return this.id;
    }

    @Override
    public Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("name", "#6495EDЛетун");
        defaults.put("biomes", List.of("SULFUR_CAVES"));
        defaults.put("chance", 0.0007);
        defaults.put("luck", 0);
        defaults.put("maxlvl", 1);

        Map<String, Object> bosses = new LinkedHashMap<>();
        bosses.put(Bosses.Rarity.MYTHIC.getName(), 10);

        defaults.put("bosses", bosses);
        return defaults;
    }
}
