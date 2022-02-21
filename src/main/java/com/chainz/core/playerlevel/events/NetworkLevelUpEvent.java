package com.chainz.core.playerlevel.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NetworkLevelUpEvent extends Event {
    private static HandlerList handlers = new HandlerList();

    private Player p;
    private int oldexp;
    private int addedexp;
    private int newexp;
    private int oldlevel;
    private int newlevel;

    public NetworkLevelUpEvent(Player p, int oldexp, int addedexp, int newexp, int oldlevel, int newlevel) {
        this.p = p;
        this.oldexp = oldexp;
        this.addedexp = addedexp;
        this.newexp = newexp;
        this.oldlevel = oldlevel;
        this.newlevel = newlevel;
        /* p.sendMessage("");
        CenterText.sendCenteredMessage(p, "DE NIVEL!" );
        p.sendMessage("");
        CenterText.sendCenteredMessage(p, "eres " + newlevel);
        p.sendMessage("");
        CenterText.sendCenteredMessage(p, "puedes recoger tu recompensa!");
        p.sendMessage("");
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10000.0F, 1.0F);
        String title = "DE NIVEL!";
        String subtitle = "" + newlevel;
        p.sendTitle(title, subtitle, 0, 60, 60);*/
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

    public int getOldLevel() {
        return this.oldlevel;
    }

    public int getNewLevel() {
        return this.newlevel;
    }
}
