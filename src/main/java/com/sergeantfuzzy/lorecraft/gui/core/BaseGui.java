package com.sergeantfuzzy.lorecraft.gui.core;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;


import java.util.*;
import java.util.function.Consumer;


/** Base class for clickable inventory UIs. */
public abstract class BaseGui implements Listener {
    protected static final MiniMessage MM = MiniMessage.miniMessage();


    protected final Plugin plugin;
    protected Inventory inv;
    private final Map<Integer, Consumer<InventoryClickEvent>> clickHandlers = new HashMap<>();


    protected BaseGui(Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    /** Title to render. Use MiniMessage. */
    protected abstract Component title(Player viewer);


    /** Size must be a multiple of 9. */
    protected abstract int size();


    /** Called when inventory is (re)built. */
    protected abstract void build(Player viewer);


    protected void put(int slot, ItemStack item, Consumer<InventoryClickEvent> onClick) {
        inv.setItem(slot, item);
        if (onClick != null) clickHandlers.put(slot, onClick);
    }


    public void open(Player viewer) {
        this.inv = Bukkit.createInventory(null, size(), title(viewer));
        build(viewer);
        viewer.openInventory(inv);
    }


    @EventHandler public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null || !Objects.equals(e.getClickedInventory(), inv)) return;
        e.setCancelled(true);
        var handler = clickHandlers.get(e.getRawSlot());
        if (handler != null) handler.accept(e);
    }


    @EventHandler public void onClose(InventoryCloseEvent e) {
        if (!Objects.equals(e.getInventory(), inv)) return;
// optional: unregister per-instance listeners on close
    }
}