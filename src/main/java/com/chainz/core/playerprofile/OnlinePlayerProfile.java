package com.chainz.core.playerprofile;

import java.util.UUID;

public class OnlinePlayerProfile extends PlayerProfile {
    private final String server;

    public OnlinePlayerProfile(UUID uuid, String name, Double coins, Double multiplier, Integer level, double exp, String skinvalue, String skinsignature, String server) {
        super(uuid, name, coins, multiplier, level, exp, skinvalue, skinsignature);
        this.server = server;
    }

    public String getServer() {
        return this.server;
    }
}
