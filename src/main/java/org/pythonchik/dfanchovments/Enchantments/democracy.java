package org.pythonchik.dfanchovments.Enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.pythonchik.dfanchovments.CEnchantment;

import java.util.ArrayList;
import java.util.List;

public class democracy extends CEnchantment implements Listener {
    NamespacedKey id;
    public democracy(NamespacedKey id) {
        this.id = id;
    }
    @Override
    public List<String> getTragers(){
        List<String> retu = new ArrayList<>();
        return retu;
    }
    @Override
    public NamespacedKey getId(){
        return this.id;
    }
}
