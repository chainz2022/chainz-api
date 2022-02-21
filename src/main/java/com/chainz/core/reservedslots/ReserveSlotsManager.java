package com.chainz.core.reservedslots;

import com.chainz.core.ChainZAPI;
import com.chainz.core.Core;
import com.chainz.core.async.reply.CallbackReply;
import com.chainz.core.async.reply.Reply;
import com.chainz.core.async.reply.ServerInfoReply;
import com.chainz.core.async.reply.ServerReservedSlotsReply;
import com.chainz.core.utils.config.ConfigManager;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReserveSlotsManager implements ReserveSlots {
    @Override
    public void reserveSlot(String server, String uuid, CallbackReply callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try (Jedis j = Core.pool.getResource()) {
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                j.select(3);
                j.rpush("reservedslots:" + server, uuid);
                System.out.println("Server " + server + " reserved space for " + uuid);
                ChainZAPI.getServerData().getServerInfoAsync(server, new CallbackReply() {
                    @Override
                    public void error(Exception ex) {
                        ex.printStackTrace();
                        callback.then(null);
                    }

                    @Override
                    public void then(Reply reply) {
                        HashMap<String, String> data = new HashMap<String, String>();
                        ChainZAPI.getServerData().setServerData(new HashMap<>());
                        ServerInfoReply svinfo = (ServerInfoReply) reply;
                        if (svinfo.getData("reservedslots") != null) {
                            int rsvslots = Integer.parseInt(svinfo.getData("reservedslots"));
                            int finalrsvslots = rsvslots + 1;
                            data.put("reservedslots", "" + finalrsvslots);
                        } else {
                            data.put("reservedslots", "1");
                            System.out.println("Reserved 1 slot in " + server);
                        }
                        ChainZAPI.getServerData().setServerData(server, data);
                        callback.then(null);
                    }
                });
            } catch (Exception ex) {
                callback.error(ex);
            }
        });
    }

    @Override
    public void reserveSlots(String server, ArrayList<String> players, CallbackReply callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try (Jedis j = Core.pool.getResource()) {
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                j.select(3);

                players.forEach(uuid -> j.rpush("reservedslots:" + server, uuid));

                ChainZAPI.getServerData().getServerInfoAsync(server, new CallbackReply() {
                    @Override
                    public void error(Exception ex) {
                        ex.printStackTrace();
                        callback.then(null);
                    }

                    @Override
                    public void then(Reply reply) {
                        HashMap<String, String> data = new HashMap<String, String>();
                        ChainZAPI.getServerData().setServerData(new HashMap<String, String>());
                        ServerInfoReply svinfo = (ServerInfoReply) reply;
                        if (svinfo.getData("reservedslots") != null) {
                            data.put("reservedslots", "" + Integer.valueOf(svinfo.getData("reservedslots")) + players.size());
                        } else {
                            data.put("reservedslots", "" + players.size());
                        }
                        ChainZAPI.getServerData().setServerData(server, data);
                        callback.then(null);
                    }
                });
            } catch (Exception ex) {
                callback.error(ex);
            }
        });
    }

    @Override
    public void getReservedSlots(String server, CallbackReply callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, new Runnable() {
            @Override
            public void run() {
                try (Jedis j = Core.pool.getResource()) {
                    if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                        j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                    }
                    j.select(3);
                    if (j.exists("reservedslots:" + server)) {
                        List<String> players = j.lrange("reservedslots:" + server, 0L, -1L);
                        if (players == null) {
                            players = new ArrayList<String>();
                        }
                        ServerReservedSlotsReply reply = new ServerReservedSlotsReply(server, (ArrayList) players);
                        callback.then(reply);
                    } else {
                        ServerReservedSlotsReply reply2 = new ServerReservedSlotsReply(server, null);
                        callback.then(reply2);
                    }
                } catch (Exception ex) {
                    callback.error(ex);
                }
            }
        });
    }

    @Override
    public ServerReservedSlotsReply getReservedSlotsSync(String server) {
        try (Jedis j = Core.pool.getResource()) {
            if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
            }
            j.select(3);
            if (j.exists("reservedslots:" + server)) {
                List<String> players = j.lrange("reservedslots:" + server, 0L, -1L);
                if (players == null) {
                    players = new ArrayList<String>();
                }
                ServerReservedSlotsReply reply = new ServerReservedSlotsReply(server, (ArrayList) players);
                return reply;
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void removePlayerReservedSlot(String server, String uuid, CallbackReply callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try (Jedis j = Core.pool.getResource()) {
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                j.select(3);
                j.lrem("reservedslots:" + server, 0L, uuid);
                ChainZAPI.getServerData().getServerInfoAsync(server, new CallbackReply() {
                    @Override
                    public void error(Exception ex) {
                        ex.printStackTrace();
                        callback.then(null);
                    }

                    @Override
                    public void then(Reply reply) {
                        HashMap<String, String> data = new HashMap<String, String>();
                        ChainZAPI.getServerData().setServerData(new HashMap<String, String>());
                        ServerInfoReply svinfo = (ServerInfoReply) reply;
                        if (svinfo.getData("reservedslots") != null) {
                            int rsvslots = Integer.valueOf(svinfo.getData("reservedslots"));
                            if (rsvslots != 0) {
                                int finalrsvslots = rsvslots - 1;
                                data.put("reservedslots", "" + finalrsvslots);
                                ChainZAPI.getServerData().setServerData(server, data);
                            }
                            callback.then(null);
                        }
                    }
                });
            } catch (Exception ex) {
                callback.error(ex);
            }
        });
    }

    @Override
    public void removePlayersReservedSlot(String server, ArrayList<String> players, CallbackReply callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try (Jedis j = Core.pool.getResource()) {
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                j.select(3);
                for (String uuid : players) {
                    j.lrem("reservedslots:" + server, 0L, uuid);
                }
                ChainZAPI.getServerData().getServerInfoAsync(server, new CallbackReply() {
                    @Override
                    public void error(Exception ex) {
                        ex.printStackTrace();
                        callback.then(null);
                    }

                    @Override
                    public void then(Reply reply) {
                        HashMap<String, String> data = new HashMap<String, String>();
                        ChainZAPI.getServerData().setServerData(new HashMap<String, String>());
                        ServerInfoReply svinfo = (ServerInfoReply) reply;
                        if (svinfo.getData("reservedslots") != null) {
                            int rsvslots = Integer.valueOf(svinfo.getData("reservedslots"));
                            if (rsvslots != 0) {
                                int finalrsvslots = rsvslots - players.size();
                                data.put("reservedslots", "" + finalrsvslots);
                                ChainZAPI.getServerData().setServerData(server, data);
                            }
                            callback.then(null);
                        }
                    }
                });
            } catch (Exception ex) {
                callback.error(ex);
            }
        });
    }
}
