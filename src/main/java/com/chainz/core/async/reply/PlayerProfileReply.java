package com.chainz.core.async.reply;

import com.chainz.core.async.request.RequestType;
import com.chainz.core.playerprofile.OnlinePlayerProfile;
import com.chainz.core.playerprofile.PlayerProfile;

public class PlayerProfileReply extends Reply {
    protected String uuid;
    protected PlayerProfile playerprofile;

    public PlayerProfileReply(String uuid, PlayerProfile profile) {
        this.uuid = uuid;
        this.playerprofile = profile;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.PLAYERPROFILE;
    }

    public String getUUID() {
        return this.uuid;
    }

    public PlayerProfile getPlayerprofile() {
        return this.playerprofile;
    }

    public boolean isOnline() {
        return this.playerprofile instanceof OnlinePlayerProfile;
    }
}
