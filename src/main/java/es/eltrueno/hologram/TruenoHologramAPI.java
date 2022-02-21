package es.eltrueno.hologram;

import es.eltrueno.protocol.protocollib.TruenoHologram_ProtocolLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.logging.Level;

public class TruenoHologramAPI {
    private static ArrayList<TruenoHologram> holograms;
    private static String version;

    static {
        TruenoHologramAPI.holograms = new ArrayList<TruenoHologram>();
    }

    public static ArrayList<TruenoHologram> getHolograms() {
        return TruenoHologramAPI.holograms;
    }

    private static void setupVersion() {
        try {
            TruenoHologramAPI.version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public static TruenoHologram getNewHologram() {
        if (TruenoHologramAPI.version == null) {
            setupVersion();
        }
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            TruenoHologram holo = new TruenoHologram_ProtocolLib();
            TruenoHologramAPI.holograms.add(holo);
            return holo;
        }
        Bukkit.getLogger().log(Level.SEVERE, ChatColor.RED + "Unsopported server version.");
        return null;
    }
}
