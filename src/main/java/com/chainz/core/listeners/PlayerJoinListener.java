package com.chainz.core.listeners;

import com.chainz.core.ChainZAPI;
import com.chainz.core.Core;
import com.chainz.core.async.reply.CallbackReply;
import com.chainz.core.async.reply.LobbyOptionsReply;
import com.chainz.core.async.reply.PlayerSkinDataReply;
import com.chainz.core.async.reply.Reply;
import com.chainz.core.lobbyoptions.LobbyOptionsManager;
import com.chainz.core.playerlevel.PlayerLevelManager;
import com.chainz.core.playersettings.PlayerSettingsManager;
import com.chainz.core.playerskindata.GameProfileChanger;
import com.chainz.core.sql.SQLManager;
import com.chainz.core.utils.Nametag;
import com.chainz.core.utils.Tablist;
import com.chainz.core.utils.config.ConfigManager;
import com.chainz.core.vanish.VanishManager;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static com.chainz.core.Core.col;

public class PlayerJoinListener implements Listener {
    private static final ArrayList<Player> nickchanges = new ArrayList<Player>();

    private boolean hasJoinBypass(Player p) {
        return p.hasPermission("chainz.joinbypass");
    }

    @EventHandler
    public void joinBypass(PlayerLoginEvent ev) {
        if (ConfigManager.get("config.yml").getBoolean("Enable_joinfull") && ev.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            Player p = ev.getPlayer();
            if (this.hasJoinBypass(p)) {
                ev.setResult(PlayerLoginEvent.Result.ALLOWED);
            } else {
                ev.setResult(PlayerLoginEvent.Result.KICK_FULL);
                ev.setKickMessage(col("&cServer full!\n&eBypass will be available for NFT holders soon."));
            }
        }
    }

    private void setDefaultEconomyTable(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            String uuid = p.getUniqueId().toString();
            int defaultcoins = ConfigManager.get("economy.yml").getInt("economy.default");
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM economy WHERE uuid = '" + uuid + "';");
                res.next();
                if (res.getRow() == 0) {
                    statement.executeUpdate("INSERT INTO economy (`uuid`, `coins`, `multiplier`) VALUES ('" + uuid + "', '" + defaultcoins + "', '" + 1 + "');");
                }
                try {
                    res.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                try {
                    statement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } catch (SQLException ex2) {
                ex2.printStackTrace();
            }
        });
    }

    private boolean canFly(Player p) {
        return p.hasPermission("chainz.fly");
    }

    private boolean hasBypass(Player p) {
        return p.hasPermission("chainz.fly");
    }

    @EventHandler
    public void login(PlayerLoginEvent ev) {
        Player p = ev.getPlayer();

        ChainZAPI.getPlayerSkinDataManager().getGameProfileChange(p, new CallbackReply() {
            @Override
            public void error(Exception ex) {
            }

            @Override
            public void then(Reply reply) {
                if (reply != null) {
                    PlayerSkinDataReply skinDataReply = (PlayerSkinDataReply) reply;
                    GameProfile profile = new GameProfile(p.getUniqueId(), skinDataReply.getName());
                    profile.getProperties().put("textures", new Property("textures", skinDataReply.getValue(), skinDataReply.getSignature()));
                    GameProfileChanger.changeGameProfileInstantly(p, profile);
                }
            }
        });
    }

    @EventHandler
    public void applyOptions(PlayerLoginEvent ev) {
        Player p = ev.getPlayer();
        LobbyOptionsManager.setDefaults(p);
        PlayerLevelManager.setDefaults(p);
        this.setDefaultEconomyTable(p);
        PlayerSettingsManager.setDefaults(p);
        if (Bukkit.getPluginManager().getPlugin("ChainZLobby") != null) {
            ChainZAPI.getLobbyOptions().getLobbyOptionsAsync(p.getUniqueId().toString(), new CallbackReply() {
                @Override
                public void error(Exception ex) {
                    p.sendMessage(col("&cAn unknown error has ocurred, please report this to a server admin"));
                    ex.printStackTrace();
                }

                @Override
                public void then(Reply reply) {
                    LobbyOptionsReply options = (LobbyOptionsReply) reply;
                    Bukkit.getScheduler().runTask(Core.core, () -> {
                        if (options.getSpeed()) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, true, false));
                        } else {
                            for (PotionEffect effect : p.getActivePotionEffects()) {
                                p.removePotionEffect(PotionEffectType.SPEED);
                            }
                        }
                        if (!options.getVisibility()) {
                            for (Player plys : Bukkit.getOnlinePlayers()) {
                                if (!hasBypass(plys)) {
                                    Bukkit.getScheduler().runTask(Core.core, () -> p.hidePlayer(plys));
                                }
                            }
                        }
                        p.setGameMode(p.getGameMode());
                        if (options.getFly()) {
                            if (canFly(p)) {
                                p.setAllowFlight(true);
                                p.setFlying(true);
                            } else {
                                p.setAllowFlight(false);
                                p.setFlying(false);
                                ChainZAPI.getLobbyOptions().setFly(p.getUniqueId() + "", false);
                            }
                        }
                    });
                }
            });
        }
        for (Player plys : Bukkit.getOnlinePlayers()) {
            if (VanishManager.isVanished(plys) && !p.hasPermission("chainz.staff")) {
                p.hidePlayer(plys);
            }
        }
    }

    @EventHandler
    public void removePrefix(PlayerQuitEvent ev) {
        ev.setQuitMessage(null);
        if (ConfigManager.get("config.yml").getBoolean("Enable_tags")) {
            Player p = ev.getPlayer();
            for (Player pl : Bukkit.getOnlinePlayers()) {
                Nametag.deleteTeam(pl, p.getName());
                Nametag.deleteTeam(p, pl.getName());
            }
        }
    }

    @EventHandler
    public void setPrefixAndTablist(PlayerJoinEvent ev) {
        ev.setJoinMessage(null);
        Player p = ev.getPlayer();

        ChainZAPI.getDisplayFormatter().update(p);

        if (ConfigManager.get("config.yml").getBoolean("Enable_tags")) {
            Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
                String prefix = ChainZAPI.getDisplayFormatter().prefix(p);
                Nametag ntag = new Nametag(p.getName(), p.getDisplayName(), prefix + "&e");
                ntag.addPlayer(p);
                ntag.updateAll();
            });
        }

        if (ConfigManager.get("config.yml").getBoolean("Enable_tablist")) {
            Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
                Tablist.sendTablist(p, "You're playing on ChainZ Network", "&dhttps://discord.gg/HqtbchaP");
            });
        }
    }

    @EventHandler
    public void teleportVanish(PlayerTeleportEvent ev) {
        if (ConfigManager.get("config.yml").getBoolean("Enable_vanish")) {
            if (VanishManager.isVanished(ev.getPlayer())) {
                for (Player plys : Bukkit.getOnlinePlayers()) {
                    if (plys.hasPermission("chainz.staff"))
                        plys.hidePlayer(ev.getPlayer());
                }
            }
        }

    }
}