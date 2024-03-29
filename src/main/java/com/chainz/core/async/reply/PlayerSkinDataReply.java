package com.chainz.core.async.reply;

import com.chainz.core.async.request.RequestType;

import java.util.UUID;

public class PlayerSkinDataReply extends Reply {
    private final UUID uuid;
    private final String value;
    private final String signature;
    private String name;

    public PlayerSkinDataReply(UUID uuid, String value, String signature) {
        this.uuid = uuid;
        this.value = value;
        this.signature = signature;
    }

    public PlayerSkinDataReply(UUID uuid, String name, String value, String signature) {
        this.uuid = uuid;
        this.value = value;
        this.name = name;
        this.signature = signature;
    }

    public PlayerSkinDataReply(String uuid, String name, String value, String signature) {
        this.uuid = UUID.fromString(uuid);
        this.value = value;
        this.name = name;
        this.signature = signature;
    }

    public UUID getUUID() {
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
