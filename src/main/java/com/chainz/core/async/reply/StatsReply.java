package com.chainz.core.async.reply;

import com.chainz.core.async.request.RequestType;
import com.chainz.core.stats.SkywarsStatsObject;
import com.chainz.core.stats.TheBridgeStatsObject;
import com.chainz.core.stats.top.StatsTopObject;

public class StatsReply extends Reply {
    private SkywarsStatsObject skywars;
    private TheBridgeStatsObject thebridge;
    private String jsonresponse;
    private StatsTopObject top;

    public StatsReply(SkywarsStatsObject skywars, TheBridgeStatsObject thebridge, String jsonresponse) {
        this.skywars = skywars;
        this.thebridge = thebridge;
        this.jsonresponse = jsonresponse;
    }

    public StatsReply(StatsTopObject top) {
        this.top = top;
    }

    public StatsTopObject getTopObject() {
        return this.top;
    }

    public SkywarsStatsObject getSkywarsStats() {
        return this.skywars;
    }

    public TheBridgeStatsObject getTheBridgeStats() {
        return this.thebridge;
    }

    public String getJsonResponse() {
        return this.jsonresponse;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.STATS;
    }
}
