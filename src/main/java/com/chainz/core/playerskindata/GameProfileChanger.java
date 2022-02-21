package com.chainz.core.playerskindata;

import com.chainz.core.Core;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import com.mojang.authlib.GameProfile;
import es.eltrueno.protocol.protocollib.wrapper.WrapperPlayServerPlayerInfo;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

public class GameProfileChanger {
    private static void setValue(final Object obj, final String name, final Object value) {
        try {
            final Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception ex) {
        }
    }

    private static Object getValue(final Object obj, final String name) {
        try {
            final Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception ex) {
            return null;
        }
    }

    private static void sendPacket(final Packet<?> packet, final Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    private static void rmvFromTablist(final Player p, final GameProfile profile2) {
        final PacketContainer packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        WrapperPlayServerPlayerInfo packet_editor = new WrapperPlayServerPlayerInfo(packet);

        WrappedGameProfile profile = WrappedGameProfile.fromHandle(profile2);
        PlayerInfoData newData = new PlayerInfoData(profile, 1, EnumWrappers.NativeGameMode.NOT_SET, WrappedChatComponent.fromText(profile2.getName()));

        List<PlayerInfoData> players = packet.getPlayerInfoDataLists().read(0);
        players.add(newData);

        packet_editor.setAction(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        packet_editor.setData(players);

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(p, packet, false);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void addToTablist(final Player p, final GameProfile profile2) {
        final PacketContainer packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        WrapperPlayServerPlayerInfo packet_editor = new WrapperPlayServerPlayerInfo(packet);

        WrappedGameProfile profile = WrappedGameProfile.fromHandle(profile2);

        PlayerInfoData newData = new PlayerInfoData(profile, 1, EnumWrappers.NativeGameMode.NOT_SET, WrappedChatComponent.fromText(profile2.getName()));

        List<PlayerInfoData> players = packet.getPlayerInfoDataLists().read(0);
        players.add(newData);

        packet_editor.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        packet_editor.setData(players);

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(p, packet, false);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void destroyEntity(final Player p, final int entityid) {
        final PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityid);
        sendPacket(packet, p);
    }

    private static void spawnEntity(final Player p, final int entityid, final UUID uuid, final Location location) {
        final PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
        setValue(packet, "a", entityid);
        setValue(packet, "b", uuid);
        setValue(packet, "c", MathHelper.floor(location.getX() * 32.0));
        setValue(packet, "d", MathHelper.floor(location.getY() * 32.0));
        setValue(packet, "e", MathHelper.floor(location.getZ() * 32.0));
        setValue(packet, "f", (byte) (location.getYaw() * 256.0f / 360.0f));
        setValue(packet, "g", (byte) (location.getPitch() * 256.0f / 360.0f));
        final WrappedDataWatcher w = new WrappedDataWatcher();
        w.setObject(10, 127);
        setValue(packet, "i", w);
        sendPacket(packet, p);
    }

    private static void respawn(final Player player, final World world, final GameMode mode, final Location loc) {
        EnumGamemode nmsmode = EnumGamemode.NOT_SET;
        if (mode == GameMode.SURVIVAL) {
            nmsmode = EnumGamemode.SURVIVAL;
        } else if (mode == GameMode.ADVENTURE) {
            nmsmode = EnumGamemode.ADVENTURE;
        } else if (mode == GameMode.CREATIVE) {
            nmsmode = EnumGamemode.CREATIVE;
        } else if (mode == GameMode.SPECTATOR) {
            nmsmode = EnumGamemode.SPECTATOR;
        }
        final PacketPlayOutRespawn packet = new PacketPlayOutRespawn(0, world.getDifficulty(), world.worldData.getType(), nmsmode);
        sendPacket(packet, player);
        player.getLocation().setPitch(loc.getPitch());
        player.getLocation().setYaw(loc.getYaw());
        player.teleport(loc);
    }

    public static void changeGameProfile(final Player p, final GameProfile profile) {
        Bukkit.getScheduler().runTask(Core.core, () -> {
            try {
                final Method getHandle = p.getClass().getMethod("getHandle", (Class<?>[]) null);
                final Object entityPlayer = getHandle.invoke(p);
                final Class<?> entityHuman = entityPlayer.getClass().getSuperclass();
                final Field bH = entityHuman.getDeclaredField("bH");
                bH.setAccessible(true);
                bH.set(entityPlayer, profile);
                final WorldServer nmsworld = ((CraftWorld) p.getLocation().getWorld()).getHandle();
                for (final Player players : Bukkit.getOnlinePlayers()) {
                    Bukkit.getScheduler().runTask(Core.core, () -> {
                        destroyEntity(players, p.getEntityId());
                        rmvFromTablist(players, ((CraftPlayer) p).getProfile());
                        addToTablist(players, ((CraftPlayer) p).getProfile());
                        players.hidePlayer(p);
                        players.showPlayer(p);
                    });
                }
            } catch (Exception ignored) {
            }
        });
    }

    public static void changeGameProfileInstantly(final Player p, final GameProfile profile) {
        Bukkit.getScheduler().runTask(Core.core, () -> {
            try {
                final Method getHandle = p.getClass().getMethod("getHandle", (Class<?>[]) null);
                final Object entityPlayer = getHandle.invoke(p);
                final Class<?> entityHuman = entityPlayer.getClass().getSuperclass();
                final Field bH = entityHuman.getDeclaredField("bH");
                bH.setAccessible(true);
                bH.set(entityPlayer, profile);
                final WorldServer nmsworld = ((CraftWorld) p.getLocation().getWorld()).getHandle();
                for (final Player players : Bukkit.getOnlinePlayers()) {
                    Bukkit.getScheduler().runTask(Core.core, () -> {
                        destroyEntity(players, p.getEntityId());
                        rmvFromTablist(players, ((CraftPlayer) p).getProfile());
                        addToTablist(players, ((CraftPlayer) p).getProfile());
                        respawn(p, nmsworld, p.getGameMode(), p.getLocation().clone());
                        players.hidePlayer(p);
                        players.showPlayer(p);
                        p.updateInventory();
                    });
                }
            } catch (Exception ignored) {
            }
        });
    }
}
