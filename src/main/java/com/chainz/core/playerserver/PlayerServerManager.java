package com.chainz.core.playerserver;

import com.chainz.core.Core;
import com.chainz.core.async.reply.CallbackReply;
import com.chainz.core.async.reply.PlayerServerReply;
import com.chainz.core.utils.config.ConfigManager;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;

public class PlayerServerManager implements PlayerServer {
    @Override
    public void getPlayerServer(String uuid, CallbackReply callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            Jedis j = null;
            try {
                j = Core.pool.getResource();
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                j.select(2);
                if (j.exists("player_server:" + uuid)) {
                    String server = j.get("player_server:" + uuid);
                    callback.then(new PlayerServerReply(uuid, server, true));
                } else {
                    callback.then(new PlayerServerReply(uuid, null, false));
                }
            } catch (Exception ex) {
                callback.error(ex);
            } finally {
                j.close();
            }
        });
    }
}
