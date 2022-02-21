package com.chainz.core.async.adrewards;

import com.chainz.core.ChainZAPI;
import com.chainz.core.async.adrewards.events.AdRewardClaimEvent;
import com.chainz.core.async.reply.AdRewardsReply;
import com.chainz.core.async.reply.CallbackReply;
import com.chainz.core.async.reply.Reply;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.JedisPubSub;

import java.util.UUID;

public class RedisListener extends JedisPubSub {
    @Override
    public void onMessage(String channel, String message) {
        if (channel.equalsIgnoreCase("adreward")) {
            System.out.println(message);
            if (message.startsWith("claimedreward:")) {
                String link = message.split(":")[1];
                ChainZAPI.getAdRewards().getLinkData(link, new CallbackReply() {
                    @Override
                    public void error(Exception ex) {
                    }

                    @Override
                    public void then(Reply reply) {
                        AdRewardsReply adRewardsReply = (AdRewardsReply) reply;
                        String uuid = adRewardsReply.getUUID();
                        Player p = Bukkit.getPlayer(UUID.fromString(uuid));
                        if (p != null) {
                            AdRewardClaimEvent event = new AdRewardClaimEvent(p);
                            Bukkit.getPluginManager().callEvent(event);
                            ChainZAPI.getAdRewards().removeLink(link);
                        }
                    }
                });
            }
        } else if (channel.equalsIgnoreCase("sendtoserver")) {
            System.out.println("sendtoserver");
            String playeruuid = message.split(":")[0];
            String server = message.split(":")[1];
            if (Bukkit.getPlayer(UUID.fromString(playeruuid)) != null) {
                System.out.println("jugador conectado");
                Player p = Bukkit.getPlayer(UUID.fromString(playeruuid));
                ChainZAPI.sendToServer(p, server);
            }
        }
    }
}
