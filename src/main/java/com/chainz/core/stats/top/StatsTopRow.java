package com.chainz.core.stats.top;

public class StatsTopRow {
    private final String uuid;
    private final String name;
    private final Integer record;

    public StatsTopRow(String uuid, String name, Integer record) {
        this.uuid = uuid;
        this.name = name;
        this.record = record;
    }


    public String getName() {
        return this.name;
    }

    public String getUUID() {
        return this.uuid;
    }

    public Integer getRecord() {
        return this.record;
    }
}
