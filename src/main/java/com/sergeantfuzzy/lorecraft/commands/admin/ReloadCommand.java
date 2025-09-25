package com.sergeantfuzzy.lorecraft.commands.admin;

import com.sergeantfuzzy.lorecraft.LoreCraft;
import com.sergeantfuzzy.lorecraft.commands.core.Subcommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand implements Subcommand {
    private final LoreCraft plugin;
    public ReloadCommand(LoreCraft plugin) { this.plugin = plugin; }

    @Override public String getName() { return "reload"; }
    @Override public String getDescription() { return "Reload configs and language."; }
    @Override public String getUsage() { return "/lc reload"; }
    @Override public String getPermission() { return "lorecraft.admin.reload"; }
    @Override public List<String> getAliases() { return List.of(); }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        try {
            plugin.reloadConfig();
            plugin.lang().reload();
            sender.sendMessage(plugin.mm().deserialize(plugin.lang().str("reload.ok")));
        } catch (Exception e) {
            sender.sendMessage(plugin.mm().deserialize(plugin.lang().str("reload.fail")));
            plugin.getLogger().severe("Reload failed: " + e.getMessage());
        }
        return true;
    }
}