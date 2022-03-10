package com.chainz.core;

import com.chainz.core.economy.commands.EconomyCommands;
import com.chainz.core.economy.hooks.VaultEco;
import com.chainz.core.listeners.ChatListener;
import com.chainz.core.listeners.CommandBlock;
import com.chainz.core.listeners.PlayerJoinListener;
import com.chainz.core.playerlevel.commands.PlayerLevelCommands;
import com.chainz.core.playerprofile.listeners.PlayerProfileListener;
import com.chainz.core.playerskindata.commands.PlayerSkinCommands;
import com.chainz.core.sql.SQLManager;
import com.chainz.core.utils.config.ConfigManager;
import com.chainz.core.vanish.commands.VanishCommands;
import es.eltrueno.hologram.TruenoHologramAPI;
import es.eltrueno.protocol.packetlistening.ProtocolLibListener;
import es.eltrueno.protocol.packetlistening.TinyProtocolListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

public class Core extends JavaPlugin {

    public static Core core;
    public static JedisPool pool = null;

    public static boolean USE_VAULT = false;

    public Core() {
        core = this;
    }

    public static String col(String s) {
        if (s == null) {
            return "";
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> col(List<String> ls) {
        if (ls == null)
            return null;
        return ls.stream().map(Core::col).toList();
    }

    @Override
    public void onEnable() {
        core = this;

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getServer().getConsoleSender().sendMessage("Loading configs...");
        ConfigManager.save("config.yml");
        ConfigManager.load("config.yml");
        ConfigManager.save("messages.yml");
        ConfigManager.load("messages.yml");
        ConfigManager.save("economy.yml");
        ConfigManager.load("economy.yml");

        SQLManager.initialize();

        Bukkit.getServer().getConsoleSender().sendMessage("Connecting with redis...");
        try {
            pool = new JedisPool(ConfigManager.get("config.yml").getString("Redis.ip"), ConfigManager.get("config.yml").getInt("Redis.port"));
            Jedis j = pool.getResource();
            if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty())
                j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
            j.select(1);
            j.ping();
            Bukkit.getServer().getConsoleSender().sendMessage("Connected to Redis");
        } catch (Exception ex) {
            Bukkit.getServer().getConsoleSender().sendMessage("There was an error while connecting to Redis!");
            ex.printStackTrace();
        }

        loadCommands();

        USE_VAULT = ConfigManager.get("economy.yml").getBoolean("Vault");

        if (USE_VAULT) {
            if (VaultEco.isActive()) {
                VaultEco.register();
                Bukkit.getServer().getConsoleSender().sendMessage("ChainZ[API] Linked with Vault");
            } else {
                Bukkit.getServer().getConsoleSender().sendMessage("ChainZ[API] Vault not found!");
            }
        }
    }

    public void loadCommands() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new CommandBlock(), this);

        getCommand("addcoins").setExecutor(new EconomyCommands());
        getCommand("removecoins").setExecutor(new EconomyCommands());
        getCommand("setplayermultiplier").setExecutor(new EconomyCommands());
        getCommand("vanish").setExecutor(new VanishCommands());
        getCommand("addxp").setExecutor(new PlayerLevelCommands());
        getCommand("setlevel").setExecutor(new PlayerLevelCommands());
        getCommand("nick").setExecutor(new PlayerSkinCommands());

        Bukkit.getPluginManager().registerEvents(new PlayerProfileListener(), this);

        if (Bukkit.getPluginManager().getPlugin("ChainZLobby") != null) {
            if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
                ProtocolLibListener hologramProtocolLibListener = new ProtocolLibListener();
                hologramProtocolLibListener.startListening(this);
            } else {
                TinyProtocolListener hologramTinyProtocolListener = new TinyProtocolListener();
                hologramTinyProtocolListener.startListening(this);
            }
        }
    }


    @Override
    public void onDisable() {
        TruenoHologramAPI.destroyAllHolos();
        ChainZAPI.getPlayerProfileManager().saveAll();
        ChainZAPI.getServerData().setServerStatusSync(Boolean.FALSE);
        SQLManager.closeConnection();
        try {
            pool.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
