package com.sergeantfuzzy.lorecraft.commands.core;

import com.sergeantfuzzy.lorecraft.LoreCraft;
import com.sergeantfuzzy.lorecraft.commands.books.BookGuiCommand;
import com.sergeantfuzzy.lorecraft.commands.general.AboutCommand;
import com.sergeantfuzzy.lorecraft.commands.general.GuiOpenCommand;
import com.sergeantfuzzy.lorecraft.commands.general.HelpCommand;
import com.sergeantfuzzy.lorecraft.commands.admin.ReloadCommand;
import com.sergeantfuzzy.lorecraft.commands.general.PacksCommand;
import com.sergeantfuzzy.lorecraft.commands.hologram.HologramCommand;
import org.bukkit.command.*;

import java.util.*;

public class CommandRegistry {
    private final LoreCraft plugin;

    // alias -> subcommand
    private final Map<String, Subcommand> byAlias = new HashMap<>();
    // primary -> category
    private final Map<String, CommandCategory> categories = new HashMap<>();

    public CommandRegistry(LoreCraft plugin) {
        this.plugin = plugin;
        registerAll();
    }

    private void registerAll() {
        register(CommandCategory.GENERAL, new HelpCommand(plugin), "help");
        register(CommandCategory.GENERAL, new AboutCommand(plugin), "about");
        register(CommandCategory.GENERAL, new GuiOpenCommand(plugin), "gui", "open", "menu", "main");
        register(CommandCategory.ADMIN, new HologramCommand(plugin),
                "holo", "hologram", "holograms");
        register(CommandCategory.ADMIN, new ReloadCommand(plugin), "reload");
        register(CommandCategory.ADMIN, new PacksCommand(plugin), "packs", "packlist", "questpacks");
        register(CommandCategory.BOOKS, new BookGuiCommand(plugin), "book", "books");

        // ─────────────────────────────────────────────────────────────────────────
        // NEW: GUI adapters so /lc npc gui, /lc scene gui, /lc holo gui work
        // ─────────────────────────────────────────────────────────────────────────

        // /lc npc gui   (aliases: dialogue, dialogues)
        register(CommandCategory.GENERAL, new Subcommand() {
            @Override public String getName() { return "npc"; }
            @Override public String getDescription() { return "Open/edit NPC dialogues"; }
            @Override public String getUsage() { return "/lc npc gui"; }
            @Override public String getPermission() { return ""; }
            @Override public List<String> getAliases() { return List.of("dialogue","dialogues"); }
            @Override public boolean execute(CommandSender sender, String[] args) {
                if (!(sender instanceof org.bukkit.entity.Player p)) {
                    sender.sendMessage(plugin.mm().deserialize("<red>This subcommand can only be used in-game.</red>"));
                    return true;
                }
                if (args.length == 0 || "gui".equalsIgnoreCase(args[0])) {
                    new com.sergeantfuzzy.lorecraft.gui.dialogues.DialogueGui(plugin).open(p);
                    return true;
                }
                // fallback usage from language file
                plugin.lang().lines("usage.lc.lines").forEach(line ->
                        sender.sendMessage(plugin.mm().deserialize(line)));
                return true;
            }
            @Override public List<String> tabComplete(CommandSender sender, String[] args) {
                return (args.length <= 1) ? List.of("gui") : List.of();
            }
        }, "dialogue", "dialogues");

        // /lc scene gui   (aliases: cutscene, cutscenes)
        register(CommandCategory.GENERAL, new Subcommand() {
            @Override public String getName() { return "scene"; }
            @Override public String getDescription() { return "Play/stop cutscenes"; }
            @Override public String getUsage() { return "/lc scene gui"; }
            @Override public String getPermission() { return ""; }
            @Override public List<String> getAliases() { return List.of("cutscene","cutscenes"); }
            @Override public boolean execute(CommandSender sender, String[] args) {
                if (!(sender instanceof org.bukkit.entity.Player p)) {
                    sender.sendMessage(plugin.mm().deserialize("<red>This subcommand can only be used in-game.</red>"));
                    return true;
                }
                if (args.length == 0 || "gui".equalsIgnoreCase(args[0])) {
                    new com.sergeantfuzzy.lorecraft.gui.scenes.ScenesGui(plugin).open(p);
                    return true;
                }
                plugin.lang().lines("usage.lc.lines").forEach(line ->
                        sender.sendMessage(plugin.mm().deserialize(line)));
                return true;
            }
            @Override public List<String> tabComplete(CommandSender sender, String[] args) {
                return (args.length <= 1) ? List.of("gui") : List.of();
            }
        }, "cutscene", "cutscenes");

        // /lc holo gui   (aliases: hologram, holograms)
        register(CommandCategory.GENERAL, new Subcommand() {
            @Override public String getName() { return "holo"; }
            @Override public String getDescription() { return "Manage holograms"; }
            @Override public String getUsage() { return "/lc holo gui"; }
            @Override public String getPermission() { return ""; }
            @Override public List<String> getAliases() { return List.of("hologram","holograms"); }
            @Override public boolean execute(CommandSender sender, String[] args) {
                if (!(sender instanceof org.bukkit.entity.Player p)) {
                    sender.sendMessage(plugin.mm().deserialize("<red>This subcommand can only be used in-game.</red>"));
                    return true;
                }
                if (args.length == 0 || "gui".equalsIgnoreCase(args[0])) {
                    new com.sergeantfuzzy.lorecraft.gui.holograms.HologramGui(plugin).open(p);
                    return true;
                }
                plugin.lang().lines("usage.lc.lines").forEach(line ->
                        sender.sendMessage(plugin.mm().deserialize(line)));
                return true;
            }
            @Override public List<String> tabComplete(CommandSender sender, String[] args) {
                return (args.length <= 1) ? List.of("gui") : List.of();
            }
        }, "hologram", "holograms");
        // ─────────────────────────────────────────────────────────────────────────
    }

    public void register(CommandCategory cat, Subcommand cmd, String... aliases) {
        String primary = cmd.getName();
        categories.put(primary, cat);
        byAlias.put(primary.toLowerCase(Locale.ROOT), cmd);
        for (String a : aliases) {
            if (a == null || a.isBlank()) continue;
            byAlias.put(a.toLowerCase(Locale.ROOT), cmd);
        }
        for (String a : cmd.getAliases()) {
            byAlias.put(a.toLowerCase(Locale.ROOT), cmd);
        }
    }

    public Subcommand get(String alias) {
        return byAlias.get(alias.toLowerCase(Locale.ROOT));
    }

    // Root executor/completer bound in onEnable
    public CommandExecutor rootExecutor() {
        return (sender, command, label, args) -> {
            if (args.length == 0) {
                // default open help page 1
                return get("help").execute(sender, new String[]{"1"});
            }
            Subcommand sub = get(args[0]);
            if (sub == null) {
                // usage
                plugin.lang().lines("usage.lc.lines").forEach(line ->
                        sender.sendMessage(plugin.mm().deserialize(line))
                );
                return true;
            }
            if (sub.getPermission() != null && !sub.getPermission().isBlank() &&
                    !sender.hasPermission(sub.getPermission())) {
                sender.sendMessage(plugin.mm().deserialize("<red>You lack permission.</red>"));
                return true;
            }
            String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            return sub.execute(sender, subArgs);
        };
    }

    public TabCompleter rootCompleter() {
        return (sender, command, alias, args) -> {
            if (args.length == 1) {
                String start = args[0].toLowerCase(Locale.ROOT);
                return byAlias.keySet().stream()
                        .filter(k -> k.startsWith(start))
                        .sorted().toList();
            }
            Subcommand sub = get(args[0]);
            if (sub != null) {
                String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
                return sub.tabComplete(sender, subArgs);
            }
            return List.of();
        };
    }
}