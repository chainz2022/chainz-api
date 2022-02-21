package com.chainz.core.async.reply;

import com.chainz.core.async.request.RequestType;

import java.util.ArrayList;

public class ServerReservedSlotsReply extends Reply {
    private String server;
    private ArrayList<String> reservedplayers;

    public ServerReservedSlotsReply(String server, ArrayList<String> reservedplayers) {
        this.server = server;
        this.reservedplayers = reservedplayers;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.SERVERRESERVEDSLOTS;
    }

    public String getServer() {
        return this.server;
    }

    public ArrayList<String> getReservedPlayers() {
        return this.reservedplayers;
    }
}
