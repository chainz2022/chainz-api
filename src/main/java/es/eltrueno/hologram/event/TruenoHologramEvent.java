package es.eltrueno.hologram.event;

import es.eltrueno.hologram.TruenoHologram;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class TruenoHologramEvent extends Event {
    private final Player player;
    private final TruenoHologram hologram;

    public TruenoHologramEvent(final Player player, final TruenoHologram hologram) {
        this.player = player;
        this.hologram = hologram;
    }

    public Player getPlayer() {
        return this.player;
    }

    public TruenoHologram getHologram() {
        return this.hologram;
    }
}
