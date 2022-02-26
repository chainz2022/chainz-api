package com.chainz.core.async.reply;

import com.chainz.core.async.request.RequestType;
import com.chainz.core.playerprofile.OnlinePlayerProfile;
import com.chainz.core.playerprofile.PlayerProfile;

import java.util.UUID;

public class PlayerProfileReply extends Reply {
    protected UUID uuid;
    protected PlayerProfile playerprofile;

    public PlayerProfileReply(UUID uuid, PlayerProfile profile) {
        this.uuid = uuid;
        this.playerprofile = profile;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.PLAYERPROFILE;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public PlayerProfile getPlayerProfile() {
        return this.playerprofile;
    }

    public boolean isOnline() {
        return this.playerprofile instanceof OnlinePlayerProfile;
    }
}
