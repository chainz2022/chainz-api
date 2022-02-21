package com.chainz.core.async.adrewards;

import com.chainz.core.Core;
import com.chainz.core.async.reply.AdRewardsReply;
import com.chainz.core.async.reply.CallbackReply;
import com.chainz.core.utils.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

public class JAdReward implements AdReward {
    @Override
    public void createLink(Player p, Boolean rank, CallbackReply callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try (Jedis j = Core.pool.getResource()) {
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                j.select(5);
                String link = Strutils.getRandomString();
                j.set("adreward:uuid:" + p.getUniqueId(), link);
                j.hset("adreward:link:" + link, "uuid", p.getUniqueId().toString());
                j.hset("adreward:link:" + link, "name", p.getName());
                j.hset("adreward:link:" + link, "rank", rank ? "true" : "false");
                j.expire("adreward:uuid:" + p.getUniqueId(), 100);
                j.expire("adreward:link:" + link, 100);
                AdRewardsReply reply = new AdRewardsReply(p.getUniqueId().toString(), link);
                callback.then(reply);
            } catch (Exception ex) {
                callback.error(ex);
            }
        });
    }

    @Override
    public void getLinkData(String link, CallbackReply callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try (Jedis j = Core.pool.getResource()) {
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                j.select(5);
                if (j.exists("adreward:link:" + link) || j.get("adreward:link:" + link) != null) {
                    String uuid = j.hget("adreward:link:" + link, "uuid");
                    String name = j.hget("adreward:link:" + link, "name");
                    Boolean rank = Boolean.valueOf(j.hget("adreward:link:" + link, "rank"));
                    AdRewardsReply reply = new AdRewardsReply(link, uuid, name, rank);
                    callback.then(reply);
                } else {
                    callback.error(new Exception("Link not found"));
                }
            } catch (Exception ex) {
                callback.error(ex);
            }
        });
    }

    @Override
    public void getPlayerLink(String uuid, CallbackReply callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try (Jedis j = Core.pool.getResource()) {
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                j.select(5);
                if (j.exists("adreward:uuid:" + uuid)) {
                    String link = j.get("adreward:uuid:" + uuid);
                    AdRewardsReply reply = new AdRewardsReply(uuid, link);
                    callback.then(reply);
                } else {
                    callback.then(null);
                }
            } catch (Exception ex) {
                callback.error(ex);
            }
        });
    }

    @Override
    public void removeLink(String link) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try (Jedis j = Core.pool.getResource()) {
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                j.select(5);
                if (j.exists("adreward:link:" + link)) {
                    String uuid = j.hget("adreward:link:" + link, "uuid");
                    j.del("adreward:uuid:" + uuid);
                    j.del("adreward:link:" + link);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
