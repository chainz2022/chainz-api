package com.chainz.core.playerlevel.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NetworkExperienceAddedEvent extends Event {
    private static HandlerList handlers = new HandlerList();

    private Player p;
    private int oldexp;
    private int addedexp;
    private int newexp;
    private int level;

    public NetworkExperienceAddedEvent(Player p, int oldexp, int addedexp, int newexp, int level) {
        this.p = p;
        this.oldexp = oldexp;
        this.addedexp = addedexp;
        this.newexp = newexp;
        this.level = level;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return this.p;
    }

    public int getOldExp() {
        return this.oldexp;
    }

    public int getAddedExp() {
        return this.addedexp;
    }

    public int getNewExp() {
        return this.newexp;
    }

    public int getLevel() {
        return this.level;
    }
}
