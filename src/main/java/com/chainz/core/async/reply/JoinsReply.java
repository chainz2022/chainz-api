package com.chainz.core.async.reply;

import com.chainz.core.async.request.RequestType;

import java.sql.Timestamp;

public class JoinsReply extends Reply {
    protected String uuid;
    protected Timestamp firstjoin;
    protected Timestamp lastjoin;
    protected Long timeplayed;

    public JoinsReply(String uuid, Timestamp firstjoin, Timestamp lastjoin, Long timeplayed) {
        this.uuid = uuid;
        this.firstjoin = firstjoin;
        this.lastjoin = lastjoin;
        this.timeplayed = timeplayed;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.JOINS;
    }

    public String getUUID() {
        return this.uuid;
    }

    public Timestamp getFirstJoin() {
        return this.firstjoin;
    }

    public Timestamp getLastJoin() {
        return this.lastjoin;
    }

    public Long getTimePlayed() {
        return this.timeplayed;
    }
}
