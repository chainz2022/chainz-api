package es.eltrueno.hologram.event;

import es.eltrueno.hologram.TruenoHologram;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class TruenoHologramClickEvent extends TruenoHologramEvent {
    private static final HandlerList handlers;

    static {
        handlers = new HandlerList();
    }

    public TruenoHologramClickEvent(final Player player, final TruenoHologram hologram) {
        super(player, hologram);
    }

    public static HandlerList getHandlerList() {
        return TruenoHologramClickEvent.handlers;
    }

    public HandlerList getHandlers() {
        return TruenoHologramClickEvent.handlers;
    }
}
