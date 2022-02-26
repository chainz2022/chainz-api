package com.chainz.core.async.reply;

import com.chainz.core.async.request.RequestType;

import java.util.UUID;

public class NameReply extends Reply {
    protected UUID uuid;
    protected String name;

    public NameReply(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.NAMEDATA;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }
}
