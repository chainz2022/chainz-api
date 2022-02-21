package com.chainz.core.stats;

import com.google.gson.JsonObject;

public abstract class StatsObject {
    public abstract String getPlayerUUID();

    public abstract JsonObject getJson();
}
