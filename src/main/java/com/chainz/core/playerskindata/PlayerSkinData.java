package com.chainz.core.playerskindata;

import com.chainz.core.async.reply.PlayerSkinDataReply;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface PlayerSkinData {
    PlayerSkinDataReply getGameProfileChange(Player p01);

    PlayerSkinDataReply getGameProfileChange(UUID p0);

    boolean changeNickname(Player p0, String p1);

    void cachePlayerSkin(UUID p0);

    PlayerSkinDataReply getPlayerSkinData(UUID p0);
}
