package com.chainz.core.async.request;

public class PlayerMultiplierRequest extends Request {
    private String uuid;

    public PlayerMultiplierRequest(String uuid) {
        this.uuid = uuid;
    }

    public String getUUID() {
        return this.uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.PLAYERMULTIPLIER;
    }
}
