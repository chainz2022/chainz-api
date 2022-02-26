package com.chainz.core.async.reply;

import com.chainz.core.async.request.RequestType;

import java.util.UUID;

public class LevelReply extends Reply {
    protected UUID uuid;
    protected Integer level;
    protected Integer percent;

    public LevelReply(UUID uuid, Integer level, Integer percent) {
        this.uuid = uuid;
        this.level = level;
        this.percent = percent;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.PLAYERLEVEL;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public Integer getLevel() {
        return this.level;
    }

    public Integer getPercent() {
        return this.percent;
    }
}
