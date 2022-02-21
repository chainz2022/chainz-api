package com.chainz.core.lobbyoptions;

import com.chainz.core.async.reply.CallbackReply;

public interface LobbyOptions {
    void getLobbyOptionsAsync(String p0, CallbackReply p1);

    void setLobbyOptionsAsync(String p0, boolean p1, boolean p2, boolean p3);

    Boolean getVisibility(String p0) throws Exception;

    Boolean getSpeed(String p0) throws Exception;

    Boolean getFly(String p0) throws Exception;

    void setVisibility(String p0, Boolean p1);

    void setVisibilityAsync(String p0, Boolean p1, CallbackReply p2);

    void setSpeed(String p0, Boolean p1);

    void setSpeedAsync(String p0, Boolean p1, CallbackReply p2);

    void setFly(String p0, Boolean p1);

    void setFlyAsync(String p0, Boolean p1, CallbackReply p2);
}
