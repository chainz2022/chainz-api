package com.chainz.core.playerserver;

import com.chainz.core.Core;
import com.chainz.core.async.reply.PlayerServerReply;
import com.chainz.core.utils.config.ConfigManager;
import redis.clients.jedis.Jedis;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerServerManager implements PlayerServer {
    @Override
    public PlayerServerReply getPlayerServer(UUID uuid) {
        CompletableFuture<PlayerServerReply> cf = CompletableFuture.supplyAsync(() -> {
            try (Jedis j = Core.pool.getResource()) {
                PlayerServerReply response;
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                j.select(2);
                if (j.exists("player_server:" + uuid.toString())) {
                    String server = j.get("player_server:" + uuid);
                    response = new PlayerServerReply(uuid, server, true);
                } else {
                    response = new PlayerServerReply(uuid, null, false);
                }
                return response;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        });
        try {
            return cf.get();
        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
