package com.chainz.core.async.reply;

import com.chainz.core.async.request.RequestType;

import java.util.ArrayList;

public class PartyReply extends Reply {
    protected String playeruuid;
    protected String leaderuuid;
    protected ArrayList<String> partyplayers;

    public PartyReply(String playeruuid, String leaderuuid) {
        this.partyplayers = new ArrayList<String>();
        this.playeruuid = playeruuid;
        this.leaderuuid = leaderuuid;
    }

    public PartyReply(String leaderuuid, ArrayList<String> players) {
        this.partyplayers = new ArrayList<String>();
        this.leaderuuid = leaderuuid;
        this.partyplayers = players;
    }

    public String getPlayeruuid() {
        return this.playeruuid;
    }

    public String getLeaderuuid() {
        return this.leaderuuid;
    }

    public ArrayList<String> getPartyplayers() {
        return this.partyplayers;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.PARTY;
    }
}
