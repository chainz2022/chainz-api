package com.chainz.core.economy;

import java.util.UUID;

public interface Economy {
    Double getCoins(UUID p0);

    Double getPlayerMultiplier(UUID p0);

    boolean playerExists(UUID p0);

    boolean addCoins(UUID p0, Double p1, boolean p2);

    boolean removeCoins(UUID p0, Double p1);

    boolean setPlayerMultiplier(UUID p0, Double p1);
}
