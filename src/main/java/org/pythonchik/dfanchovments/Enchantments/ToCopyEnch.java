package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.List;

public class ToCopyEnch extends CEnchantment implements Listener {
    public ToCopyEnch(NamespacedKey id) {
        super(id);
    }
    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        retu.add("ENCHANTED_BOOK");
        return retu;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
