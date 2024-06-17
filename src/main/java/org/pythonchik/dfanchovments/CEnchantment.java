package org.pythonchik.dfanchovments;

import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.List;

public class CEnchantment {
    NamespacedKey id;
    public String getName(){
        return DFanchovments.getConfig1().getString(this.getId().getKey().toString() + ".name");
    }
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        return retu;
    }
    public NamespacedKey getId(){
        return this.id;
    }
    public int getMaxLevel() {
        return DFanchovments.getConfig1().getInt(this.getId().getKey().toString() + ".maxlvl");
    }
    public int getStartLevel() {
        return 1;
    }
}
