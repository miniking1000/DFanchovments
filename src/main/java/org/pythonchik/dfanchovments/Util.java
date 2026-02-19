package org.pythonchik.dfanchovments;

import org.bukkit.Material;

import java.util.*;

public class Util {

    public static String toRoman(int lvl) {
        if (lvl <= 0 || lvl > 255) return String.valueOf(lvl);

        int[] values = {100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] numerals = {"C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        StringBuilder roman = new StringBuilder();
        int remaining = lvl;

        for (int i = 0; i < values.length; i++) {
            while (remaining >= values[i]) {
                roman.append(numerals[i]);
                remaining -= values[i];
            }
        }

        return roman.toString();
    }

    private static final double LUCK_SOFTCAP_K = 1.5;
    public static double softcapLuck(double luck) {
        if (luck <= 0.0) return 0.0;
        return luck / (LUCK_SOFTCAP_K + luck);
    }

    public static double clamp01(double v) {
        if (v < 0.0) return 0.0;
        if (v >= 1.0) return Math.nextDown(1.0);
        return v;
    }


    public static List<String> helmets() {
        List<Material> retu = new ArrayList<>();
        retu.add(Material.LEATHER_HELMET);
        retu.add(Material.CHAINMAIL_HELMET);
        retu.add(Material.COPPER_HELMET);
        retu.add(Material.IRON_HELMET);
        retu.add(Material.GOLDEN_HELMET);
        retu.add(Material.DIAMOND_HELMET);
        retu.add(Material.NETHERITE_HELMET);
        return retu.stream()
                .map(Material::name)
                .toList();
    }

    public static List<String> chestplates() {
        List<Material> retu = new ArrayList<>();
        retu.add(Material.LEATHER_CHESTPLATE);
        retu.add(Material.CHAINMAIL_CHESTPLATE);
        retu.add(Material.COPPER_CHESTPLATE);
        retu.add(Material.IRON_CHESTPLATE);
        retu.add(Material.GOLDEN_CHESTPLATE);
        retu.add(Material.DIAMOND_CHESTPLATE);
        retu.add(Material.NETHERITE_CHESTPLATE);
        return retu.stream()
                .map(Material::name)
                .toList();
    }

    public static List<String> leggings() {
        List<Material> retu = new ArrayList<>();
        retu.add(Material.LEATHER_LEGGINGS);
        retu.add(Material.CHAINMAIL_LEGGINGS);
        retu.add(Material.COPPER_LEGGINGS);
        retu.add(Material.IRON_LEGGINGS);
        retu.add(Material.GOLDEN_LEGGINGS);
        retu.add(Material.DIAMOND_LEGGINGS);
        retu.add(Material.NETHERITE_LEGGINGS);
        return retu.stream()
                .map(Material::name)
                .toList();
    }

    public static List<String> boots() {
        List<Material> retu = new ArrayList<>();
        retu.add(Material.LEATHER_BOOTS);
        retu.add(Material.CHAINMAIL_BOOTS);
        retu.add(Material.COPPER_BOOTS);
        retu.add(Material.IRON_BOOTS);
        retu.add(Material.GOLDEN_BOOTS);
        retu.add(Material.DIAMOND_BOOTS);
        retu.add(Material.NETHERITE_BOOTS);
        return retu.stream()
                .map(Material::name)
                .toList();
    }

    public static List<String> swords() {
        List<Material> retu = new ArrayList<>();
        retu.add(Material.WOODEN_SWORD);
        retu.add(Material.STONE_SWORD);
        retu.add(Material.COPPER_SWORD);
        retu.add(Material.IRON_SWORD);
        retu.add(Material.GOLDEN_SWORD);
        retu.add(Material.DIAMOND_SWORD);
        retu.add(Material.NETHERITE_SWORD);
        return retu.stream()
                .map(Material::name)
                .toList();
    }

    public static List<String> pickaxes() {
        List<Material> retu = new ArrayList<>();
        retu.add(Material.WOODEN_PICKAXE);
        retu.add(Material.STONE_PICKAXE);
        retu.add(Material.COPPER_PICKAXE);
        retu.add(Material.IRON_PICKAXE);
        retu.add(Material.GOLDEN_PICKAXE);
        retu.add(Material.DIAMOND_PICKAXE);
        retu.add(Material.NETHERITE_PICKAXE);
        return retu.stream()
                .map(Material::name)
                .toList();
    }

    public static List<String> axes() {
        List<Material> retu = new ArrayList<>();
        retu.add(Material.WOODEN_AXE);
        retu.add(Material.STONE_AXE);
        retu.add(Material.COPPER_AXE);
        retu.add(Material.IRON_AXE);
        retu.add(Material.GOLDEN_AXE);
        retu.add(Material.DIAMOND_AXE);
        retu.add(Material.NETHERITE_AXE);
        return retu.stream()
                .map(Material::name)
                .toList();
    }

    public static List<String> shovels() {
        List<Material> retu = new ArrayList<>();
        retu.add(Material.WOODEN_SHOVEL);
        retu.add(Material.STONE_SHOVEL);
        retu.add(Material.COPPER_SHOVEL);
        retu.add(Material.IRON_SHOVEL);
        retu.add(Material.GOLDEN_SHOVEL);
        retu.add(Material.DIAMOND_SHOVEL);
        retu.add(Material.NETHERITE_SHOVEL);
        return retu.stream()
                .map(Material::name)
                .toList();
    }

    public static List<String> hoes() {
        List<Material> retu = new ArrayList<>();
        retu.add(Material.WOODEN_HOE);
        retu.add(Material.STONE_HOE);
        retu.add(Material.COPPER_HOE);
        retu.add(Material.IRON_HOE);
        retu.add(Material.GOLDEN_HOE);
        retu.add(Material.DIAMOND_HOE);
        retu.add(Material.NETHERITE_HOE);
        return retu.stream()
                .map(Material::name)
                .toList();
    }

    public static List<String> spears() {
        List<Material> retu = new ArrayList<>();
        retu.add(Material.WOODEN_SPEAR);
        retu.add(Material.STONE_SPEAR);
        retu.add(Material.COPPER_SPEAR);
        retu.add(Material.IRON_SPEAR);
        retu.add(Material.GOLDEN_SPEAR);
        retu.add(Material.DIAMOND_SPEAR);
        retu.add(Material.NETHERITE_SPEAR);
        return retu.stream()
                .map(Material::name)
                .toList();
    }


    public static List<String> armors() {
        List<String> retu = new ArrayList<>();
        retu.addAll(helmets());
        retu.addAll(chestplates());
        retu.addAll(leggings());
        retu.addAll(boots());
        return retu;
    }

    public static List<String> instruments() {
        List<String> retu = new ArrayList<>();
        retu.addAll(pickaxes());
        retu.addAll(axes());
        retu.addAll(shovels());
        retu.addAll(hoes());
        return retu;
    }


}
