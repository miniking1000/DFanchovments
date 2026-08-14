package org.pythonchik.dfanchovments;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.stream.Stream;

public class Bosses implements Listener {

    static Long CDtimestamp = 0L;
    static final NamespacedKey rarityKey = new NamespacedKey(DFanchovments.plugin, "rarity");
    static final Random rng = new Random();
    static HashSet<EntityType> blacklisted = new HashSet<>();
    final Scoreboard scoreboard;
    private final NamespacedKey summonerKey;

    public Bosses() {
        summonerKey = DFanchovments.summonerKey;
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        ArrayList<EntityType> types = new ArrayList<>();
        for (String e : DFanchovments.bossConfig.getStringList("blacklisted")) {
            try {
                types.add(EntityType.valueOf(e));
            } catch (Exception ignored) {
                DFanchovments.plugin.getLogger().warning("Failed to get EntityType to blacklist from: " + e);
            }
        }
        blacklisted = new HashSet<>(types);
        if (manager == null) {
            System.out.println("WARNING!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!WHY are bosses loading before every world? no scoreboards to take!");
            scoreboard = null;
        } else {
            scoreboard = manager.getMainScoreboard();
            for (Rarity rarity : Rarity.ALL) {
                Team team = scoreboard.getTeam(rarity.name);
                if (team == null) {
                    scoreboard.registerNewTeam(rarity.name);
                    team = scoreboard.getTeam(rarity.name);
                    assert team != null;
                    team.setColor(rarity.getColor());
                    team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
                    team.setOption(Team.Option.DEATH_MESSAGE_VISIBILITY, Team.OptionStatus.ALWAYS);
                }
            }

        }
    }

    public enum Rarity {
        UNKNOWN("unknown", 0),
        COMMON("common", 1),
        UNCOMMON("uncommon", 2),
        RARE("rare", 3),
        EPIC("epic", 4),
        LEGENDARY("legendary", 5),
        MYTHIC("mythic", 6);
        // higher the number -> higher the priority to spawn, even if chances say otherwise. ?
        public static final List<Rarity> ALL = Stream.of(COMMON, UNCOMMON, RARE, EPIC, LEGENDARY, MYTHIC).sorted(Comparator.comparingInt(Rarity::getNumber).reversed()).toList();

        private final String name;
        private final int number;
        private ConfigurationSection config;

        Rarity(String name, int number) {
            this.name = name;
            this.number = number;
            this.config = DFanchovments.bossConfig.getConfigurationSection("rarities." + name);
        }

        public void updateConfig() {
            this.config = DFanchovments.bossConfig.getConfigurationSection("rarities." + name);
        }

        public String getName() {
            return name;
        }

        public int getNumber() {
            return number;
        }

        public boolean isAccepted(double rolled) {
            return config.getDouble("chance") >= rolled;
        }

        public boolean isAcceptedSummon(double rolled) {
            return config.getDouble("chanceSummoned") >= rolled;
        }

        public boolean shouldDrop(double rolled, double luck) {
            return config.getDouble("dropChance", 0) + luck * config.getDouble("luck", 0) >= rolled;
        }

        public String pickName(boolean isMale) {
            ConfigurationSection selection = config.getConfigurationSection("names");
            if (selection == null) return "";
            List<String> all = selection.getStringList(isMale ? "male" : "female").stream().filter(s -> !s.isBlank()).toList();
            if (all.isEmpty()) return "";
            String picked = all.get(rng.nextInt(0, all.size()));
            return DFanchovments.message.hex(config.getString("hexColor", "#FFFFFF") + picked);
        }

        public ChatColor getColor() {
            String colorString = config.getString("color", "black").toUpperCase();
            ChatColor color = ChatColor.BLACK;
            try {
                ChatColor tempColor = ChatColor.valueOf(colorString);
                if (tempColor.isColor()) {
                    color = tempColor;
                } else {
                    DFanchovments.plugin.getLogger().warning("Color '" + colorString + "' from rarity '" + name + "' is not a color actually!");
                }
            } catch (Exception e) {
                DFanchovments.plugin.getLogger().warning("Failed to load color '" + colorString + "' from rarity '" + name + "'!: " + e);
            }

            return color;
        }

        public Map<Attribute, AttributeModifier> getAttributes() {
            ConfigurationSection attrs = config.getConfigurationSection("attributes");
            Map<Attribute, AttributeModifier> output = new HashMap<>();
            if (attrs == null) {
                return output;
            }

            for (String name : attrs.getKeys(false)) {
                Attribute attr = Registry.ATTRIBUTE.get(NamespacedKey.minecraft(name.toLowerCase()));
                if (attr == null) {
                    DFanchovments.plugin.getLogger().warning("Attribute " + name + " was not found, rarity: " + getName());
                    continue;
                }

                ConfigurationSection selection = attrs.getConfigurationSection(name);
                if (selection == null) {
                    DFanchovments.plugin.getLogger().warning("Attribute selection " + name + " is not a selection fix it for rarity: " + getName());
                    continue;
                }

                double amount;
                if (selection.getConfigurationSection("amount") != null) {
                    double min = selection.getDouble("amount.min");
                    double max = selection.getDouble("amount.max");
                    amount = rng.nextDouble(min, max);
                } else {
                    amount = selection.getDouble("amount");
                }
                AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(selection.getString("operation"));

                AttributeModifier modifier = new AttributeModifier(
                        new NamespacedKey(DFanchovments.plugin, UUID.randomUUID().toString()),
                        amount,
                        operation,
                        EquipmentSlotGroup.ANY
                );
                output.put(attr, modifier);
            }
            return output;
        }
    }


    @EventHandler
    public void onSummonerUse(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemMeta meta = event.getItem().getItemMeta();
        if (meta == null) return;
        if (!meta.getPersistentDataContainer().has(summonerKey)) return;
        Collection<Entity> allNearby = player.getWorld().getNearbyEntities(player.getLocation(), 32, 16, 32, e -> {
            return (e instanceof Enemy enemy) && //mob is hostile
                    !blacklisted.contains(enemy.getType()) && // its not blacklisted (small or slimes etc.)
                    !(e instanceof Ageable ageable && !ageable.isAdult()) && // its not a baby
                    !enemy.getPersistentDataContainer().has(rarityKey, PersistentDataType.STRING) &&  // and its not a boss already
                    enemy.hasAI();  // its no on the hook already
        });
        if (allNearby.isEmpty()) {
            DFanchovments.message.sendNoPrefix(player, "&6Не получилось найти сущность для превращения в босса.");
            event.setCancelled(true); // could not pick anyone
            return;
        }
        List<Entity> entities = new ArrayList<>(allNearby);
        Enemy entity = (Enemy) entities.get(rng.nextInt(entities.size()));

        double roll = Math.random() * 100; // % that rolled
        for (Rarity rarity : Rarity.ALL) {
            if (rarity.isAcceptedSummon(roll)) {
                makeIntoBoss(entity, rarity);
                return;
            }
        }
        DFanchovments.message.sendNoPrefix(player, "&6Не получилось выбрать редкость босса.");
        event.setCancelled(true);
    }

    public void makeIntoBoss(Enemy enemy, Rarity rarity) {
        // it IS a boss
        enemy.getPersistentDataContainer().set(rarityKey, PersistentDataType.STRING, rarity.name);
        enemy.getPersistentDataContainer().set(new NamespacedKey("craftbukket", "unpickupable"), PersistentDataType.BOOLEAN, true);
        enemy.setRemoveWhenFarAway(false);

        // custom name
        enemy.setCustomNameVisible(true);
        boolean isMaleName = Math.random() > 0.5;
        String name = rarity.pickName(isMaleName);
        if (name.isBlank() || name.length() <= 2) {
            isMaleName = !isMaleName;
            name = rarity.pickName(isMaleName);
        }
        enemy.setCustomName(name);

        //notification about spawn
        scoreboard.getTeam(rarity.name).addEntry(enemy.getUniqueId().toString());
        enemy.addPotionEffect(PotionEffectType.GLOWING.createEffect(Integer.MAX_VALUE, 1));
        World world = enemy.getWorld();
        Location loc = enemy.getLocation();
        Collection<Entity> players = world.getNearbyEntities(enemy.getLocation(), 32, 64, 32, e -> e.getType().equals(EntityType.PLAYER));
        for (Entity player : players) {
            DFanchovments.message.sendNoPrefix(player, "&6Рядом " + (isMaleName ? "появился " : "появилась ") + name + String.format("&r&6! &7(%.1f %.1f %.1f)", loc.getX(), loc.getY(), loc.getZ()));
            player.getWorld().playSound(player, Sound.BLOCK_END_PORTAL_SPAWN, 0.3f, 1f);
        }

        //attributes
        for (Map.Entry<Attribute, AttributeModifier> mods : rarity.getAttributes().entrySet()) {
            AttributeInstance instance = enemy.getAttribute(mods.getKey());
            if (instance != null) {
                instance.addModifier(mods.getValue());
                if (mods.getKey().equals(Attribute.MAX_HEALTH)) {
                    enemy.setHealth(instance.getValue());
                }
            } else {
                DFanchovments.plugin.getLogger().info("Attribute " + mods.getKey().toString() + " is null for entity: " + enemy.toString());
            }
        }
    }

    @EventHandler
    public void onEntityDefeat(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Enemy enemy)) return;
        if (!(event.getDamageSource().getCausingEntity() instanceof Player killer)) return;
        if (!enemy.getPersistentDataContainer().has(rarityKey, PersistentDataType.STRING)) return;
        double roll = Math.random() * 100; // % that rolled
        Rarity rarity = Rarity.valueOf(enemy.getPersistentDataContainer().getOrDefault(rarityKey, PersistentDataType.STRING, Rarity.UNKNOWN.name).toUpperCase());
        double luck = 0.0;
        //Add enchantment luck ONLY AND ONLY IF IT WAS THE MELEE ATTACK, otherwise they might have swapped items
        if (event.getDamageSource().getCausingEntity().equals(event.getDamageSource().getDirectEntity())) {
            ItemMeta meta = killer.getInventory().getItemInMainHand().getItemMeta();
            if (meta != null && meta.hasEnchant(Enchantment.LOOTING)) {
                luck += meta.getEnchantLevel(Enchantment.LOOTING);
            }
        }

        //Add attribute luck
        AttributeInstance attr = killer.getAttribute(Attribute.LUCK);
        if (attr != null) {
            luck += attr.getValue();
        }

        if (rarity.shouldDrop(roll, luck)) {
            CEnchantment enchantment = findDrop(rarity);
            if (enchantment == null) {
                DFanchovments.plugin.getLogger().warning("Failed to find enchantment to drop, message miniking1000 pretty please");
                return;
            }


            ItemStack book = enchantment.createRandomBook(luck);
            Item item = enemy.getWorld().dropItemNaturally(enemy.getLocation(), book);
            item.setGlowing(true);
        }
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Enemy enemy && enemy.getPersistentDataContainer().has(rarityKey, PersistentDataType.STRING)) {
            event.setCancelled(true);
        }
    }

    public CEnchantment findDrop(Rarity rarity) {
        int total_weight = 0;
        for (CEnchantment CEnch : DFanchovments.CEnchantments) {
            int weight = CEnch.getWeight(rarity);
            if (weight >= 0) {
                total_weight += weight;
            }
        }
        if (total_weight <= 0) {
            return null;
        }
        int roll = rng.nextInt(total_weight);
        for (CEnchantment enchantment : DFanchovments.CEnchantments) {
            int weight = enchantment.getWeight(rarity);
            if (weight <= 0) continue;
            if (roll < weight) {
                return enchantment;
            }
            roll -= weight;
        }

        return null; // should never happen
    }


}
