package com.chainz.core.arena;

import com.chainz.core.ChainZAPI;
import com.chainz.core.Core;
import com.chainz.core.utils.config.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ArenaDataManager implements ArenaData, Listener {
    private static final Map<Player, ArenaType> gameQueue = new HashMap<>();

    private final BukkitRunnable arenaTicker;

    public ArenaDataManager() {
        this.arenaTicker = new BukkitRunnable() {
            @Override
            public void run() {
                processQueue();
            }
        };
        arenaTicker.runTaskTimerAsynchronously(Core.core, 0L, 60L);
    }

    @Override
    public String getJoinableArena(ArenaType type, Player player) {
        CompletableFuture<List<String>> cf = CompletableFuture.supplyAsync(() -> {
            try (Jedis j = Core.pool.getResource()) {
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                return j.zrange(type.toString(), 0, 0);
            } catch (Exception e) {
                return null;
            }
        });
        try {
            return cf.get().get(0);
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public boolean sendToServerPubSub(Player p, CachedArena arena) {
        return this.sendToServerPubSub(p, arena.getServer(), arena.getMapName());
    }

    @Override
    public boolean sendToServerPubSub(Player p, String server, String mapName) {
        CompletableFuture<Boolean> cf = CompletableFuture.supplyAsync(() -> {
            Jedis j;
            try {
                j = Core.pool.getResource();
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty())
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                j.publish(server, p.getUniqueId() + ":" + mapName);

                j.close();
                j.disconnect();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
        try {
            return cf.get();
        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean joinRandomArena(ArenaType type, Player player) {
        String arenaData = ChainZAPI.getArenaDataManager().getJoinableArena(type, player);
        if (arenaData == null) {
            return false;
        } else {
            String[] data = arenaData.split(":");
            this.sendToServerPubSub(player, data[0], data[1]);
            ChainZAPI.sendToServer(player, data[0]);
            return true;
        }
    }

    @Override
    public boolean joinRandomArenaWithQueue(ArenaType type, Player player) {
        if (gameQueue.containsKey(player)) {
            if (gameQueue.get(player) == type) {
                player.sendMessage(Core.col("&aYou're already in the queue for " + gameQueue.get(player).getName() + ", you should be redirected any time now..."));
                return false;
            } else {
                gameQueue.remove(player);
                player.sendMessage(Core.col("&cLeaving " + gameQueue.get(player).getName() + " queue"));
            }
        }

        player.sendMessage(Core.col("&aLooking for the best arena..."));

        if (!joinRandomArena(type, player)) {
            player.sendMessage(Core.col("&cWe couldn't find a suitable arena, we've added you to a queue and redirect you as soon as possible!"));
            gameQueue.putIfAbsent(player, type);

            return false;
        } else {
            return true;
        }
    }

    @Override
    public void processQueue() {
        if (!gameQueue.isEmpty()) {
            for (Player player : gameQueue.keySet()) {
                ArenaType type = gameQueue.get(player);
                if (joinRandomArena(type, player)) {
                    gameQueue.remove(player);
                }
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        gameQueue.remove(event.getPlayer());
    }
}
