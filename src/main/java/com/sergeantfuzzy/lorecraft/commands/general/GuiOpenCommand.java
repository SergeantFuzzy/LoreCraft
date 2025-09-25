// "/lc" root opens MainGui
package com.sergeantfuzzy.lorecraft.commands.general;


import com.sergeantfuzzy.lorecraft.gui.MainGui;
import com.sergeantfuzzy.lorecraft.commands.core.Subcommand;
import com.sergeantfuzzy.lorecraft.commands.core.CommandCategory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


import java.util.List;


public class GuiOpenCommand implements Subcommand {
    private final Plugin plugin;
    public GuiOpenCommand(Plugin plugin) { this.plugin = plugin; }


    @Override public String getName() { return "gui"; }
    @Override public String getDescription() { return "Open the LoreCraft main GUI"; }
    @Override public String getUsage() { return "/lc"; }
    @Override public String getPermission() { return "lorecraft.gui.open"; }
    @Override public List<String> getAliases() { return List.of("open", "menu", "main"); }
    @Override public CommandCategory getCategory() { return CommandCategory.GENERAL; }
    @Override public boolean playerOnly() { return true; }


    @Override public boolean execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        new MainGui(plugin).open(p);
        return true;
    }
}