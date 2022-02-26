package com.chainz.core.economy;

import java.util.UUID;

public class PlayerEconomy {
    private UUID uuid;
    private double coins;
    private double multiplier;

    public PlayerEconomy(UUID uuid, double coins, double multiplier) {
        this.coins = coins;
        this.multiplier = multiplier;
    }

    public UUID getUUID() {
        return uuid;
    }

    public double getCoins() {
        return coins;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void addCoins(double amount) {
        this.coins += amount;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }
}
