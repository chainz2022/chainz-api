package com.chainz.core.async.adrewards.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AdRewardClaimEvent extends Event {
    private static HandlerList handlers;

    static {
        handlers = new HandlerList();
    }

    private Player p;

    public AdRewardClaimEvent(Player p) {
        this.p = p;
    }

    public static HandlerList getHandlerList() {
        return AdRewardClaimEvent.handlers;
    }

    public HandlerList getHandlers() {
        return AdRewardClaimEvent.handlers;
    }

    public Player getPlayer() {
        return this.p;
    }
}
