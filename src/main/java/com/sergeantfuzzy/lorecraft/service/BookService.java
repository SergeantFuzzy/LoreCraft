package com.sergeantfuzzy.lorecraft.service;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BookService {
    private final Plugin plugin;
    private final Map<String, List<String>> books = new ConcurrentHashMap<>();
    private static final MiniMessage MM = MiniMessage.miniMessage();

    public BookService(Plugin plugin) { this.plugin = plugin; }

    public void create(String id) { books.putIfAbsent(id, new ArrayList<>()); }

    public void addPage(String id, String page) {
        books.computeIfAbsent(id, k -> new ArrayList<>()).add(page);
    }

    public void delete(String id) { books.remove(id); }

    public Set<String> list() { return books.keySet(); }

    public void open(Player p, String id) {
        List<String> pages = books.get(id);
        if (pages == null || pages.isEmpty()) {
            p.sendMessage(MM.deserialize(
                    "<red>Book has no pages:</red> <white><id></white>",
                    Placeholder.unparsed("id", id)
            ));
            return;
        }

        // Header
        p.sendMessage(MM.deserialize("<gold>[Book]</gold> <white><id></white>", Placeholder.unparsed("id", id)));

        // Each page (page text may itself be MiniMessage; if you want it literal, wrap with <reset> or escape)
        for (int i = 0; i < pages.size(); i++) {
            int pageNum = i + 1;
            String page = pages.get(i);
            Component line = MM.deserialize(
                    "<yellow>Page <n>:</yellow> <white><content></white>",
                    Placeholder.unparsed("n", String.valueOf(pageNum)),
                    Placeholder.parsed("content", page) // interpret stored page text as MiniMessage
            );
            p.sendMessage(line);
        }
    }
}