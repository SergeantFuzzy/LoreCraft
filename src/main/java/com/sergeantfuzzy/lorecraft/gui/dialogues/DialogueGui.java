package com.sergeantfuzzy.lorecraft.gui.dialogues;

import com.sergeantfuzzy.lorecraft.gui.MainGui;
import com.sergeantfuzzy.lorecraft.gui.core.BaseGui;
import com.sergeantfuzzy.lorecraft.gui.core.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DialogueGui extends BaseGui {
    public DialogueGui(Plugin plugin) { super(plugin); }

    @Override
    protected Component title(Player viewer) {
        return MM.deserialize("<gradient:#ffb400:#ff6a00><bold>Dialogues • Editor</bold></gradient>");
    }

    @Override
    protected int size() { return 54; }

    @Override
    protected void build(Player viewer) {
        // Example: Open NPC tree
        put(10, ItemBuilder.icon(Material.VILLAGER_SPAWN_EGG,
                        "<gold><bold>Open NPC</bold>",
                        "<gray>Load a dialogue tree</gray>",
                        "<yellow><italic>NPC:</italic> <white>blacksmith</white></yellow>"),
                e -> viewer.performCommand("lc npc open blacksmith"));

        // Example: Create new dialogue
        put(12, ItemBuilder.icon(Material.WRITABLE_BOOK,
                        "<yellow><bold>Create Dialogue</bold>",
                        "<gray>Start a new dialogue tree</gray>"),
                e -> viewer.performCommand("lc npc create new_tree"));

        // Example: Test current session
        put(14, ItemBuilder.icon(Material.PAPER,
                        "<aqua><bold>Test Session</bold>",
                        "<gray>Run active dialogue with you</gray>"),
                e -> viewer.performCommand("lc npc test"));

        // Back to main
        put(49, ItemBuilder.icon(Material.ARROW,
                        "<red><bold>⬅ Back</bold>",
                        "<gray>Return to main menu</gray>"),
                e -> new MainGui(plugin).open(viewer));
    }
}