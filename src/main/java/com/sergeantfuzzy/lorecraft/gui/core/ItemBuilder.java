// tiny util for clean item creation
package com.sergeantfuzzy.lorecraft.gui.core;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.ArrayList;
import java.util.List;


public final class ItemBuilder {
    private static final MiniMessage MM = MiniMessage.miniMessage();


    public static ItemStack icon(Material mat, String mmName, String... mmLore) {
        ItemStack is = new ItemStack(mat);
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return is;
        Component name = MM.deserialize(mmName);
        meta.displayName(name);
        List<Component> lore = new ArrayList<>();
        for (String line : mmLore) lore.add(MM.deserialize(line));
        meta.lore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        is.setItemMeta(meta);
        return is;
    }
}