package com.sergeantfuzzy.lorecraft.listeners;

import com.sergeantfuzzy.lorecraft.LoreCraft;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinLeaveListener implements Listener {
    private final LoreCraft plugin;
    public JoinLeaveListener(LoreCraft plugin) { this.plugin = plugin; }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= 60) { // 60 ticks = 3 seconds
                    cancel();
                    return;
                }
                player.getWorld().strikeLightningEffect(player.getLocation());
                ticks += 10;
            }
        }.runTaskTimer(plugin, 0L, 10L);

        if (!plugin.getConfig().getBoolean("join.enabled", true)) return;
        String raw = plugin.getConfig().getString("join.message", "<yellow>Welcome,</yellow> <white><player></white>");
        raw = raw.replace("<player>", e.getPlayer().getName());
        e.getPlayer().sendMessage(plugin.mm().deserialize(raw));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (!plugin.getConfig().getBoolean("leave.enabled", true)) return;
        String raw = plugin.getConfig().getString("leave.message", "<gray>Goodbye,</gray> <white><player></white>");
        raw = raw.replace("<player>", e.getPlayer().getName());
        // can't send to quitting player; broadcast nicely:
        e.quitMessage(plugin.mm().deserialize(raw));
    }
}