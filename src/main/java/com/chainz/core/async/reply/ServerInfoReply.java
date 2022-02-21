package com.chainz.core.async.reply;

import com.chainz.core.async.request.RequestType;

import java.util.Map;

public class ServerInfoReply extends Reply {
    private String server;
    private Boolean status;
    private Integer players;
    private Integer maxplayers;
    private Map<String, String> data;

    public ServerInfoReply(String server, Boolean status, int players, int maxplayers, Map<String, String> data) {
        this.server = server;
        this.status = status;
        this.players = players;
        this.maxplayers = maxplayers;
        this.data = data;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.SERVERINFO;
    }

    public String getServer() {
        return this.server;
    }

    public Integer getPlayers() {
        return this.players;
    }

    public Integer getMaxPlayers() {
        return this.maxplayers;
    }

    public Map<String, String> getData() {
        return this.data;
    }

    public String getData(String key) {
        if (this.data != null) {
            return this.data.get(key);
        }
        return null;
    }

    public Boolean getStatus() {
        return this.status;
    }
}
