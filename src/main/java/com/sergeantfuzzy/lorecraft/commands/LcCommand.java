package com.sergeantfuzzy.lorecraft.commands;

import com.sergeantfuzzy.lorecraft.commands.core.CommandCategory;
import com.sergeantfuzzy.lorecraft.commands.core.Subcommand;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

// ▼ ADDED: imports to open GUIs via singleton plugin
import com.sergeantfuzzy.lorecraft.LoreCraft;
import com.sergeantfuzzy.lorecraft.gui.dialogues.DialogueGui;
import com.sergeantfuzzy.lorecraft.gui.scenes.ScenesGui;
import com.sergeantfuzzy.lorecraft.gui.holograms.HologramGui;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class LcCommand implements CommandExecutor, TabCompleter {

    private static Plugin plugin() {
        return JavaPlugin.getProvidingPlugin(LcCommand.class);
    }

    private static final MiniMessage MM = MiniMessage.miniMessage();

    private final Map<String, Subcommand> subs = new LinkedHashMap<>();
    private final Map<String, String> aliases = new HashMap<>();

    // ---------- Minimal base that SATISFIES your Subcommand interface ----------
    private static abstract class BasicSub implements Subcommand {
        private final String name;
        private final String description;
        private final String usage;
        private final String permission;
        private final List<String> aliasList;
        private final boolean playerOnly;
        private final CommandCategory category;

        protected BasicSub(
                String name,
                String description,
                String usage,
                String permission,
                boolean playerOnly,
                CommandCategory category,
                String... aliases
        ) {
            this.name = name;
            this.description = description != null ? description : "";
            this.usage = usage != null ? usage : "";
            this.permission = permission != null ? permission : "";
            this.playerOnly = playerOnly;
            this.category = (category != null ? category : CommandCategory.GENERAL);
            this.aliasList = (aliases == null || aliases.length == 0)
                    ? Collections.emptyList()
                    : Arrays.asList(aliases);
        }

        @Override public String getName() { return name; }
        @Override public String getDescription() { return description; }
        @Override public String getUsage() { return usage; }
        @Override public String getPermission() { return permission; }
        @Override public List<String> getAliases() { return aliasList; }
        @Override public boolean playerOnly() { return playerOnly; }
        @Override public CommandCategory getCategory() { return category; }

        // Subclasses MUST implement execute(); tabComplete is optional
        @Override public List<String> tabComplete(CommandSender sender, String[] args) { return Collections.emptyList(); }
    }

    // ---------- Constructors ----------

    /** Default constructor with built-in stubs you can replace with real logic */
    public LcCommand() {
        // help / ?
        register(new BasicSub(
                "help",
                "Show LoreCraft help",
                "/lc help",
                "", false, CommandCategory.GENERAL, "?"
        ) {
            @Override public boolean execute(CommandSender s, String[] args) {
                String label = "lc";
                showRootHelp(s, label);
                return true;
            }
        });

        // book
        register(new BasicSub(
                "book",
                "Manage LoreCraft books",
                "/lc book <open|create|addpage|list> ...",
                "lorecraft.book", false, CommandCategory.BOOKS, "books"
        ) {
            @Override public boolean execute(CommandSender s, String[] a) {
                s.sendMessage(MM.deserialize("<yellow>/lc book</yellow> <gray>- (stub) implement book actions here</gray>"));
                return true;
            }
            @Override public List<String> tabComplete(CommandSender s, String[] a) {
                return suggest(a, "open","create","addpage","list");
            }
        });

        // effect
        register(new BasicSub(
                "effect",
                "Play/stop visual effects",
                "/lc effect <play|stop|list> ...",
                "lorecraft.effect", false, CommandCategory.GENERAL, "fx","effects"
        ) {
            @Override public boolean execute(CommandSender s, String[] a) {
                s.sendMessage(MM.deserialize("<yellow>/lc effect</yellow> <gray>- (stub) implement effects here</gray>"));
                return true;
            }
            @Override public List<String> tabComplete(CommandSender s, String[] a) {
                return suggest(a, "play","stop","list");
            }
        });

        // holo  ✨ ADDED: /lc holo gui → opens HologramGui
        register(new BasicSub(
                "holo",
                "Manage holograms",
                "/lc holo <gui|create|remove|setline|teleport> ...",
                "lorecraft.holo", false, CommandCategory.GENERAL, "hologram","holograms"
        ) {
            @Override public boolean execute(CommandSender s, String[] a) {
                if (a.length > 0 && "gui".equalsIgnoreCase(a[0])) {
                    if (!(s instanceof Player p)) {
                        s.sendMessage(MM.deserialize("<red>This subcommand can only be used in-game.</red>"));
                        return true;
                    }
                    new HologramGui(plugin()).open(p);
                    return true;
                }
                s.sendMessage(MM.deserialize("<yellow>/lc holo</yellow> <gray>- (stub) implement holograms here</gray>"));
                return true;
            }
            @Override public List<String> tabComplete(CommandSender s, String[] a) {
                return suggest(a, "gui","create","remove","setline","teleport");
            }
        });

        // npc  ✨ ADDED: /lc npc gui → opens DialogueGui
        register(new BasicSub(
                "npc",
                "Open/edit NPC dialogues",
                "/lc npc <gui|open|edit|reload> ...",
                "lorecraft.npc", true, CommandCategory.GENERAL, "dialogue","dialogues"
        ) {
            @Override public boolean execute(CommandSender s, String[] a) {
                if (a.length > 0 && "gui".equalsIgnoreCase(a[0])) {
                    if (!(s instanceof Player p)) {
                        s.sendMessage(MM.deserialize("<red>This subcommand can only be used in-game.</red>"));
                        return true;
                    }
                    new DialogueGui(plugin()).open(p);
                    return true;
                }
                if (playerOnly() && !(s instanceof Player)) {
                    s.sendMessage(MM.deserialize("<red>This subcommand can only be used in-game.</red>"));
                    return true;
                }
                s.sendMessage(MM.deserialize("<yellow>/lc npc</yellow> <gray>- (stub) implement dialogues here</gray>"));
                return true;
            }
            @Override public List<String> tabComplete(CommandSender s, String[] a) {
                return suggest(a, "gui","open","edit","reload");
            }
        });

        // scene  ✨ ADDED: /lc scene gui → opens ScenesGui
        register(new BasicSub(
                "scene",
                "Play/stop cutscenes",
                "/lc scene <gui|play|stop|list> ...",
                "lorecraft.scene", false, CommandCategory.GENERAL, "cutscene","cutscenes"
        ) {
            @Override public boolean execute(CommandSender s, String[] a) {
                if (a.length > 0 && "gui".equalsIgnoreCase(a[0])) {
                    if (!(s instanceof Player p)) {
                        s.sendMessage(MM.deserialize("<red>This subcommand can only be used in-game.</red>"));
                        return true;
                    }
                    new ScenesGui(plugin()).open(p);
                    return true;
                }
                s.sendMessage(MM.deserialize("<yellow>/lc scene</yellow> <gray>- (stub) implement scenes here</gray>"));
                return true;
            }
            @Override public List<String> tabComplete(CommandSender s, String[] a) {
                return suggest(a, "gui","play","stop","list");
            }
        });

        // vars
        register(new BasicSub(
                "vars",
                "Get/set server variables",
                "/lc vars <get|set|list|clear> ...",
                "lorecraft.vars", false, CommandCategory.GENERAL, "var","variables"
        ) {
            @Override public boolean execute(CommandSender s, String[] a) {
                s.sendMessage(MM.deserialize("<yellow>/lc vars</yellow> <gray>- (stub) implement variables here</gray>"));
                return true;
            }
            @Override public List<String> tabComplete(CommandSender s, String[] a) {
                return suggest(a, "get","set","list","clear");
            }
        });
    }

    /** Optional: allow passing external subcommands from elsewhere */
    public LcCommand(Collection<? extends Subcommand> handlers) {
        if (handlers != null) handlers.forEach(this::register);
        if (!subs.containsKey("help")) {
            register(new BasicSub("help","Show LoreCraft help","/lc help","",false, CommandCategory.GENERAL,"?") {
                @Override public boolean execute(CommandSender s, String[] args) { showRootHelp(s, "lc"); return true; }
            });
        }
    }

    // ---------- Registration / helpers ----------

    private void register(Subcommand sc) {
        String key = sc.getName();
        if (key == null || key.isEmpty()) return;
        key = key.toLowerCase(Locale.ROOT);
        subs.put(key, sc);
        for (String a : sc.getAliases()) {
            if (a != null && !a.isEmpty()) aliases.put(a.toLowerCase(Locale.ROOT), key);
        }
    }

    private static String[] shift(String[] args) {
        return (args.length <= 1) ? new String[0] : Arrays.copyOfRange(args, 1, args.length);
    }

    private String resolveKey(String maybe) {
        if (maybe == null) return null;
        String k = maybe.toLowerCase(Locale.ROOT);
        if (subs.containsKey(k)) return k;
        return aliases.getOrDefault(k, null);
    }

    private void showRootHelp(CommandSender sender, String label) {
        String lines = subs.values().stream()
                .sorted(Comparator.comparing(Subcommand::getName))
                .map(sc -> "<yellow>/" + label + " " + sc.getName() + "</yellow> <gray>—</gray> <gray>" + sc.getDescription() + "</gray>")
                .collect(Collectors.joining("\n"));
        sender.sendMessage(MM.deserialize("""
            <gradient:#ffb400:#ff6a00><bold>✦ LoreCraft</bold></gradient> <gray>• main command</gray>
            %lines%
            """.replace("%lines%", lines)));
    }

    private static List<String> suggest(String[] args, String... options) {
        if (args.length == 0) return Arrays.asList(options);
        String typed = args[args.length - 1].toLowerCase(Locale.ROOT);
        return Arrays.stream(options)
                .filter(opt -> opt.toLowerCase(Locale.ROOT).startsWith(typed))
                .sorted()
                .collect(Collectors.toList());
    }

    // ---------- Bukkit hooks ----------

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0 || "help".equalsIgnoreCase(args[0]) || "?".equals(args[0])) {
            showRootHelp(sender, label);
            return true;
        }

        String key = resolveKey(args[0]);
        if (key == null) {
            sender.sendMessage(MM.deserialize("<red>Unknown subcommand:</red> <white>" + args[0] + "</white>"));
            return true;
        }

        Subcommand sc = subs.get(key);
        if (sc == null) {
            sender.sendMessage(MM.deserialize("<red>Handler not found for:</red> <white>" + key + "</white>"));
            return true;
        }

        // permission / player-only gates
        String perm = sc.getPermission();
        if (perm != null && !perm.isEmpty() && !sender.hasPermission(perm)) {
            sender.sendMessage(MM.deserialize("<red>You don't have permission:</red> <white>" + perm + "</white>"));
            return true;
        }
        if (sc.playerOnly() && !(sender instanceof Player)) {
            sender.sendMessage(MM.deserialize("<red>This subcommand can only be used in-game.</red>"));
            return true;
        }

        return sc.execute(sender, shift(args));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length <= 1) {
            String typed = (args.length == 0 ? "" : args[0].toLowerCase(Locale.ROOT));
            Set<String> candidates = new LinkedHashSet<>(subs.keySet());
            candidates.addAll(aliases.keySet());
            return candidates.stream()
                    .filter(k -> k.startsWith(typed))
                    .map(k -> subs.containsKey(k) ? k : aliases.getOrDefault(k, k))
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
        }

        String key = resolveKey(args[0]);
        if (key == null) return Collections.emptyList();
        Subcommand sc = subs.get(key);
        if (sc == null) return Collections.emptyList();

        return sc.tabComplete(sender, shift(args));
    }
}