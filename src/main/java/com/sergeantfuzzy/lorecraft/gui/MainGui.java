// hub menu with links to Books / Dialogues / Scenes
package com.sergeantfuzzy.lorecraft.gui;


import com.sergeantfuzzy.lorecraft.gui.core.BaseGui;
import com.sergeantfuzzy.lorecraft.gui.core.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;


public class MainGui extends BaseGui {
    public MainGui(Plugin plugin) { super(plugin); }


    @Override protected Component title(Player viewer) {
        return MM.deserialize("<gradient:#ffb400:#ff6a00><bold>✦ LoreCraft • Builder ✦</bold></gradient>");
    }


    @Override protected int size() { return 27; }


    @Override protected void build(Player viewer) {

        // Books
        put(10,
                ItemBuilder.icon(Material.WRITTEN_BOOK,
                        "<gold><bold>Books</bold>",
                        "<gray>Create codices, journals, quest logs</gray>",
                        "<yellow><italic>Click to open</italic></yellow>"
                ),
                (InventoryClickEvent e) -> viewer.performCommand("lc book gui")
        );

        // Dialogues
        put(12,
                ItemBuilder.icon(Material.VILLAGER_SPAWN_EGG,
                        "<gold><bold>Dialogues</bold>",
                        "<gray>Branching NPC conversations</gray>",
                        "<yellow><italic>Click to open</italic></yellow>"),
                e -> viewer.performCommand("lc npc gui")
        );

        // Scenes
        put(14,
                ItemBuilder.icon(Material.RECOVERY_COMPASS,
                        "<gold><bold>Scenes</bold>",
                        "<gray>Cinematics, camera paths, actions</gray>",
                        "<yellow><italic>Click to open</italic></yellow>"),
                e -> viewer.performCommand("lc scene gui")
        );

        // Holograms / Effects
        put(16,
                ItemBuilder.icon(Material.AMETHYST_SHARD,
                        "<gold><bold>Holograms & Effects</bold>",
                        "<gray>Titles · particles · sounds</gray>",
                        "<yellow><italic>Click to open</italic></yellow>"),
                e -> viewer.performCommand("lc holo gui")
        );
    }
}