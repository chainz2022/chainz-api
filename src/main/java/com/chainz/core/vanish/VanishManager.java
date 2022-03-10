package com.chainz.core.vanish;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class VanishManager {
    private static final HashMap<Player, Boolean> vanishes = new HashMap<>();

    private static final HashMap<Player, Boolean> hideAll = new HashMap<>();

    public static boolean isVanished(Player p) {
        return VanishManager.vanishes.containsKey(p) && VanishManager.vanishes.get(p) && VanishManager.vanishes.get(p);
    }

    public static void setVanished(Player p, boolean bol) {
        if (VanishManager.vanishes.containsKey(p)) {
            VanishManager.vanishes.replace(p, bol);
        } else {
            VanishManager.vanishes.put(p, bol);
        }
    }

    public static void hideAll(Player p, boolean bol) {
        if (VanishManager.hideAll.containsKey(p)) {
            VanishManager.hideAll.replace(p, bol);
        } else {
            VanishManager.hideAll.put(p, bol);
        }
        
        if (bol) {
            vanishAllFrom(p, true);
        } else {
            showAllTo(p, true);
        }
    }

    public static void showAllTo(Player p, boolean showStaff) {
        for (Player o : Bukkit.getOnlinePlayers()) {
            if (p != o) {
                p.showPlayer(o);
            }
        }
    }

    public static void vanishAllFrom(Player p, boolean showStaff) {
        for (Player o : Bukkit.getOnlinePlayers()) {
            if (p != o) {
                if (showStaff && o.hasPermission("chainz.staff")) {
                    continue;
                }
                p.hidePlayer(o);
            }
        }
    }
}
