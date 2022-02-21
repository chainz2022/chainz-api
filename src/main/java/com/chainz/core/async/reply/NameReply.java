package com.chainz.core.async.reply;

import com.chainz.core.async.request.RequestType;

public class NameReply extends Reply {
    protected String uuid;
    protected String name;

    public NameReply(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.NAMEDATA;
    }

    public String getUUID() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }
}
