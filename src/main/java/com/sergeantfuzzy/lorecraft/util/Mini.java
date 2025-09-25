package com.sergeantfuzzy.lorecraft.util;

import net.kyori.adventure.text.minimessage.MiniMessage;

// If we relocate MiniMessage, centralize the creation here.
public final class Mini {
    private Mini() {}
    public static MiniMessage mini() {
        return MiniMessage.miniMessage();
    }
}