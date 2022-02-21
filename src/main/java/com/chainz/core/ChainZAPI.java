package com.chainz.core;

import com.chainz.core.arena.ArenaData;
import com.chainz.core.arena.ArenaDataManager;
import com.chainz.core.async.ChainZAsync;
import com.chainz.core.async.adrewards.AdReward;
import com.chainz.core.async.adrewards.JAdReward;
import com.chainz.core.chat.displayformatter.DisplayFormatter;
import com.chainz.core.economy.Economy;
import com.chainz.core.economy.EconomyManager;
import com.chainz.core.joins.Joins;
import com.chainz.core.joins.JoinsManager;
import com.chainz.core.lobbyoptions.LobbyOptions;
import com.chainz.core.lobbyoptions.LobbyOptionsManager;
import com.chainz.core.namedata.NameData;
import com.chainz.core.namedata.NameDataManager;
import com.chainz.core.playerlevel.PlayerLevel;
import com.chainz.core.playerlevel.PlayerLevelManager;
import com.chainz.core.playerprofile.PlayerProfileInterface;
import com.chainz.core.playerprofile.PlayerProfileManager;
import com.chainz.core.playerserver.PlayerServer;
import com.chainz.core.playerserver.PlayerServerManager;
import com.chainz.core.playersettings.PlayerSettings;
import com.chainz.core.playersettings.PlayerSettingsManager;
import com.chainz.core.playerskindata.PlayerSkinData;
import com.chainz.core.playerskindata.PlayerSkinDataManager;
import com.chainz.core.reservedslots.ReserveSlots;
import com.chainz.core.reservedslots.ReserveSlotsManager;
import com.chainz.core.serverdata.ServerData;
import com.chainz.core.serverdata.ServerDataManager;
import com.chainz.core.stats.Stats;
import com.chainz.core.stats.StatsManager;
import com.chainz.core.utils.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ChainZAPI {
    private static final Economy eco = new EconomyManager();
    private static final LobbyOptions lo = new LobbyOptionsManager();
    private static final PlayerServer playerserver = new PlayerServerManager();
    private static final PlayerLevel level = new PlayerLevelManager();
    private static final PlayerSettings playersettings = new PlayerSettingsManager();
    private static final Stats stats = new StatsManager();
    private static final PlayerSkinData skindata = new PlayerSkinDataManager();
    private static final Joins joins = new JoinsManager();
    private static final NameData namedata = new NameDataManager();
    private static final PlayerProfileInterface playerprofile = new PlayerProfileManager();
    private static final ChainZAsync async = new ChainZAsync();
    private static final AdReward adreward = new JAdReward();
    private static final ArenaData arenaManager = new ArenaDataManager();
    private static final ServerData sd = new ServerDataManager();
    private static final ReserveSlots rs = new ReserveSlotsManager();
    private static final DisplayFormatter df = new DisplayFormatter();

    public static Economy getEconomyManager() {
        return ChainZAPI.eco;
    }

    public static ArenaData getArenaDataManager() {
        return ChainZAPI.arenaManager;
    }

    public static ServerData getServerData() {
        return ChainZAPI.sd;
    }

    public static ReserveSlots getReserveSlots() {
        return ChainZAPI.rs;
    }

    public static LobbyOptions getLobbyOptions() {
        return ChainZAPI.lo;
    }

    public static PlayerServer getPlayerServerManager() {
        return ChainZAPI.playerserver;
    }

    public static PlayerLevel getLevelSystem() {
        return ChainZAPI.level;
    }

    public static Stats getStatsManager() {
        return ChainZAPI.stats;
    }

    public static PlayerSkinData getPlayerSkinDataManager() {
        return skindata;
    }

    public static PlayerSettings getPlayerSettings() {
        return ChainZAPI.playersettings;
    }

    public static PlayerProfileInterface getPlayerProfileManager() {
        return ChainZAPI.playerprofile;
    }

    public static Joins getJoinsManager() {
        return ChainZAPI.joins;
    }

    public static AdReward getAdRewards() {
        return ChainZAPI.adreward;
    }

    public static NameData getNameDataManager() {
        return namedata;
    }

    public static FileConfiguration getConfig() {
        return ConfigManager.get("config.yml");
    }

    public static DisplayFormatter getDisplayFormatter() {
        return df;
    }

    public static ChainZAsync getAsync() {
        return ChainZAPI.async;
    }

    public static String getServerName() {
        return ConfigManager.get("config.yml").getString("bungee_server_name");
    }

    public static void executeBungeeCommand(Player p, String command) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("commands");
            out.writeUTF(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.sendPluginMessage(Core.core, "BungeeCord", b.toByteArray());
    }

    public static void bungeeSendToServer(Player p, String playeruuid, String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("bgsendtoserver");
            out.writeUTF(playeruuid + ":" + server);
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.sendPluginMessage(Core.core, "BungeeCord", b.toByteArray());
    }

    public static void sendToServer(Player player, String targetServer) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(out);
        try {
            dos.writeUTF("Connect");
            dos.writeUTF(targetServer);
        } catch (Exception ex) {
            Bukkit.getServer().getLogger().severe("Error Intentando enviar al jugador " + player.getName() + " al servidor " + targetServer);
            return;
        }
        player.sendPluginMessage(Core.core, "BungeeCord", out.toByteArray());
    }

    public static void sendToLobby(Player p) {
        sendToServer(p, "lobby");
    }
}
