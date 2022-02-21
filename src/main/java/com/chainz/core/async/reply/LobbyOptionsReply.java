package com.chainz.core.async.reply;

import com.chainz.core.async.request.RequestType;

public class LobbyOptionsReply extends Reply {
    protected String uuid;
    protected boolean visibility;
    protected boolean speed;
    protected boolean fly;

    public LobbyOptionsReply(String uuid, boolean visibility, boolean speed, boolean fly) {
        this.uuid = uuid;
        this.visibility = visibility;
        this.fly = fly;
        this.speed = speed;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.LOBBYOPTIONS;
    }

    public String getUUID() {
        return this.uuid;
    }

    public boolean getVisibility() {
        return this.visibility;
    }

    public boolean getSpeed() {
        return this.speed;
    }

    public boolean getFly() {
        return this.fly;
    }
}
