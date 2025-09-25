package com.sergeantfuzzy.lorecraft.service;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HologramService {
    private final Plugin plugin;
    private static final MiniMessage MM = MiniMessage.miniMessage();

    private static class Holo { Location loc; List<String> lines = new ArrayList<>(); }
    private final Map<String, Holo> holos = new ConcurrentHashMap<>();

    public HologramService(Plugin plugin) { this.plugin = plugin; }

    public void create(String id, Location loc, String text) {
        Holo h = holos.computeIfAbsent(id, k -> new Holo());
        h.loc = loc.clone();
        h.lines = new ArrayList<>(Collections.singletonList(text));
        show(id);
    }

    public void setMultiline(String id, List<String> lines) {
        Holo h = holos.get(id);
        if (h == null) throw new IllegalArgumentException("Unknown hologram: " + id);
        h.lines = new ArrayList<>(lines);
        show(id);
    }

    public void move(String id, Location to) {
        Holo h = holos.get(id);
        if (h == null) throw new IllegalArgumentException("Unknown hologram: " + id);
        h.loc = to.clone();
        show(id);
    }

    public void delete(String id) { holos.remove(id); }

    public boolean has(String id) {
        return holos.containsKey(id);
    }

    public java.util.Set<String> ids() {
        return new java.util.TreeSet<>(holos.keySet());
    }

    public java.util.List<String> getLines(String id) {
        Holo h = holos.get(id);
        if (h == null) throw new IllegalArgumentException("Unknown hologram: " + id);
        return new java.util.ArrayList<>(h.lines);
    }

    public void show(String id) {
        Holo h = holos.get(id);
        if (h == null) return;

        for (int i = 0; i < h.lines.size(); i++) {
            Location l = h.loc.clone().add(0, (h.lines.size() - 1 - i) * 0.25, 0);
            for (Player p : l.getWorld().getPlayers()) {
                // light particle accent
                p.spawnParticle(Particle.SOUL, l, 6, 0.1, 0.1, 0.1, 0.01);

                // render a “header + line” message to the player using MiniMessage
                String lineText = h.lines.get(i);
                p.sendMessage(MM.deserialize(
                        "<gold>[Holo]</gold> <white><line></white>",
                        // Treat stored lines as MiniMessage so gradients/hex/etc. work out of the box
                        Placeholder.parsed("line", lineText)
                ));
            }
        }
    }
}