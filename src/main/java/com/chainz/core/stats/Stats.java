package com.chainz.core.stats;

import com.chainz.core.async.reply.CallbackReply;
import com.chainz.core.stats.top.StatsTopType;

public interface Stats {
    void getStats(String p0, CallbackReply p1);

    void getTop(StatsTopType p0, String p1, int p2, CallbackReply p3);
}
