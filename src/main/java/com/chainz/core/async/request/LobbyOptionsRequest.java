package com.chainz.core.async.request;

public class LobbyOptionsRequest extends Request {
    private String uuid;

    public LobbyOptionsRequest(String uuid) {
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
        return RequestType.LOBBYOPTIONS;
    }
}
