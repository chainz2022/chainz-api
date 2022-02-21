package com.chainz.core.serverdata;

import com.chainz.core.async.reply.CallbackReply;

import java.util.ArrayList;
import java.util.Map;

public interface ServerData {
    void getServerInfoAsync(String p0, CallbackReply p1);

    void getServersPlayersAsync(ArrayList<String> p0, CallbackReply p1);

    void setServerStatus(Boolean p0);

    void setServerStatusSync(Boolean p0);

    void setServerPlayers(Integer p0);

    void setServerMaxPlayers(Integer p0);

    void setServerData(Map<String, String> p0);

    void setServerData(String p0, Map<String, String> p1);

    String getServerName();
}
