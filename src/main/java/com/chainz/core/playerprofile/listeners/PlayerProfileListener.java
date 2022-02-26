package com.chainz.core.playerprofile.listeners;

import com.chainz.core.ChainZAPI;
import com.chainz.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerProfileListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(AsyncPlayerPreLoginEvent e) {
        ChainZAPI.getPlayerProfileManager().getPlayerProfileFromUUID(e.getUniqueId());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            ChainZAPI.getPlayerProfileManager().save(e.getPlayer().getUniqueId());
        });
    }
}