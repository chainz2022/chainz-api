package com.chainz.core.vanish;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class VanishManager {
    private static HashMap<Player, Boolean> vanishes;

    static {
        VanishManager.vanishes = new HashMap<Player, Boolean>();
    }

    public static boolean isVanished(final Player p) {
        return VanishManager.vanishes.containsKey(p) && VanishManager.vanishes.get(p) && VanishManager.vanishes.get(p);
    }

    public static void setVanished(final Player p, final Boolean bol) {
        if (VanishManager.vanishes.containsKey(p)) {
            VanishManager.vanishes.replace(p, bol);
        } else {
            VanishManager.vanishes.put(p, bol);
        }
    }
}
