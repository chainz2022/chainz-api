package com.chainz.core.async.reply;

import com.chainz.core.async.request.RequestType;

import java.util.UUID;

public class PlayerServerReply extends Reply {
    protected UUID uuid;
    protected String server;
    protected boolean online;

    public PlayerServerReply(UUID uuid, String server, boolean online) {
        this.uuid = uuid;
        this.server = server;
        this.online = online;
    }

    public UUID getUUID() {
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
