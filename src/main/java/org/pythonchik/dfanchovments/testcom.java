package org.pythonchik.dfanchovments;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class testcom implements CommandExecutor {
    DFanchovments plugin = (DFanchovments) Bukkit.getPluginManager().getPlugin("DFanchovments");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        ItemStack stack = new ItemStack(Material.NETHERITE_AXE);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName("soul");
        meta.addEnchant(DFanchovments.soul,1,true);
        stack.setItemMeta(meta);
        player.getInventory().addItem(stack);
        ItemStack stack2 = new ItemStack(Material.NETHERITE_AXE);
        ItemMeta meta2 = stack2.getItemMeta();
        meta2.setDisplayName("antisoul");
        meta2.addEnchant(DFanchovments.antisoul,1,true);
        stack2.setItemMeta(meta2);
        player.getInventory().addItem(stack2);
        ItemStack stack3 = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta3 = stack3.getItemMeta();
        meta3.setDisplayName("xpench");
        meta3.addEnchant(DFanchovments.XPEnch,5,true);
        stack3.setItemMeta(meta3);
        player.getInventory().addItem(stack3);
        ItemStack stack4 = new ItemStack(Material.BOW);
        ItemMeta meta4 = stack4.getItemMeta();
        meta4.setDisplayName("AR rain");
        meta4.addEnchant(DFanchovments.ARrain,5,true);
        stack4.setItemMeta(meta4);
        player.getInventory().addItem(stack4);
        return true;
    }
}
