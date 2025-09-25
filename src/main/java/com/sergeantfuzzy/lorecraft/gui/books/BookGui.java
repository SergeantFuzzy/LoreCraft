// scaffold for the Books editor
package com.sergeantfuzzy.lorecraft.gui.books;

import com.sergeantfuzzy.lorecraft.gui.MainGui;
import com.sergeantfuzzy.lorecraft.gui.core.BaseGui;
import com.sergeantfuzzy.lorecraft.gui.core.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BookGui extends BaseGui {
    public BookGui(Plugin plugin) { super(plugin); }

    @Override
    protected Component title(Player viewer) {
        return MM.deserialize("<gradient:#ffb400:#ff6a00><bold>Books • Editor</bold></gradient>");
    }

    @Override
    protected int size() { return 54; }

    @Override
    protected void build(Player viewer) {
        // Example "create new book" button
        put(4,
                ItemBuilder.icon(Material.BOOK,
                        "<yellow><bold>Create New</bold>",
                        "<gray>Start a new lore book</gray>"),
                e -> viewer.performCommand("lc book create new_book")
        );

        // 🔙 Back button
        put(49, // center of bottom row in a 54-slot inventory
                ItemBuilder.icon(Material.ARROW,
                        "<red><bold>⬅ Back</bold>",
                        "<gray>Return to the main menu</gray>"),
                e -> new MainGui(plugin).open(viewer)
        );
    }
}