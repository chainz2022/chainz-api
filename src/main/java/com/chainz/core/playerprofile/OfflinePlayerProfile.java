package com.chainz.core.playerprofile;

import com.chainz.core.utils.time.TimeUtils;

import java.sql.Timestamp;

public class OfflinePlayerProfile extends PlayerProfile {
    private final Timestamp lastjoin;

    public OfflinePlayerProfile(String uuid, String name, Double coins, Double multiplier, Integer level, String skinvalue, String skinsignature, Timestamp lastjoin) {
        super(uuid, name, coins, multiplier, level, skinvalue, skinsignature);
        this.lastjoin = lastjoin;
    }

    public Timestamp getLastJoin() {
        return this.lastjoin;
    }

    public String getLastJoinFormatted() {
        return TimeUtils.getPrettyTime(this.lastjoin);
    }
}
