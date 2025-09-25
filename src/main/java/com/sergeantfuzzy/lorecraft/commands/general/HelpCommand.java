package com.sergeantfuzzy.lorecraft.commands.general;

import com.sergeantfuzzy.lorecraft.LoreCraft;
import com.sergeantfuzzy.lorecraft.commands.core.Subcommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpCommand implements Subcommand {
    private final LoreCraft plugin;
    public HelpCommand(LoreCraft plugin) { this.plugin = plugin; }

    @Override public String getName() { return "help"; }
    @Override public String getDescription() { return "Show help for commands."; }
    @Override public String getUsage() { return "/lc help [page]"; }
    @Override public String getPermission() { return "lorecraft.help"; }
    @Override public List<String> getAliases() { return List.of("?"); }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        int page = 1;
        try { if (args.length > 0) page = Math.max(1, Integer.parseInt(args[0])); } catch (NumberFormatException ignored) {}

        // Minimal static help for M1; we’ll generate from registry later
        List<String> entries = List.of(
                "help|Show help for commands.",
                "about|Show plugin info & credits.",
                "reload|Reload configs (admin)."
        );

        int perPage = 6;
        int pages = Math.max(1, (int) Math.ceil(entries.size() / (double) perPage));
        page = Math.min(page, pages);

        sender.sendMessage(plugin.mm().deserialize(
                plugin.lang().str("help.title")
                        .replace("{PAGE}", String.valueOf(page))
                        .replace("{PAGES}", String.valueOf(pages))
        ));

        int start = (page - 1) * perPage;
        int end = Math.min(entries.size(), start + perPage);
        for (int i = start; i < end; i++) {
            String[] parts = entries.get(i).split("\\|", 2);
            String line = plugin.lang().str("help.entry")
                    .replace("{CMD}", parts[0])
                    .replace("{DESC}", parts[1]);
            sender.sendMessage(plugin.mm().deserialize(line));
        }

        if (page < pages) {
            String footer = plugin.lang().str("help.footer")
                    .replace("{NEXT}", String.valueOf(page + 1));
            sender.sendMessage(plugin.mm().deserialize(footer));
        }
        return true;
    }
}