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
import com.chainz.core.utils.Tablist;
import com.chainz.core.utils.config.ConfigManager;
import com.chainz.core.vanish.VanishManager;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.nametagedit.plugin.NametagEdit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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

    private boolean canFly(Player p) {
        return p.hasPermission("chainz.fly");
    }

    private boolean hasBypass(Player p) {
        return p.hasPermission("chainz.fly");
    }

    @EventHandler
    public void login(PlayerLoginEvent ev) {
        Player p = ev.getPlayer();

        PlayerSkinDataReply skinDataReply = ChainZAPI.getPlayerSkinDataManager().getGameProfileChange(p);
        if (skinDataReply != null) {
            GameProfile profile = new GameProfile(p.getUniqueId(), skinDataReply.getName());
            profile.getProperties().put("textures", new Property("textures", skinDataReply.getValue(), skinDataReply.getSignature()));
            GameProfileChanger.changeGameProfileInstantly(p, profile);
        }
    }

    @EventHandler
    public void applyOptions(PlayerLoginEvent ev) {
        Player p = ev.getPlayer();
        LobbyOptionsManager.setDefaults(p);
        PlayerLevelManager.setDefaults(p);
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
    public void setPrefixAndTablist(PlayerJoinEvent ev) {
        ev.setJoinMessage(null);
        Player p = ev.getPlayer();

        ChainZAPI.getDisplayFormatter().update(p);

        if (ConfigManager.get("config.yml").getBoolean("Enable_tags")) {
            Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
                String prefix = ChainZAPI.getDisplayFormatter().prefix(p);
                NametagEdit.getApi().setPrefix(p, prefix);
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
