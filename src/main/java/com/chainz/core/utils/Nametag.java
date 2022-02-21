package com.chainz.core.utils;

import net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_12_R1.ScoreboardTeamBase;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class Nametag {
    private final PacketPlayOutScoreboardTeam packet;
    
    private static final ArrayList<Player> players = new ArrayList<Player>();

    public static void deleteTeam(String teamName) {
        for (Player pl : players) {
            try {
                PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
                Field f = packet.getClass().getDeclaredField("a");
                f.setAccessible(true);
                f.set(packet, teamName);
                f.setAccessible(false);
                Field f2 = packet.getClass().getDeclaredField("i");
                f2.setAccessible(true);
                f2.set(packet, 1);
                f2.setAccessible(false);
                ((CraftPlayer) pl).getHandle().playerConnection.sendPacket(packet);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void deleteTeam(Player p, String teamName) {
        try {
            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
            Field f = packet.getClass().getDeclaredField("a");
            f.setAccessible(true);
            f.set(packet, teamName);
            f.setAccessible(false);
            Field f2 = packet.getClass().getDeclaredField("i");
            f2.setAccessible(true);
            f2.set(packet, 1);
            f2.setAccessible(false);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteTeamPlayer(Player pl, String teamName) {
        try {
            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
            Field f = packet.getClass().getDeclaredField("a");
            f.setAccessible(true);
            f.set(packet, teamName);
            f.setAccessible(false);
            Field f2 = packet.getClass().getDeclaredField("i");
            f2.setAccessible(true);
            f2.set(packet, 1);
            f2.setAccessible(false);
            ((CraftPlayer) pl).getHandle().playerConnection.sendPacket(packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Nametag(String teamName, String displayName, String prefix, String suffix) {
        this.packet = new PacketPlayOutScoreboardTeam();
        setField("a", teamName);
        setField("b", displayName);
        setField("c", prefix);
        setField("d", suffix);
        setField("e", ScoreboardTeamBase.EnumTeamPush.ALWAYS.e);
        setField("g", 0);
        setField("i", 1);
    }

    public Nametag(String teamName, String displayName, String prefix) {
        this.packet = new PacketPlayOutScoreboardTeam();
        setField("b", displayName);
        setField("a", teamName);
        setField("c", prefix);
        setField("e", ScoreboardTeamBase.EnumTeamPush.ALWAYS.e);
        setField("g", 0);
        setField("i", 1);
    }

    public void addPlayer(Player pl) {
        try {
            add(pl);
            players.add(pl);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendToPlayer(Player pl) {
        try {
            ((CraftPlayer) pl).getHandle().playerConnection.sendPacket(this.packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateAll() {
        for (Player pl : players) {
            sendToPlayer(pl);
        }
    }

    private void setField(String field, Object value) {
        try {
            Field f = this.packet.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(this.packet, value);
            f.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void add(Player pl) throws NoSuchFieldException, IllegalAccessException {
        Field f = this.packet.getClass().getDeclaredField("h");
        f.setAccessible(true);
        ((Collection) f.get(this.packet)).add(pl.getName());
    }
}
