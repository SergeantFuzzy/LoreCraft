package com.sergeantfuzzy.lorecraft.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class YamlResourceControl {
    private final Plugin plugin;
    private final String fileName;
    private final File file;
    private final YamlConfiguration cfg;
    private final MiniMessage mm = Mini.mini();

    public YamlResourceControl(Plugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.file = new File(plugin.getDataFolder(), fileName);
        this.cfg = new YamlConfiguration();
        reload();
    }

    public void reload() {
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
        try {
            cfg.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().severe("Failed to load " + fileName + ": " + e.getMessage());
        }
    }

    // String fetchers
    public String str(String path, String def) {
        String s = cfg.getString(path);
        return (s == null || s.isEmpty()) ? def : s;
    }
    public String str(String path) { return str(path, path); }

    public List<String> lines(String path) {
        List<String> list = cfg.getStringList(path);
        return (list == null || list.isEmpty()) ? List.of(str(path)) : list;
    }

    // Placeholder applier (simple {KEY} replace)
    private String apply(String raw, Map<String, Object> ph) {
        String out = raw;
        if (ph != null) {
            for (var e : ph.entrySet()) {
                out = out.replace("{" + e.getKey() + "}", String.valueOf(e.getValue()));
            }
        }
        return out;
    }

    // MiniMessage convenience
    public Component mmc(String path) { return mm.deserialize(str(path)); }
    public Component mmc(String path, Map<String, Object> ph) {
        return mm.deserialize(apply(str(path), ph));
    }
}