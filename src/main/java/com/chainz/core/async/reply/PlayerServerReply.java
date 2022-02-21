package com.chainz.core.async.reply;

import com.chainz.core.async.request.RequestType;

public class PlayerServerReply extends Reply {
    protected String uuid;
    protected String server;
    protected boolean online;

    public PlayerServerReply(String uuid, String server, boolean online) {
        this.uuid = uuid;
        this.server = server;
        this.online = online;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getServer() {
        return this.server;
    }

    public boolean isOnline() {
        return this.online;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.PLAYERSERVER;
    }
}
