//
// Decompiled by Procyon v0.5.36
//

package es.eltrueno.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public interface TruenoHologram {
    void setupWorldHologram(final Location p0, final ArrayList<String> p1);

    void setupPlayerHologram(final Player p0, final Location p1, final ArrayList<String> p2);

    Location getLocation();

    Player getPlayer();

    ArrayList<Integer> getEntitiesIds();

    void setDistanceBetweenLines(final Double p0);

    void display();

    void update(final ArrayList<String> p0);

    void updateLine(final int p0, final String p1);

    void removeLine(final int p0);

    void delete();
}
