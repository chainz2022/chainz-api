package com.chainz.core.async.reply;

import com.chainz.core.async.request.RequestType;

import java.text.DecimalFormat;

public class CoinsReply extends Reply {
    protected String uuid;
    protected Double coins;
    protected Double multiplier;

    public CoinsReply(String uuid, Double coins) {
        this.uuid = uuid;
        this.coins = coins;
    }

    public CoinsReply(String uuid, Double coins, Double multiplier) {
        this.uuid = uuid;
        this.coins = coins;
        this.multiplier = multiplier;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.COINS;
    }

    public String getUUID() {
        return this.uuid;
    }

    public Double getCoins() {
        return this.coins;
    }

    public Double getMultiplier() {
        return this.multiplier;
    }

    public String getCoinsFormatted() {
        if (this.getCoins() != 0) {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            decimalFormat.setGroupingUsed(true);
            decimalFormat.setGroupingSize(3);
            return decimalFormat.format(this.coins);
        }
        return "0";
    }
}
