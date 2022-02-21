package com.chainz.core.async.reply;

import com.chainz.core.async.request.RequestType;

public class PlayerSettingsReply extends Reply {
    protected String uuid;
    protected boolean friendrequests;
    protected boolean partyrequests;
    protected boolean msg;
    protected boolean chat;
    protected boolean ads;

    public PlayerSettingsReply(String uuid, boolean friendrequests, boolean partyrequests, boolean msg, boolean chat, boolean ads) {
        this.friendrequests = friendrequests;
        this.partyrequests = partyrequests;
        this.msg = msg;
        this.chat = chat;
        this.ads = ads;
    }

    public String getUuid() {
        return this.uuid;
    }

    public boolean getFriendRequests() {
        return this.friendrequests;
    }

    public boolean getPartyRequests() {
        return this.partyrequests;
    }

    public boolean getCanMsg() {
        return this.msg;
    }

    public boolean getChatVisibility() {
        return this.chat;
    }

    public boolean getAdVisibility() {
        return this.ads;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.PLAYERSETTINGS;
    }
}
