package es.eltrueno.npc;

import es.eltrueno.npc.skin.TruenoNPCSkin;
import es.eltrueno.protocol.packetlistening.PacketListener;
import es.eltrueno.protocol.packetlistening.ProtocolLibListener;
import es.eltrueno.protocol.packetlistening.TinyProtocolListener;
import es.eltrueno.protocol.protocollib.TruenoNPC_ProtocolLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.logging.Level;

public class TruenoNPCApi {

    private static final ArrayList<TruenoNPC> npcs = new ArrayList<TruenoNPC>();
    private static String version;
    private static Plugin plugin;
    private static Boolean cache = true;

    private static PacketListener packetListener = null;

    public static Plugin getPlugin() {
        return plugin;
    }

    public static Boolean getCache() {
        return cache;
    }

    public static void useCache(boolean bol) {
        cache = bol;
    }

    private static void setupVersion() {
        try {
            version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public static void removeCacheFile() {
        TruenoNPC_ProtocolLib.removeCacheFile();
    }

    public static ArrayList<TruenoNPC> getNPCs() {
        ArrayList<TruenoNPC> list = new ArrayList<TruenoNPC>();
        for (TruenoNPC npc : npcs) {
            if (!npc.isDeleted()) {
                list.add(npc);
            }
        }
        return list;
    }

    /**
     * Create a NPC
     *
     * @param plugin
     * @param location NPC Location
     * @param skin     NPC skin using a playername
     */
    public static TruenoNPC createNPC(Plugin plugin, Location location, TruenoNPCSkin skin) {
        TruenoNPCApi.plugin = plugin;
        if (version == null) {
            setupVersion();
        }
        if (packetListener == null) {
            if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
                packetListener = new ProtocolLibListener();
                Bukkit.getLogger().log(Level.INFO, ChatColor.YELLOW + "ProtocolLib found. NPCs listening and using it");
            } else {
                packetListener = new TinyProtocolListener();
            }
            packetListener.startListening(plugin);
        }
        if (packetListener instanceof ProtocolLibListener) {
            TruenoNPC_ProtocolLib.startTask(plugin);
            TruenoNPC npc = new TruenoNPC_ProtocolLib(location, skin);
            npcs.add(npc);
            return npc;
        }
        Bukkit.getLogger().log(Level.SEVERE, ChatColor.RED + "Unsopported server version.");
        return null;
    }

}
