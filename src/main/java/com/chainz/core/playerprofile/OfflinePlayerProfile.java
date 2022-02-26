package com.chainz.core.playerprofile;

import com.chainz.core.utils.time.TimeUtils;

import java.sql.Timestamp;
import java.util.UUID;

public class OfflinePlayerProfile extends PlayerProfile {
    private final Timestamp lastjoin;

    public OfflinePlayerProfile(UUID uuid, String name, Double coins, Double multiplier, Integer level, double exp, String skinvalue, String skinsignature, Timestamp lastjoin) {
        super(uuid, name, coins, multiplier, level, exp, skinvalue, skinsignature);
        this.lastjoin = lastjoin;
    }

    public Timestamp getLastJoin() {
        return this.lastjoin;
    }

    public String getLastJoinFormatted() {
        return TimeUtils.getPrettyTime(this.lastjoin);
    }
}
