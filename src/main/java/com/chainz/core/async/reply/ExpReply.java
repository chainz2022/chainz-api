package com.chainz.core.async.reply;

import com.chainz.core.async.request.RequestType;

public class ExpReply extends Reply {
    protected String uuid;
    protected Integer exp;
    protected Integer exptolevel;

    public ExpReply(String uuid, Integer exp, Integer exptolevel) {
        this.uuid = uuid;
        this.exp = exp;
        this.exptolevel = exptolevel;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.PLAYERLEVEL;
    }

    public String getUUID() {
        return this.uuid;
    }

    public Integer getExp() {
        return this.exp;
    }

    public Integer getExpTolevel() {
        return this.exptolevel;
    }
}
