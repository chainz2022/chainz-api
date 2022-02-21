package com.chainz.core.async.adrewards;

import com.chainz.core.async.reply.CallbackReply;
import org.bukkit.entity.Player;

public interface AdReward {
    void createLink(Player p0, Boolean p1, CallbackReply p2);

    void getLinkData(String p0, CallbackReply p1);

    void getPlayerLink(String p0, CallbackReply p1);

    void removeLink(String p0);
}
