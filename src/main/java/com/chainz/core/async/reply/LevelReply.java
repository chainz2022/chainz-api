package com.chainz.core.async.reply;

import com.chainz.core.async.request.RequestType;

public class LevelReply extends Reply {
    protected String uuid;
    protected Integer level;
    protected Integer percent;

    public LevelReply(String uuid, Integer level, Integer percent) {
        this.uuid = uuid;
        this.level = level;
        this.percent = percent;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.PLAYERLEVEL;
    }

    public String getUUID() {
        return this.uuid;
    }

    public Integer getLevel() {
        return this.level;
    }

    public Integer getPercent() {
        return this.percent;
    }
}
