package com.sergeantfuzzy.lorecraft.commands.hologram;

import com.sergeantfuzzy.lorecraft.LoreCraft;
import com.sergeantfuzzy.lorecraft.commands.core.Subcommand;
import com.sergeantfuzzy.lorecraft.service.HologramService;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * /lc holo <create|edit|move|delete|multiline|toggle|see> <id> ...
 *
 * Storage intent (future): /LoreCraft/holograms/<world>/<hologram-id>.yml
 * Current service is in-memory + on-demand render.
 */
public class HologramCommand implements Subcommand {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private final LoreCraft plugin;

    public HologramCommand(LoreCraft plugin) {
        this.plugin = plugin;
    }

    // ---- Subcommand contract ----
    @Override public String getName() { return "holo"; }
    @Override public List<String> getAliases() { return Arrays.asList("hologram", "holograms"); }
    @Override public String getDescription() { return "Create and manage holograms."; }
    @Override public String getUsage() { return "/lc holo <create|edit|move|delete|multiline|toggle|see> <id> [args]"; }
    @Override public String getPermission() { return "lorecraft.holo"; }

    // ---- Execute ----
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            msg(sender, "<red>Usage:</red> " + getUsage());
            return true;
        }

        final String action = args[0].toLowerCase(Locale.ROOT);
        switch (action) {
            case "create":     return onCreate(sender, slice(args, 1));
            case "edit":       return onEdit(sender, slice(args, 1));
            case "move":       return onMove(sender, slice(args, 1));
            case "delete":     return onDelete(sender, slice(args, 1));
            case "multiline":  return onMultiline(sender, slice(args, 1));
            case "toggle":     return onToggle(sender, slice(args, 1));   // stub → refresh
            case "see":        return onSee(sender, slice(args, 1));      // stub
            default:
                msg(sender, "<red>Unknown action:</red> <yellow>" + action + "</yellow>");
                msg(sender, "<gray>Try:</gray> <white>create, edit, move, delete, multiline, toggle, see</white>");
                return true;
        }
    }

    // ---------- Actions ----------

    // /lc holo create <id> [line...]
    private boolean onCreate(CommandSender sender, String[] args) {
        if (!(sender instanceof Player p)) {
            msg(sender, "<red>Players only.</red>");
            return true;
        }
        if (!checkPerm(sender, "lorecraft.holo.create")) return true;

        if (args.length < 1) {
            msg(sender, "<red>Usage:</red> /lc holo create <id> [first line...]");
            return true;
        }
        final String id = args[0];
        final String line = (args.length > 1) ? join(args, 1) : "<gray><italic>(empty line)</italic></gray>";

        HologramService svc = plugin.hologramService();
        Location loc = p.getLocation();
        svc.create(id, loc, line); // matches your service signature

        msg(sender, "<green>✔ Created hologram</green> <white>" + id + "</white> "
                + "<gray>@</gray> <yellow>" + fmtLoc(loc) + " in " + loc.getWorld().getName() + "</yellow>");
        return true;
    }

    // /lc holo edit <id> <set|add|insert|remove> [index] [text...]
    private boolean onEdit(CommandSender sender, String[] args) {
        if (!checkPerm(sender, "lorecraft.holo.edit")) return true;
        if (args.length < 2) {
            msg(sender, "<red>Usage:</red> /lc holo edit <id> <set|add|insert|remove> [index] [text...]");
            return true;
        }
        final String id = args[0];
        final String mode = args[1].toLowerCase(Locale.ROOT);

        HologramService svc = plugin.hologramService();

        // Get current lines (via helper you’ll add below)
        List<String> current;
        try {
            current = new ArrayList<>(svc.getLines(id));
        } catch (Throwable t) {
            msg(sender, "<red>Hologram not found:</red> <yellow>" + id + "</yellow>");
            return true;
        }

        switch (mode) {
            case "set": {
                if (args.length < 4) {
                    msg(sender, "<red>Usage:</red> /lc holo edit " + id + " set <index> <text...>");
                    return true;
                }
                int index = parseIndex(args[2], current.size());
                if (index < 0 || index >= current.size()) {
                    msg(sender, "<red>Index out of range.</red> <gray>0-" + (current.size() - 1) + "</gray>");
                    return true;
                }
                String text = join(args, 3);
                current.set(index, text);
                break;
            }
            case "add": {
                if (args.length < 3) {
                    msg(sender, "<red>Usage:</red> /lc holo edit " + id + " add <text...>");
                    return true;
                }
                String text = join(args, 2);
                current.add(text);
                break;
            }
            case "insert": {
                if (args.length < 4) {
                    msg(sender, "<red>Usage:</red> /lc holo edit " + id + " insert <index> <text...>");
                    return true;
                }
                int index = parseIndex(args[2], current.size() + 1);
                if (index < 0 || index > current.size()) {
                    msg(sender, "<red>Index out of range.</red> <gray>0-" + current.size() + "</gray>");
                    return true;
                }
                String text = join(args, 3);
                current.add(index, text);
                break;
            }
            case "remove": {
                if (args.length < 3) {
                    msg(sender, "<red>Usage:</red> /lc holo edit " + id + " remove <index>");
                    return true;
                }
                int index = parseIndex(args[2], current.size());
                if (index < 0 || index >= current.size()) {
                    msg(sender, "<red>Index out of range.</red> <gray>0-" + (current.size() - 1) + "</gray>");
                    return true;
                }
                current.remove(index);
                break;
            }
            default:
                msg(sender, "<red>Unknown edit mode:</red> <yellow>" + mode + "</yellow>");
                msg(sender, "<gray>Use:</gray> <white>set, add, insert, remove</white>");
                return true;
        }

        svc.setMultiline(id, current); // re-renders internally
        msg(sender, "<green>✔ Updated lines for</green> <white>" + id + "</white> "
                + "<gray>(now " + current.size() + " line" + (current.size() == 1 ? "" : "s") + ")</gray>");
        return true;
    }

    // /lc holo move <id>
    private boolean onMove(CommandSender sender, String[] args) {
        if (!(sender instanceof Player p)) {
            msg(sender, "<red>Players only.</red>");
            return true;
        }
        if (!checkPerm(sender, "lorecraft.holo.move")) return true;

        if (args.length < 1) {
            msg(sender, "<red>Usage:</red> /lc holo move <id>");
            return true;
        }
        final String id = args[0];

        HologramService svc = plugin.hologramService();
        try {
            svc.move(id, p.getLocation());
        } catch (IllegalArgumentException ex) {
            msg(sender, "<red>Hologram not found:</red> <yellow>" + id + "</yellow>");
            return true;
        }
        msg(sender, "<green>✔ Moved</green> <white>" + id + "</white> <gray>-></gray> <yellow>" + fmtLoc(p.getLocation()) + "</yellow>");
        return true;
    }

    // /lc holo delete <id>
    private boolean onDelete(CommandSender sender, String[] args) {
        if (!checkPerm(sender, "lorecraft.holo.delete")) return true;
        if (args.length < 1) {
            msg(sender, "<red>Usage:</red> /lc holo delete <id>");
            return true;
        }
        final String id = args[0];

        HologramService svc = plugin.hologramService();
        svc.delete(id);
        msg(sender, "<green>✔ Deleted</green> <white>" + id + "</white>");
        return true;
    }

    // /lc holo multiline <id>
    private boolean onMultiline(CommandSender sender, String[] args) {
        if (!(sender instanceof Player p)) {
            msg(sender, "<red>Players only.</red>");
            return true;
        }
        if (!checkPerm(sender, "lorecraft.holo.gui")) return true;

        if (args.length < 1) {
            msg(sender, "<red>Usage:</red> /lc holo multiline <id>");
            return true;
        }
        final String id = args[0];

        try {
            plugin.hologramGui().open(p, id);
            msg(sender, "<gray>Opened editor for</gray> <white>" + id + "</white>.");
        } catch (Throwable t) {
            msg(sender, "<yellow>No GUI available.</yellow> <gray>Use:</gray> /lc holo edit " + id + " ...");
        }
        return true;
    }

    // /lc holo toggle <id> [on|off] — stub: refresh only
    private boolean onToggle(CommandSender sender, String[] args) {
        if (!checkPerm(sender, "lorecraft.holo.toggle")) return true;
        if (args.length < 1) {
            msg(sender, "<red>Usage:</red> /lc holo toggle <id> [on|off]");
            return true;
        }
        final String id = args[0];
        msg(sender, "<yellow>Heads up:</yellow> enable/disable isn’t implemented yet. Re-rendering instead.");
        try {
            plugin.hologramService().show(id);
            msg(sender, "<green>✔ Refreshed</green> <white>" + id + "</white>");
        } catch (Throwable t) {
            msg(sender, "<red>Hologram not found:</red> <yellow>" + id + "</yellow>");
        }
        return true;
    }

    // /lc holo see <id> [player] — stub (per-player visibility not implemented)
    private boolean onSee(CommandSender sender, String[] args) {
        if (!checkPerm(sender, "lorecraft.holo.see")) return true;
        if (args.length < 1) {
            msg(sender, "<red>Usage:</red> /lc holo see <id> [player]");
            return true;
        }
        final String id = args[0];

        Player target;
        if (args.length >= 2) {
            target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                msg(sender, "<red>Player not found:</red> <yellow>" + args[1] + "</yellow>");
                return true;
            }
        } else {
            if (!(sender instanceof Player p)) {
                msg(sender, "<red>Usage:</red> /lc holo see <id> <player>");
                return true;
            }
            target = p;
        }

        msg(sender, "<yellow>Per-player visibility isn’t implemented yet.</yellow> Re-rendering globally.");
        try {
            plugin.hologramService().show(id);
        } catch (Throwable t) {
            msg(sender, "<red>Hologram not found:</red> <yellow>" + id + "</yellow>");
        }
        return true;
    }

    // ---------- Tab Complete ----------
    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        HologramService svc = null;
        try { svc = plugin.hologramService(); } catch (Throwable ignored) {}

        if (args.length == 1) {
            return Arrays.asList("create","edit","move","delete","multiline","toggle","see")
                    .stream().filter(s -> s.startsWith(args[0].toLowerCase(Locale.ROOT))).collect(Collectors.toList());
        }

        String sub = args[0].toLowerCase(Locale.ROOT);
        if (args.length == 2) {
            if ("create".equals(sub)) return Collections.emptyList();
            if (svc != null) {
                try {
                    return svc.ids().stream()
                            .filter(id -> id.toLowerCase(Locale.ROOT).startsWith(args[1].toLowerCase(Locale.ROOT)))
                            .sorted().collect(Collectors.toList());
                } catch (Throwable ignored) {}
            }
        }

        if (args.length == 3 && sub.equals("edit")) {
            return Arrays.asList("set","add","insert","remove").stream()
                    .filter(s -> s.startsWith(args[2].toLowerCase(Locale.ROOT)))
                    .collect(Collectors.toList());
        }

        if (args.length == 3 && sub.equals("toggle")) {
            return Arrays.asList("on","off").stream()
                    .filter(s -> s.startsWith(args[2].toLowerCase(Locale.ROOT)))
                    .collect(Collectors.toList());
        }

        if (args.length == 3 && sub.equals("see")) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName)
                    .filter(n -> n.toLowerCase(Locale.ROOT).startsWith(args[2].toLowerCase(Locale.ROOT)))
                    .sorted()
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    // ---------- Helpers ----------
    private static String[] slice(String[] arr, int from) {
        if (from >= arr.length) return new String[0];
        return Arrays.copyOfRange(arr, from, arr.length);
    }

    private static String join(String[] arr, int from) {
        return String.join(" ", Arrays.copyOfRange(arr, from, arr.length));
    }

    private boolean checkPerm(CommandSender sender, String perm) {
        String need = (perm == null || perm.isEmpty()) ? getPermission() : perm;
        if (need == null || need.isEmpty() || sender.hasPermission(need) || sender.hasPermission("lorecraft.*")) return true;
        msg(sender, "<red>You lack permission:</red> <gray>" + need + "</gray>");
        return false;
    }

    private void msg(CommandSender to, String mm) {
        try {
            to.sendMessage(plugin.mm().deserialize(mm));
        } catch (Throwable ignored) {
            to.sendMessage(MM.deserialize(mm).toString());
        }
    }

    private static String fmtLoc(Location l) {
        return String.format(Locale.ROOT, "%.2f, %.2f, %.2f", l.getX(), l.getY(), l.getZ());
    }

    private static int parseIndex(String raw, int size) {
        try { return Integer.parseInt(raw); } catch (NumberFormatException e) { return -1; }
    }
}