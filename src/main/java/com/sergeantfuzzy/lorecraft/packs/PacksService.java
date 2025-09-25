package com.sergeantfuzzy.lorecraft.packs;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.Yaml;

public class PacksService {
    public static final String PACKS_RELATIVE_DIR = "packs";
    private final Plugin plugin;
    private final Map<String, PackRecord> byId = new LinkedHashMap<>();

    public PacksService(Plugin plugin) {
        this.plugin = plugin;
    }

    public Map<String, PackRecord> all() { return Collections.unmodifiableMap(byId); }

    public File packsRoot() {
        return new File(plugin.getDataFolder(), PACKS_RELATIVE_DIR);
    }

    /** Load (or reload) packs from plugins/LoreCraft/packs/ */
    public void reload() {
        byId.clear();
        File root = packsRoot();
        if (!root.exists()) root.mkdirs();

        List<File> packYmls = findPackYmls(root);
        if (packYmls.isEmpty()) {
            plugin.getLogger().info("[LoreCraft] No pack.yml files found under " + root.getPath());
            return;
        }

        for (File packYml : packYmls) {
            try (FileInputStream in = new FileInputStream(packYml)) {
                Map<String,Object> raw = new Yaml().load(in);
                PackMeta meta = PackMeta.from(raw);

                if (meta.id == null || meta.id.isBlank()) {
                    plugin.getLogger().warning("[LoreCraft] pack.yml missing 'id' at: " + packYml);
                    continue;
                }
                File packDir = packYml.getParentFile();

                PackRecord rec = new PackRecord(meta, packDir);
                rec.files.dialogues = resolveList(packDir, raw, "load.dialogues");
                rec.files.guis      = resolveList(packDir, raw, "load.guis");
                rec.files.scenes    = resolveList(packDir, raw, "load.scenes");
                rec.files.books     = resolveList(packDir, raw, "load.books");
                rec.files.quests    = resolveList(packDir, raw, "load.quests");
                rec.files.triggers  = resolveList(packDir, raw, "load.triggers");
                rec.files.holograms = resolveList(packDir, raw, "load.holograms");

                byId.put(meta.id, rec);
                plugin.getLogger().info(String.format(
                        "[LoreCraft] Loaded quest pack: %s (%s) -> %d files",
                        meta.id, meta.version, rec.totalFileCount()));
            } catch (Exception ex) {
                plugin.getLogger().severe("[LoreCraft] Failed parsing " + packYml + " : " + ex.getMessage());
            }
        }
    }

    private static List<File> findPackYmls(File root) {
        List<File> out = new ArrayList<>();
        Deque<File> q = new ArrayDeque<>();
        q.add(root);
        while (!q.isEmpty()) {
            File f = q.poll();
            File[] kids = f.listFiles();
            if (kids == null) continue;
            for (File k : kids) {
                if (k.isDirectory()) q.add(k);
                else if (k.getName().equalsIgnoreCase("pack.yml")) out.add(k);
            }
        }
        return out;
    }

    @SuppressWarnings("unchecked")
    private static List<File> resolveList(File packDir, Map<String,Object> root, String path) {
        Object cur = root;
        for (String p : path.split("\\.")) {
            if (!(cur instanceof Map)) return List.of();
            cur = ((Map<String,Object>) cur).get(p);
            if (cur == null) return List.of();
        }
        if (!(cur instanceof List)) return List.of();
        List<Object> items = (List<Object>) cur;
        return items.stream()
                .map(Object::toString)
                .map(rel -> new File(packDir, rel))
                .collect(Collectors.toList());
    }

    // ---------- Models ----------
    public static class PackMeta {
        public final String id, name, version;
        public final boolean enabled;

        public PackMeta(String id, String name, String version, boolean enabled) {
            this.id = id; this.name = name; this.version = version; this.enabled = enabled;
        }

        @SuppressWarnings("unchecked")
        public static PackMeta from(Map<String,Object> raw) {
            String id = String.valueOf(raw.getOrDefault("id","")).trim();
            String name = String.valueOf(raw.getOrDefault("name", id)).trim();
            String version = String.valueOf(raw.getOrDefault("version", "1.0.0"));
            boolean enabled = Boolean.parseBoolean(String.valueOf(raw.getOrDefault("enabled", "true")));
            return new PackMeta(id, name, version, enabled);
        }
    }

    public static class PackFiles {
        public List<File> dialogues = List.of();
        public List<File> guis      = List.of();
        public List<File> scenes    = List.of();
        public List<File> books     = List.of();
        public List<File> quests    = List.of();
        public List<File> triggers  = List.of();
        public List<File> holograms = List.of();
    }

    public static class PackRecord {
        public final PackMeta meta;
        public final File dir;
        public final PackFiles files = new PackFiles();

        public PackRecord(PackMeta meta, File dir) { this.meta = meta; this.dir = dir; }
        public int totalFileCount() {
            return files.dialogues.size() + files.guis.size() + files.scenes.size() +
                    files.books.size() + files.quests.size() + files.triggers.size() +
                    files.holograms.size();
        }
    }
}