package com.chainz.core.playerskindata;

import com.chainz.core.async.reply.CallbackReply;
import com.chainz.core.async.reply.PlayerSkinDataReply;
import org.bukkit.entity.Player;

public interface PlayerSkinData {
    void getGameProfileChange(final Player p0, final CallbackReply p1);

    void getGameProfileChange(final String p0, final CallbackReply p1);

    PlayerSkinDataReply getGameProfileChangeSync(final String p0);

    void changeNickname(final Player p0, final String p1, final CallbackReply p2);

    void cachePlayerSkin(final String p0);

    void getPlayerSkinData(final String p0, final CallbackReply p1);
}
