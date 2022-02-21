package es.eltrueno.npc.event;

import es.eltrueno.npc.TruenoNPC;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class TruenoNPCDespawnEvent extends TruenoNPCEvent {

    private static final HandlerList handlers = new HandlerList();

    public TruenoNPCDespawnEvent(Player player, TruenoNPC npc) {
        super(player, npc);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}
