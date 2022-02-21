package com.chainz.core.serverdata;

import com.chainz.core.ChainZAPI;
import com.chainz.core.Core;
import org.bukkit.Bukkit;

public class ServerDataTask {
    public static void startTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Core.core, () -> {
            ChainZAPI.getServerData().setServerPlayers(Bukkit.getServer().getOnlinePlayers().size());
            ChainZAPI.getServerData().setServerMaxPlayers(Bukkit.getServer().getMaxPlayers());
        }, 0L, 8L);
    }
}
