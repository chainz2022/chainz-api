package com.chainz.core.playerprofile;

import com.chainz.core.ChainZAPI;
import com.chainz.core.Core;
import com.chainz.core.async.reply.*;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;

public class PlayerProfileManager implements PlayerProfileInterface {

    private final HashMap<UUID, PlayerProfileReply> profileCache = new HashMap<>();

    //TODO: join everything in the same sql
    @Override
    public void getPlayerProfileFromUuid(String uuid, CallbackReply callback) {
        if (profileCache.containsKey(UUID.fromString(uuid))) {
            callback.then(profileCache.get(UUID.fromString(uuid)));
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> ChainZAPI.getNameDataManager().getNameFromUuidAsync(uuid, new CallbackReply() {
                @Override
                public void then(Reply reply) {
                    NameReply namereply = (NameReply) reply;
                    ChainZAPI.getPlayerSkinDataManager().getPlayerSkinData(uuid, new CallbackReply() {
                        @Override
                        public void then(Reply reply) {
                            PlayerSkinDataReply playerSkinDataReply = (PlayerSkinDataReply) reply;
                            ChainZAPI.getEconomyManager().getCoinsAsync(uuid, new CallbackReply() {
                                @Override
                                public void then(Reply reply) {
                                    CoinsReply coinsReply = (CoinsReply) reply;
                                    ChainZAPI.getLevelSystem().getLevelAsync(uuid, new CallbackReply() {
                                        @Override
                                        public void then(Reply reply) {
                                            LevelReply levelreply = (LevelReply) reply;
                                            ChainZAPI.getPlayerServerManager().getPlayerServer(uuid, new CallbackReply() {
                                                @Override
                                                public void then(Reply reply) {
                                                    PlayerServerReply playersvreply = (PlayerServerReply) reply;
                                                    if (playersvreply.isOnline()) {
                                                        OnlinePlayerProfile onlineprofile = new OnlinePlayerProfile(uuid, namereply.getName(), coinsReply.getCoins(), coinsReply.getMultiplier(), levelreply.getLevel(), playerSkinDataReply.getValue(), playerSkinDataReply.getSignature(), playersvreply.getServer());
                                                        PlayerProfileReply profileReply = new PlayerProfileReply(uuid, onlineprofile);
                                                        callback.then(profileReply);
                                                    } else {
                                                        OnlinePlayerProfile onlineprofile = new OnlinePlayerProfile(uuid, namereply.getName(), coinsReply.getCoins(), coinsReply.getMultiplier(), levelreply.getLevel(), playerSkinDataReply.getValue(), playerSkinDataReply.getSignature(), playersvreply.getServer());
                                                        PlayerProfileReply profileReply = new PlayerProfileReply(uuid, onlineprofile);
                                                        callback.then(profileReply);
                                                    }
                                                }

                                                @Override
                                                public void error(Exception ex) {
                                                    ex.printStackTrace();
                                                    callback.error(ex);
                                                }
                                            });
                                        }

                                        @Override
                                        public void error(Exception ex) {
                                            ex.printStackTrace();
                                            callback.error(ex);
                                        }
                                    });
                                }

                                @Override
                                public void error(Exception ex) {
                                    ex.printStackTrace();
                                    callback.error(ex);
                                }
                            });
                        }

                        @Override
                        public void error(Exception ex) {
                            ex.printStackTrace();
                            callback.error(ex);
                        }
                    });
                }

                @Override
                public void error(Exception ex) {
                    ex.printStackTrace();
                    callback.error(ex);
                }
            }));
        }
    }
}