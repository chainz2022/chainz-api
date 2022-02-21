package com.chainz.core.stats;

import com.chainz.core.utils.time.TimeUtils;
import com.google.gson.JsonObject;

public class SkywarsStatsObject extends StatsObject {
    private String uuid;
    private final int gamesplayed;
    private final int gameswinned;
    private final int kills;
    private final int deaths;
    private final int secondsplayed;
    private final String timeplayedformated;
    private JsonObject skywarsjson;

    public SkywarsStatsObject(JsonObject skywarsjson) {
        if (skywarsjson != null) {
            this.skywarsjson = skywarsjson;
            this.gamesplayed = this.skywarsjson.get("gamesPlayed").getAsInt();
            this.gameswinned = this.skywarsjson.get("gamesWon").getAsInt();
            this.kills = this.skywarsjson.get("kills").getAsInt();
            this.deaths = this.skywarsjson.get("deaths").getAsInt();
            this.secondsplayed = 0;
            this.timeplayedformated = TimeUtils.formatSeconds(this.secondsplayed);
        } else {
            this.gamesplayed = 0;
            this.gameswinned = 0;
            this.kills = 0;
            this.deaths = 0;
            this.secondsplayed = 0;
            this.timeplayedformated = TimeUtils.formatSeconds(this.secondsplayed);
        }
    }

    @Override
    public String getPlayerUUID() {
        return this.uuid;
    }

    @Override
    public JsonObject getJson() {
        return this.skywarsjson;
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

    public int getSecondsPlayed() {
        return this.secondsplayed;
    }

    public String getTimePlayedFormated() {
        return this.timeplayedformated;
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
        return null;
    }
}
