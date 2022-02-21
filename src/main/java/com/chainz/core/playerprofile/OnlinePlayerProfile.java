package com.chainz.core.playerprofile;

public class OnlinePlayerProfile extends PlayerProfile {
    private final String server;

    public OnlinePlayerProfile(String uuid, String name, Double coins, Double multiplier, Integer level, String skinvalue, String skinsignature, String server) {
        super(uuid, name, coins, multiplier, level, skinvalue, skinsignature);
        this.server = server;
    }

    public String getServer() {
        return this.server;
    }
}
