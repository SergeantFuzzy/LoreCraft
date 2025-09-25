package com.sergeantfuzzy.lorecraft.commands.general;

import com.sergeantfuzzy.lorecraft.LoreCraft;
import com.sergeantfuzzy.lorecraft.commands.core.Subcommand;
import org.bukkit.command.*;

import java.util.List;

public class PacksCommand implements Subcommand {
    private final LoreCraft plugin;
    public PacksCommand(LoreCraft plugin) { this.plugin = plugin; }

    @Override public String getName() { return "packs"; }
    @Override public String getDescription() { return "List or reload quest packs"; }
    @Override public String getUsage() { return "/lc packs [reload]"; }
    @Override public String getPermission() { return "lorecraft.admin"; }
    @Override public List<String> getAliases() { return List.of("packlist", "questpacks"); }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        var svc = plugin.packs();
        if (svc == null) {
            sender.sendMessage(plugin.mm().deserialize("<red>Packs service not available.</red>"));
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            svc.reload();
            sender.sendMessage(plugin.mm().deserialize("<green>✔ Packs reloaded.</green>"));
            return true;
        }
        if (svc.all().isEmpty()) {
            sender.sendMessage(plugin.mm().deserialize("<gray>No packs found in <white>plugins/LoreCraft/packs/</white></gray>"));
            return true;
        }
        sender.sendMessage(plugin.mm().deserialize("<gold>Loaded Packs:</gold>"));
        svc.all().forEach((id, rec) -> sender.sendMessage(plugin.mm().deserialize(
                "<yellow>- " + rec.meta.name + " <gray>(" + rec.meta.version + ")</gray> [<white>" + rec.totalFileCount() + "</white> files]"
        )));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return (args.length == 1) ? List.of("reload") : List.of();
    }
}