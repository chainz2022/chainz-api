package com.chainz.core.playerprofile;

import java.text.DecimalFormat;

public abstract class PlayerProfile {
    private final String uuid;
    private final String name;
    private final Double coins;
    private final Double multiplier;
    private final Integer level;
    private final String skinvalue;
    private final String skinsignature;

    public PlayerProfile(String uuid, String name, Double coins, Double multiplier, Integer level, String skinvalue, String skinsignature) {
        this.uuid = uuid;
        this.name = name;
        this.coins = coins;
        this.multiplier = multiplier;
        this.level = level;
        this.skinvalue = skinvalue;
        this.skinsignature = skinsignature;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public Integer getLevel() {
        return this.level;
    }

    public Double getCoins() {
        return this.coins;
    }

    public Double getMultiplier() {
        return this.multiplier;
    }

    public String getSkinValue() {
        return this.skinvalue;
    }

    public String getSkinSignature() {
        return this.skinsignature;
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
