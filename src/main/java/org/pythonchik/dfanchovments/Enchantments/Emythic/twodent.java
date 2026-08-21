package org.pythonchik.dfanchovments.Enchantments.Emythic;

import net.minecraft.util.Mth;
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

                float yRot = player.getLocation().getYaw();
                float xRot = player.getLocation().getPitch();
                float xd = -Mth.sin(yRot * Mth.DEG_TO_RAD) * Mth.cos(xRot * Mth.DEG_TO_RAD);
                float yd = -Mth.sin(xRot * Mth.DEG_TO_RAD);
                float zd = Mth.cos(yRot * Mth.DEG_TO_RAD) * Mth.cos(xRot * Mth.DEG_TO_RAD);
                float dist = Mth.sqrt(xd * xd + yd * yd + zd * zd);
                xd *= level / dist;
                yd *= level / dist;
                zd *= level / dist;


                Vector move = player.getVelocity();
                move.add(new Vector((double) xd, (double) yd, (double) zd));
                player.setVelocity(move);

                player.startRiptideAttack(10 + (10 * level), 0, chestplate);
            }
        }
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
