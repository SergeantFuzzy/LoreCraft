package com.sergeantfuzzy.lorecraft.gui.scenes;

import com.sergeantfuzzy.lorecraft.gui.MainGui;
import com.sergeantfuzzy.lorecraft.gui.core.BaseGui;
import com.sergeantfuzzy.lorecraft.gui.core.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ScenesGui extends BaseGui {
    public ScenesGui(Plugin plugin) { super(plugin); }

    @Override
    protected Component title(Player viewer) {
        return MM.deserialize("<gradient:#ffb400:#ff6a00><bold>Scenes • Cinematics</bold></gradient>");
    }

    @Override
    protected int size() { return 54; }

    @Override
    protected void build(Player viewer) {
        // Example: Start recording a scene path
        put(10, ItemBuilder.icon(Material.RECOVERY_COMPASS,
                        "<gold><bold>Record Path</bold>",
                        "<gray>Begin capturing a camera route</gray>",
                        "<yellow><italic>ID:</italic> <white>intro_walk</white></yellow>"),
                e -> viewer.performCommand("lc scene record intro_walk"));

        // Example: Play a scene
        put(12, ItemBuilder.icon(Material.SPYGLASS,
                        "<aqua><bold>Play Scene</bold>",
                        "<gray>Preview a saved cinematic</gray>"),
                e -> viewer.performCommand("lc scene play intro_walk"));

        // Example: Add action at your location
        put(14, ItemBuilder.icon(Material.LEVER,
                        "<light_purple><bold>Add Action</bold>",
                        "<gray>Title · sound · command · delay</gray>"),
                e -> viewer.performCommand("lc scene action add intro_walk title '<yellow>Welcome!'"));

        // Example: Save/export scene
        put(16, ItemBuilder.icon(Material.CARTOGRAPHY_TABLE,
                        "<green><bold>Save/Export</bold>",
                        "<gray>Write scene to file</gray>"),
                e -> viewer.performCommand("lc scene save intro_walk"));

        // Back to main
        put(49, ItemBuilder.icon(Material.ARROW,
                        "<red><bold>⬅ Back</bold>",
                        "<gray>Return to main menu</gray>"),
                e -> new MainGui(plugin).open(viewer));
    }
}