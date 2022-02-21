package com.chainz.core.async.request;

public class PlayerServerRequest extends Request {
    protected String uuid;

    public PlayerServerRequest(String uuid) {
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
        return RequestType.PLAYERSERVER;
    }
}
