package com.chainz.core.async.reply;

import com.chainz.core.async.request.RequestType;

import java.util.ArrayList;

public class FriendsReply extends Reply {
    protected String uuid1;
    protected String uuid2;
    protected boolean areFriends;
    protected ArrayList<String> friends;

    public FriendsReply(String uuid1, String uuid2, boolean areFriends) {
        this.friends = new ArrayList<String>();
        this.uuid1 = uuid1;
        this.uuid2 = uuid2;
        this.areFriends = areFriends;
    }

    public FriendsReply(String uuid, ArrayList<String> friends) {
        this.friends = new ArrayList<String>();
        this.uuid1 = uuid;
        this.friends = friends;
    }

    public String getUuid1() {
        return this.uuid1;
    }

    public String getUuid2() {
        return this.uuid2;
    }

    public boolean areFriends() {
        return this.areFriends;
    }

    public ArrayList<String> getFriends() {
        return this.friends;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.FRIENDS;
    }
}
