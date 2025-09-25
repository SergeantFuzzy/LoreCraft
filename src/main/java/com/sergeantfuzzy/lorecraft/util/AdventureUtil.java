package com.sergeantfuzzy.lorecraft.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class AdventureUtil {
    private static final MiniMessage MM = MiniMessage.miniMessage();

    private AdventureUtil() {}

    public static Component mm(String miniMessage) {
        return MM.deserialize(miniMessage);
    }

    // ✅ Accept TagResolver... (e.g., Placeholder.unparsed(...) returns TagResolver)
    public static Component mm(String miniMessage, TagResolver... resolvers) {
        return MM.deserialize(miniMessage, resolvers);
    }

    public static void tell(CommandSender to, String miniMessage, TagResolver... resolvers) {
        to.sendMessage(mm(miniMessage, resolvers));
    }

    public static void tellRaw(CommandSender to, String miniMessage, TagResolver... resolvers) {
        to.sendMessage(mm(miniMessage, resolvers));
    }

    public static void actionBar(Player p, String miniMessage, TagResolver... resolvers) {
        p.sendActionBar(mm(miniMessage, resolvers));
    }

    public static void title(Player p, String titleMini, String subtitleMini) {
        p.showTitle(Title.title(mm(titleMini), mm(subtitleMini)));
    }
}