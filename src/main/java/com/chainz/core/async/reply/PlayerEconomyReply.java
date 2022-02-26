package com.chainz.core.async.reply;

import com.chainz.core.async.request.RequestType;

import java.text.DecimalFormat;
import java.util.UUID;

public class PlayerEconomyReply extends Reply {
    protected UUID uuid;
    protected Double coins;
    protected Double multiplier;

    public PlayerEconomyReply(UUID uuid, Double coins) {
        this.uuid = uuid;
        this.coins = coins;
    }

    public PlayerEconomyReply(UUID uuid, Double coins, Double multiplier) {
        this.uuid = uuid;
        this.coins = coins;
        this.multiplier = multiplier;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.COINS;
    }

    public UUID getUUID() {
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
