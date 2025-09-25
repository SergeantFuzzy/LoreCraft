// hook to open BookGui
package com.sergeantfuzzy.lorecraft.commands.books;


import com.sergeantfuzzy.lorecraft.commands.core.Subcommand;
import com.sergeantfuzzy.lorecraft.commands.core.CommandCategory;
import com.sergeantfuzzy.lorecraft.gui.books.BookGui;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


import java.util.List;


public class BookGuiCommand implements Subcommand {
    private final Plugin plugin;
    public BookGuiCommand(Plugin plugin) { this.plugin = plugin; }


    @Override public String getName() { return "book"; }
    @Override public String getDescription() { return "Books editor GUI"; }
    @Override public String getUsage() { return "/lc book gui"; }
    @Override public String getPermission() { return "lorecraft.book.gui"; }
    @Override public List<String> getAliases() { return List.of("books"); }
    @Override public CommandCategory getCategory() { return CommandCategory.BOOKS; }
    @Override public boolean playerOnly() { return true; }


    @Override public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player p)) return true;
        // Accept: /lc book (defaults to gui) or /lc book gui
        new BookGui(plugin).open(p);
        return true;
    }
}