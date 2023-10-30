package org.pythonchik.dfanchovments;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


public class dfchants implements CommandExecutor, Listener {
    DFanchovments plugin;// = (DFanchovments) Bukkit.getPluginManager().getPlugin("DFanchovments");
    Message message = DFanchovments.getMessage();
    private final Logger logger = Bukkit.getPluginManager().getPlugin("DFanchovments").getLogger();
    FileConfiguration config;
    ItemStack[] EmptyGuiContents;
    public dfchants(FileConfiguration config, DFanchovments plugin){
        this.config=config;this.plugin=plugin;
        ItemStack itemStack = getNull();
        EmptyGuiContents = new ItemStack[27];
        Arrays.fill(EmptyGuiContents, itemStack);

    }

    public ItemStack getNull(){
        ItemStack itemStack = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "I-tem"), PersistentDataType.STRING, "null");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean bpl = sender instanceof Player;
        if (args.length >= 1) {
            if (args[0].equals("reload") && sender.isOp()) {
                plugin.reload5();
                if (bpl) message.send(sender, "#B44C43Перезагрузка завершена");
                else logger.info("Перезагрузка завершена");
            }
        } //admin commands (args>=1)
        else {
            if (bpl) OpenMenu((Player) sender);
            else logger.info("FIX LATER"); //TODO FIX THIS MESSAGE
        }


        return true;
    }

    private void OpenMenu(Player player) {
        Inventory gui = Bukkit.createInventory(player, 27, message.hex("&6Уникальные чары"));
        gui.setContents(EmptyGuiContents);
        player.openInventory(gui);
    }

    private ItemStack[] getEnchantItem(Player player,ArrayList<CEnchantment> enchs) {
        ItemStack[] stack = new ItemStack[enchs.size()];
        if (enchs.size()==0){
            return stack;
        }
        int i=0;
        for (CEnchantment ench : enchs) {

            for (String itemString : config.getKeys(false)) {
                if (!(new NamespacedKey(plugin,itemString)).equals(ench.getKey())) {
                    continue;
                }
                message.send(player,ench.getKey() + " " + new NamespacedKey(plugin,itemString));

                ItemStack itemStack = Material.getMaterial(config.getString(itemString + ".material")) != null ? new ItemStack(Material.getMaterial(config.getString(itemString + ".material"))) : new ItemStack(Material.BARRIER);
                ItemMeta itemMeta = itemStack.getItemMeta();

                itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "I-tem"), PersistentDataType.STRING, itemString);

                List<String> lore = new ArrayList<>();
                itemMeta.setDisplayName(message.hex(config.getString(itemString + ".name")));
                lore.add(message.hex(String.format("&7Нажмите сюда что-бы за &6%d&7 наложить зачарование &6%s", config.getInt(itemString + ".xp"), config.getString(itemString + ".name"))));
                lore.add(message.hex(config.getString(itemString + ".lore")));
                lore.add(message.hex(String.format("&7На данный момент вам доступно &6%d&7ед. опыта", XP.getTotalExperience(player))));
                itemMeta.setLore(lore);

                itemStack.setItemMeta(itemMeta);
                stack[i] = itemStack;

            }
            i++;
        }
        return stack;
    }
    public ItemStack[] addItemsToMenu(ItemStack[] stack,ItemStack nul){
        ItemStack[] stacks = new ItemStack[27];
        for (int i=0;i<stacks.length;i++){
            if (i>10 && i<stack.length+11) {
                stacks[i]=stack[i-11];
            } else {
                stacks[i]=(nul);
            }
        }
        return stacks;
    }

    public void updateGui(Player player, Inventory gui,ArrayList<CEnchantment> enches) {
        gui.setContents(addItemsToMenu(getEnchantItem(player,enches),getNull()));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(message.hex("&6Уникальные чары"))) {
            return;
        }
        if (event.getCurrentItem() == null) {
            return;
        }
        event.setCancelled(true);
        ItemStack stack = event.getCurrentItem();
        if (stack.getItemMeta().getPersistentDataContainer().isEmpty()) {
            ArrayList<CEnchantment> enchs = new ArrayList<>();
            for (CEnchantment CEnchantment : DFanchovments.CEnchantments) {
                if (CEnchantment.getTragers().equals(new ArrayList<>())) {
                    return;
                }
                for (String gooditem : CEnchantment.getTragers()) {
                    if ((Material.getMaterial(gooditem) != null ? Material.getMaterial(gooditem) : Material.BARRIER).equals(stack.getType())) {
                        enchs.add(CEnchantment);
                    }
                }
            }
            updateGui((Player) event.getWhoClicked(), event.getInventory(), enchs);
        } else {

        } //ench material
    }
}
