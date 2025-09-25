package com.sergeantfuzzy.lorecraft.service;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class EffectService {
    private final Plugin plugin;
    public EffectService(Plugin plugin) { this.plugin = plugin; }


    public void particleScript(Player pivot, String preset) {
// Tiny built-in demo scripts; replace with your script loader
        Location base = pivot.getLocation().add(0, 1.2, 0);
        if ("sparks_spiral".equalsIgnoreCase(preset)) {
            for (int i = 0; i < 40; i++) {
                double t = i / 8.0; double r = 0.5 + (i * 0.01);
                double x = Math.cos(t) * r, z = Math.sin(t) * r;
                base.getWorld().spawnParticle(Particle.CRIT, base.clone().add(x, i * 0.03, z), 4, 0, 0, 0, 0);
            }
            base.getWorld().playSound(base, Sound.BLOCK_ANVIL_LAND, 0.7f, 1.2f);
        } else {
            base.getWorld().spawnParticle(Particle.ENCHANT, base, 60, 0.4, 0.6, 0.4, 0.0);
        }
    }


    public void soundAt(Location loc, Sound sound, float vol, float pitch) {
        loc.getWorld().playSound(loc, sound, vol, pitch);
    }
}