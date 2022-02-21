package com.chainz.core.playersettings;

import com.chainz.core.async.reply.CallbackReply;

public interface PlayerSettings {
    void setCanFriendRequests(String p0, boolean p1, CallbackReply p2);

    void setChatVisibility(String p0, boolean p1, CallbackReply p2);

    void setAdVisibility(String p0, boolean p1, CallbackReply p2);

    void setCanPartyRequests(String p0, boolean p1, CallbackReply p2);

    void setCanMsg(String p0, boolean p1, CallbackReply p2);

    void setPersonalSettingsAsync(String p0, boolean p1, boolean p2, boolean p3, boolean p4, boolean p5, CallbackReply p6);

    void getPersonalSettingsAsync(String p0, CallbackReply p1);

    boolean getCanFriendRequests(String p0) throws Exception;

    boolean getCanPartyRequests(String p0) throws Exception;

    boolean getCanMsg(String p0) throws Exception;

    boolean getChatVisibility(String p0) throws Exception;

    boolean getAdVisibility(String p0) throws Exception;
}
