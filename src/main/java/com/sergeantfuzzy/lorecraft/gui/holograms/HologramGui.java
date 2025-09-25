package com.sergeantfuzzy.lorecraft.gui.holograms;

import com.sergeantfuzzy.lorecraft.gui.MainGui;
import com.sergeantfuzzy.lorecraft.gui.core.BaseGui;
import com.sergeantfuzzy.lorecraft.gui.core.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class HologramGui extends BaseGui {
    public HologramGui(Plugin plugin) { super(plugin); }

    @Override
    protected Component title(Player viewer) {
        return MM.deserialize("<gradient:#ffb400:#ff6a00><bold>Holograms & Effects</bold></gradient>");
    }

    @Override
    protected int size() { return 54; }

    @Override
    protected void build(Player viewer) {
        // Example: Create hologram
        put(10, ItemBuilder.icon(Material.AMETHYST_SHARD,
                        "<gold><bold>Create Hologram</bold>",
                        "<gray>Spawn a multiline hologram</gray>",
                        "<yellow><italic>ID:</italic> <white>spawn_welcome</white></yellow>"),
                e -> viewer.performCommand("lc holo create spawn_welcome"));

        // Example: Send title to self
        put(12, ItemBuilder.icon(Material.OAK_SIGN,
                        "<aqua><bold>Send Title</bold>",
                        "<gray>Show a styled title to you</gray>"),
                e -> viewer.performCommand("lc title send"));

        // Example: Particle demo
        put(14, ItemBuilder.icon(Material.FIREWORK_STAR,
                        "<light_purple><bold>Particle Demo</bold>",
                        "<gray>Preview a particle effect</gray>"),
                e -> viewer.performCommand("lc particle demo"));

        // Example: Play sound
        put(16, ItemBuilder.icon(Material.NOTE_BLOCK,
                        "<green><bold>Play Sound</bold>",
                        "<gray>Hear a feedback sound</gray>"),
                e -> viewer.performCommand("lc sound play ui.button.click"));

        // Back to main
        put(49, ItemBuilder.icon(Material.ARROW,
                        "<red><bold>⬅ Back</bold>",
                        "<gray>Return to main menu</gray>"),
                e -> new MainGui(plugin).open(viewer));
    }

    public void open(org.bukkit.entity.Player viewer, String id) {
        this.open(viewer);
    }
}