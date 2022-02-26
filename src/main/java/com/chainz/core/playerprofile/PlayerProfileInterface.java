package com.chainz.core.playerprofile;

import java.util.UUID;

public interface PlayerProfileInterface {
    PlayerProfile getPlayerProfileFromUUID(UUID p0);

    void consistency(UUID uniqueId);
}
