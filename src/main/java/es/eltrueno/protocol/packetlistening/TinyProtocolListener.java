package es.eltrueno.protocol.packetlistening;

import es.eltrueno.hologram.TruenoHologram;
import es.eltrueno.hologram.TruenoHologramAPI;
import es.eltrueno.hologram.event.TruenoHologramClickEvent;
import es.eltrueno.npc.TruenoNPC;
import es.eltrueno.npc.TruenoNPCApi;
import es.eltrueno.npc.event.TruenoNPCInteractEvent;
import es.eltrueno.protocol.tinyprotocol.Reflection;
import es.eltrueno.protocol.tinyprotocol.TinyProtocol;
import io.netty.channel.Channel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class TinyProtocolListener implements PacketListener {

    private static TinyProtocol protocol = null;

    private static final Class<?> EntityInteractClass = Reflection.getClass("{nms}.PacketPlayInUseEntity");
    private static final Reflection.FieldAccessor<Integer> EntityID = Reflection.getField(EntityInteractClass, int.class, 0);
    private static final ArrayList<Player> playerswhointeract = new ArrayList<Player>();


    @Override
    public void startListening(Plugin plugin) {
        if (protocol == null) {
            protocol = new TinyProtocol(plugin) {
                @Override
                public Object onPacketInAsync(Player sender, Channel channel, Object packet) {
                    if (EntityInteractClass.isInstance(packet)) {
                        if (!playerswhointeract.contains(sender)) {
                            for (TruenoNPC npc : TruenoNPCApi.getNPCs()) {
                                if (npc.getEntityID(sender) == EntityID.get(packet)) {
                                    TruenoNPCInteractEvent event = new TruenoNPCInteractEvent(sender, npc);
                                    Bukkit.getPluginManager().callEvent(event);
                                    break;
                                }
                            }
                            for (TruenoHologram holo : TruenoHologramAPI.getHolograms()) {
                                if (holo.getEntitiesIds().contains(EntityID.get(packet))) {
                                    TruenoHologramClickEvent event = new TruenoHologramClickEvent(sender, holo);
                                    Bukkit.getPluginManager().callEvent(event);
                                    break;
                                }
                            }
                            playerswhointeract.add(sender);
                            Bukkit.getScheduler().runTaskLaterAsynchronously(TruenoNPCApi.getPlugin(), () -> playerswhointeract.remove(sender), 2);
                        }
                    }
                    return super.onPacketInAsync(sender, channel, packet);
                }
            };
        }
    }

}
