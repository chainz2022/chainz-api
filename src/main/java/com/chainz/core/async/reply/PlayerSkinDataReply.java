package com.chainz.core.async.reply;

import com.chainz.core.async.request.RequestType;

public class PlayerSkinDataReply extends Reply {
    private String uuid;
    private String value;
    private String signature;
    private String name;

    public PlayerSkinDataReply(String uuid, String value, String signature) {
        this.uuid = uuid;
        this.value = value;
        this.signature = signature;
    }

    public PlayerSkinDataReply(String uuid, String name, String value, String signature) {
        this.uuid = uuid;
        this.value = value;
        this.name = name;
        this.signature = signature;
    }

    public String getUUID() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public String getSignature() {
        return this.signature;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.SKINDATA;
    }
}
