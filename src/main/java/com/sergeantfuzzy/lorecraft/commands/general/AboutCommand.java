package com.sergeantfuzzy.lorecraft.commands.general;

import com.sergeantfuzzy.lorecraft.LoreCraft;
import com.sergeantfuzzy.lorecraft.commands.core.Subcommand;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

public class AboutCommand implements Subcommand {
    private final LoreCraft plugin;
    public AboutCommand(LoreCraft plugin) { this.plugin = plugin; }

    @Override public String getName() { return "about"; }
    @Override public String getDescription() { return "Show plugin info & credits."; }
    @Override public String getUsage() { return "/lc about"; }
    @Override public String getPermission() { return "lorecraft.about"; }
    @Override public List<String> getAliases() { return List.of("info", "version"); }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        var desc = plugin.getDescription();
        var ph = Map.of(
                "VERSION", desc.getVersion(),
                "DESC", desc.getDescription(),
                "AUTHORS", String.join(", ", desc.getAuthors()),
                "WEBSITE", desc.getWebsite() == null ? "n/a" : desc.getWebsite()
        );
        for (String line : plugin.lang().lines("about.lines")) {
            sender.sendMessage(plugin.mm().deserialize(apply(line, ph)));
        }
        return true;
    }

    private String apply(String raw, Map<String, ?> ph) {
        String out = raw;
        for (var e : ph.entrySet()) out = out.replace("{"+e.getKey()+"}", String.valueOf(e.getValue()));
        return out;
    }
}