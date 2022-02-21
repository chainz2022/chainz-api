package com.chainz.core.arena;

import com.chainz.core.ChainZAPI;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.Date;

public class CachedArena implements Comparable<CachedArena> {
    public ArenaStatus status;
    public ArenaType type;

    public int maxPlayers;
    public int currentPlayers;

    public int teamSize;
    public long lastUpdate;
    public String mapName;
    public String server;

    public CachedArena(String[] data) {
        this.server = data[0];
        this.mapName = data[1];
        this.status = ArenaStatus.valueOf(data[2]);
        this.type = ArenaType.valueOf(data[3]);
        this.maxPlayers = Integer.parseInt(data[4]);
        this.currentPlayers = Integer.parseInt(data[5]);
        this.teamSize = Integer.parseInt(data[6]);

        Date date = new Date();
        this.lastUpdate = date.getTime();
    }

    public ArenaStatus getStatus() {
        return status;
    }

    public ArenaType getType() {
        return type;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public String getMapName() {
        return mapName;
    }

    public String getServer() {
        return server;
    }

    public boolean isJoinable() {
        return this.getStatus() == ArenaStatus.WAITING || (this.getStatus() == ArenaStatus.STARTING && this.getCurrentPlayers() < this.getMaxPlayers());
    }

    public void addPlayer(Player player) {
        ChainZAPI.getArenaDataManager().sendToServerPubSub(player, this);
        ChainZAPI.sendToServer(player, getServer());
    }

    public void update(String[] data) {
        this.status = ArenaStatus.valueOf(data[2]);
        this.currentPlayers = Integer.parseInt(data[5]);

        Date date = new Date();
        this.lastUpdate = date.getTime();
    }

    public void update(ArenaStatus status, int currentPlayers) {
        this.status = status;
        this.currentPlayers = currentPlayers;

        Date date = new Date();
        this.lastUpdate = date.getTime();
    }

    @Override
    public int compareTo(CachedArena o) {
        return Comparator.comparingInt(CachedArena::getCurrentPlayers).thenComparingLong(CachedArena::getLastUpdate).thenComparing(CachedArena::isJoinable).compare(this, o);
    }
}
