package com.chainz.core.playerprofile;

import java.text.DecimalFormat;
import java.util.UUID;

public abstract class PlayerProfile {
    private final UUID uuid;
    private final String name;
    private double coins;
    private double multiplier;
    private Integer level;
    private final double exp;
    private final String skinvalue;
    private final String skinsignature;

    public PlayerProfile(UUID uuid, String name, Double coins, Double multiplier, Integer level, double exp, String skinvalue, String skinsignature) {
        this.uuid = uuid;
        this.name = name;
        this.coins = coins;
        this.multiplier = multiplier;
        this.level = level;
        this.exp = exp;
        this.skinvalue = skinvalue;
        this.skinsignature = skinsignature;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public Integer getLevel() {
        return this.level;
    }

    public Double getExp() {
        return this.exp;
    }

    public Double getCoins() {
        return this.coins;
    }

    public double getMultiplier() {
        return this.multiplier;
    }

    public void addCoins(double amount) {
        this.coins += amount;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
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

    public boolean differs(PlayerProfile profile) {
        return (this.getCoins() != profile.getCoins() || this.getMultiplier() != profile.getMultiplier() ||
                this.getExp() != profile.getExp() || this.getLevel() != profile.getLevel());
    }
}
