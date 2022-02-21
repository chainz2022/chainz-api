package com.chainz.core.stats;

import com.google.gson.JsonObject;

public class TheBridgeStatsObject extends StatsObject {
    private String uuid;
    private final int gamesplayed;
    private final int gameswinned;
    private final int kills;
    private final int deaths;
    private final int scoredpoints;
    private JsonObject thebridgejson;

    public TheBridgeStatsObject(JsonObject thebridgejson) {
        if (thebridgejson != null) {
            this.thebridgejson = thebridgejson;
            this.gamesplayed = this.thebridgejson.get("gamesPlayed").getAsInt();
            this.gameswinned = this.thebridgejson.get("gamesWon").getAsInt();
            this.kills = this.thebridgejson.get("kills").getAsInt();
            this.deaths = this.thebridgejson.get("deaths").getAsInt();
            this.scoredpoints = this.thebridgejson.get("scoredPoints").getAsInt();
        } else {
            this.gamesplayed = 0;
            this.gameswinned = 0;
            this.kills = 0;
            this.deaths = 0;
            this.scoredpoints = 0;
        }
    }

    @Override
    public String getPlayerUUID() {
        return this.uuid;
    }

    @Override
    public JsonObject getJson() {
        return this.thebridgejson;
    }

    public int getGamesPlayed() {
        return this.gamesplayed;
    }

    public int getGamesWinned() {
        return this.gameswinned;
    }

    public int getKills() {
        return this.kills;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public int getScoredPoints() {
        return this.scoredpoints;
    }

    public Double getWinRatePercent() {
        if (this.gameswinned != 0 && this.gamesplayed != 0) {
            Double d = this.gameswinned * 100.0 / this.gamesplayed;
            Double rounded = Math.round(d * 100.0) / 100.0;
            return rounded;
        }
        return null;
    }

    public Double getKDR() {
        if (this.kills != 0 && this.deaths != 0) {
            Double d = this.kills / (double) this.deaths;
            Double rounded = Math.round(d * 100.0) / 100.0;
            return rounded;
        }
        return 0.0;
    }
}
