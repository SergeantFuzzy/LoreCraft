package com.sergeantfuzzy.lorecraft.service;


import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


// ───────────────────────────────── DialogueService
public class DialogueService {
    private final Plugin plugin;
    // dialogueId -> display name / metadata (stub)
    private final Map<String, String> dialogues = new ConcurrentHashMap<>();
    // npcId -> dialogueId
    private final Map<Integer, String> links = new ConcurrentHashMap<>();


    public DialogueService(Plugin plugin) { this.plugin = plugin; }


    public boolean hasTree(String id) { return dialogues.containsKey(id); }
    public void create(String id, String title) { dialogues.put(id, title == null ? id : title); }
    public Set<String> list() { return dialogues.keySet(); }


    public void link(int npcId, String dialogueId) { links.put(npcId, dialogueId); }
    public void unlink(int npcId) { links.remove(npcId); }
    public Optional<String> linkedDialogue(int npcId) { return Optional.ofNullable(links.get(npcId)); }


    public void start(Player player, String dialogueId) {
        if (!hasTree(dialogueId)) throw new IllegalArgumentException("Unknown dialogue: " + dialogueId);
        player.sendMessage(ChatColor.GOLD + "[LoreCraft] Dialogue: " + dialogueId + ChatColor.GRAY + " started.");
        // TODO: Open Dialogue GUI flow; set vars; trigger hooks
    }
}