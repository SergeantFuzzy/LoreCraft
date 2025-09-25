package com.sergeantfuzzy.lorecraft.commands.core;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public interface Subcommand {
    String getName();
    String getDescription();
    String getUsage();
    String getPermission();

    /** Optional aliases. */
    default List<String> getAliases() { return Collections.emptyList(); }


    /** Category for help UI. */
    default CommandCategory getCategory() { return CommandCategory.GENERAL; }


    /** If true, restrict to Player senders. */
    default boolean playerOnly() { return false; }


    /** Execute implementation. Return true on handled. */
    boolean execute(CommandSender sender, String[] args);


    /** Tab suggestions. */
    default List<String> tabComplete(CommandSender sender, String[] args) { return Collections.emptyList(); }
}