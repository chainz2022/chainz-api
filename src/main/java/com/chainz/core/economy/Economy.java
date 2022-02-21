package com.chainz.core.economy;

import com.chainz.core.async.reply.CallbackReply;

public interface Economy {
    Double getCoins(String p0);

    void getCoinsAsync(String p0, CallbackReply p1);

    Double getPlayerMultiplier(String p0);

    Boolean playerExists(String p0);

    void addCoins(String p0, Double p1, boolean p2);

    void addCoinsAsync(String p0, Double p1, boolean p2, CallbackReply p3);

    void removeCoins(String p0, Double p1);

    void removeCoinsAsync(String p0, Double p1, CallbackReply p2);

    void setPlayerMultiplier(String p0, Double p1);

    void setPlayerMultiplierAsync(String p0, Double p1, CallbackReply p2);
}
