package com.chainz.core.async.reply;

import com.chainz.core.async.request.RequestType;

public class AdRewardsReply extends Reply {
    private String uuid;
    private String link;
    private String name;
    private boolean rank;

    public AdRewardsReply(String uuid, String link) {
        this.uuid = uuid;
        this.link = link;
    }

    public AdRewardsReply(String link, String uuid, String name, Boolean rank) {
        this.uuid = uuid;
        this.link = link;
        this.name = name;
        this.rank = rank;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.ADREWARDS;
    }

    public String getUUID() {
        return this.uuid;
    }

    public String getLink() {
        return this.link;
    }

    public String getName() {
        return this.name;
    }

    public boolean haveRank() {
        return this.rank;
    }
}
