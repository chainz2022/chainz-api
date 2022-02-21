package com.chainz.core.async.request;

public class ServerInfoRequest extends Request {
    private String server;

    public ServerInfoRequest(String server) {
        this.server = server;
    }

    public String getServer() {
        return this.server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.SERVERINFO;
    }
}
