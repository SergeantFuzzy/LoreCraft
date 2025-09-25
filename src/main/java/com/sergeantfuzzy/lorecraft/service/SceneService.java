package com.sergeantfuzzy.lorecraft.service;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SceneService {
    private final Plugin plugin;
    private final Map<String, SceneRecording> scenes = new ConcurrentHashMap<>();
    private static final MiniMessage MM = MiniMessage.miniMessage();

    // Minimal recording container so this class compiles stand-alone
    private static final class SceneRecording {
        final String id;
        final List<Runnable> actions = new ArrayList<>();
        SceneRecording(String id) { this.id = id; }
    }

    public SceneService(Plugin plugin) { this.plugin = plugin; }

    public void create(String id) { scenes.putIfAbsent(id, new SceneRecording(id)); }
    public boolean exists(String id) { return scenes.containsKey(id); }
    public Set<String> list() { return scenes.keySet(); }

    public void record(String id) {
        create(id);
        // Demo action so play() does something visible
        scenes.get(id).actions.add(() -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.showTitle(Title.title(
                        MM.deserialize("<gold>Scene:</gold> <white>" + id + "</white>"),
                        MM.deserialize("<yellow>(demo recording)</yellow>"),
                        Title.Times.times(Duration.ofMillis(200), Duration.ofMillis(1600), Duration.ofMillis(300))
                ));
                p.playSound(p.getLocation(), Sound.UI_TOAST_IN, 0.8f, 1.0f);
            }
        });
    }

    public void play(String id) {
        SceneRecording rec = scenes.get(id);
        if (rec == null) throw new IllegalArgumentException("Unknown scene: " + id);
        Bukkit.getScheduler().runTask(plugin, () -> rec.actions.forEach(Runnable::run));
    }

    public void stopAll() {
        // If you schedule repeating tasks for real timelines, cancel them here.
    }
}