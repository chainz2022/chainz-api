package com.chainz.core.economy;

import com.chainz.core.playerprofile.PlayerProfileManager;

import java.util.UUID;

public class EconomyManager implements Economy {
    private final PlayerProfileManager profileManager;
    private final EconomyDatabase db;

    public EconomyManager(EconomyDatabase db, PlayerProfileManager profileManager) {
        this.profileManager = profileManager;
        this.db = db;
    }

    public EconomyDatabase getDatabase() {
        return this.db;
    }

    @Override
    public boolean addCoins(UUID uuid, Double amount, boolean multiply) {
        if (this.profileManager.addCoins(uuid, amount, multiply)) {
            return true;
        } else {
            return db.addCoins(uuid, amount, multiply);
        }
    }

    @Override
    public boolean setPlayerMultiplier(UUID uuid, Double multiplier) {
        if (this.profileManager.setPlayerMultiplier(uuid, multiplier)) {
            return true;
        } else {
            return db.setPlayerMultiplier(uuid, multiplier);
        }
    }

    @Override
    public boolean removeCoins(UUID uuid, Double remove) {
        if (this.profileManager.removeCoins(uuid, remove)) {
            return true;
        } else {
            return db.removeCoins(uuid, remove);
        }
    }

    @Override
    public Double getCoins(UUID uuid) {
        return this.profileManager.getCoins(uuid);
    }

    @Override
    public Double getPlayerMultiplier(UUID uuid) {
        return this.profileManager.getPlayerMultiplier(uuid);
    }

    @Override
    public boolean playerExists(UUID uuid) {
        return this.profileManager.playerExists(uuid);
    }
}
