package com.chainz.core.arena;

import org.bukkit.entity.Player;

public interface ArenaData {
    String getJoinableArena(ArenaType p0, Player p1);

    boolean sendToServerPubSub(Player p0, CachedArena p1);

    boolean sendToServerPubSub(Player p0, String p1, String p2);

    boolean joinRandomArena(ArenaType p0, Player p1);

    boolean joinRandomArenaWithQueue(ArenaType p0, Player p1);

    void processQueue();
}
