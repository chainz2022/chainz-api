package com.chainz.core.serverdata;

import com.chainz.core.Core;
import com.chainz.core.async.reply.CallbackReply;
import com.chainz.core.async.reply.ServerInfoReply;
import com.chainz.core.utils.config.ConfigManager;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ServerDataManager implements ServerData {
    @Override
    public void getServerInfoAsync(String server, CallbackReply reply) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try (Jedis j = Core.pool.getResource()) {
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                j.select(1);
                if (j.exists("server:" + server)) {
                    Map<String, String> svinfo = j.hgetAll("server:" + server);
                    Map<String, String> svinfodata = new HashMap<String, String>();
                    if (svinfo != null && j.hget("server:" + server, "updated") != null) {
                        for (String key : svinfo.keySet()) {
                            if (key.startsWith("data:")) {
                                svinfodata.put(key.substring(5), svinfo.get(key));
                            }
                        }
                        Date actualdate = new Date();
                        Date updatedate = new Date(Long.parseLong(svinfo.get("updated")));
                        ServerInfoReply svreply = new ServerInfoReply(server, svinfo.get("status").equals("on") && actualdate.getTime() - updatedate.getTime() <= 8000L, Integer.valueOf(svinfo.get("players")), Integer.valueOf(svinfo.get("maxplayers")), svinfodata);
                        reply.then(svreply);
                    } else {
                        reply.error(new Exception("bad format"));
                    }
                } else {
                    reply.error(new Exception("Server not found"));
                }
            } catch (Exception ex) {
                reply.error(ex);
            }
        });
    }

    @Override
    public void getServersPlayersAsync(ArrayList<String> servers, CallbackReply reply) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            int players = 0;
            try (Jedis j = Core.pool.getResource()) {
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                j.select(1);
                Date actualdate = new Date();
                for (String server : servers) {
                    if (j.exists("server:" + server) && j.hget("server:" + server, "updated") != null) {
                        Date updatedate = new Date(Long.parseLong(j.hget("server:" + server, "updated")));
                        if (!j.hget("server:" + server, "status").equals("on") || actualdate.getTime() - updatedate.getTime() > 8000L) {
                            continue;
                        }
                        players += Integer.parseInt(j.hget("server:" + server, "players"));
                    }
                }
                ServerInfoReply svreply = new ServerInfoReply(null, null, players, 0, null);
                reply.then(svreply);
            } catch (Exception ex) {
                reply.error(ex);
            }
        });
    }

    @Override
    public void setServerStatus(Boolean bol) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try (Jedis j = Core.pool.getResource()) {
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                j.select(1);
                Date date = new Date();
                String servername = ConfigManager.get("config.yml").getString("bungee_server_name");
                j.hset("server:" + servername, "status", bol ? "on" : "off");
                j.hset("server:" + servername, "updated", date.getTime() + "");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    @Override
    public void setServerStatusSync(Boolean bol) {
        try (Jedis j = Core.pool.getResource()) {
            if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
            }
            j.select(1);
            Date date = new Date();
            String servername = ConfigManager.get("config.yml").getString("bungee_server_name");
            j.hset("server:" + servername, "status", bol ? "on" : "off");
            j.hset("server:" + servername, "updated", date.getTime() + "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setServerPlayers(Integer playercount) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try (Jedis j = Core.pool.getResource()) {
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                j.select(1);
                Date date = new Date();
                String servername = ConfigManager.get("config.yml").getString("bungee_server_name");
                j.hset("server:" + servername, "players", playercount + "");
                j.hset("server:" + servername, "updated", date.getTime() + "");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    @Override
    public void setServerMaxPlayers(Integer maxplayercount) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try (Jedis j = Core.pool.getResource()) {
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                j.select(1);
                Date date = new Date();
                String servername = ConfigManager.get("config.yml").getString("bungee_server_name");
                j.hset("server:" + servername, "maxplayers", maxplayercount + "");
                j.hset("server:" + servername, "updated", date.getTime() + "");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    @Override
    public void setServerData(Map<String, String> data) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try (Jedis j = Core.pool.getResource()) {
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                j.select(1);
                Date date = new Date();
                String servername = ConfigManager.get("config.yml").getString("bungee_server_name");
                for (String key : data.keySet()) {
                    String value = data.get(key);
                    j.hset("server:" + servername, "data:" + key, value);
                }
                j.hset("server:" + servername, "updated", date.getTime() + "");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    @Override
    public void setServerData(String server, Map<String, String> data) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try (Jedis j = Core.pool.getResource()) {
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                j.select(1);
                Date date = new Date();
                for (String key : data.keySet()) {
                    String value = data.get(key);
                    j.hset("server:" + server, "data:" + key, value);
                }
                j.hset("server:" + server, "updated", date.getTime() + "");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    @Override
    public String getServerName() {
        return ConfigManager.get("config.yml").getString("bungee_server_name");
    }
}
