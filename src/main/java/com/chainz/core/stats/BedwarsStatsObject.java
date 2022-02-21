package com.chainz.core.stats;

import com.google.gson.JsonObject;

public class BedwarsStatsObject extends StatsObject {
    private String uuid;
    private String firstplay;
    private String lastplay;
    private int gamesplayed;
    private int gameswinned;
    private int gamesloosed;
    private int kills;
    private int finalkills;
    private int deaths;
    private int finaldeaths;
    private int bedsdestroyed;
    private String firstplayformated;
    private String lastplayformated;
    private JsonObject bedwarsjson;

    public BedwarsStatsObject(JsonObject bedwarsjson) {
        this.bedwarsjson = bedwarsjson;
        this.gamesplayed = this.bedwarsjson.get("games_played").getAsInt();
        this.gameswinned = this.bedwarsjson.get("games_winned").getAsInt();
        this.gamesloosed = this.bedwarsjson.get("games_loosed").getAsInt();
        this.kills = this.bedwarsjson.get("kills").getAsInt();
        this.deaths = this.bedwarsjson.get("deaths").getAsInt();
        this.finalkills = this.bedwarsjson.get("final_kills").getAsInt();
        this.finaldeaths = this.bedwarsjson.get("final_deaths").getAsInt();
        this.bedsdestroyed = this.bedwarsjson.get("beds_destroyed").getAsInt();
    }

    @Override
    public String getPlayerUUID() {
        return this.uuid;
    }

    @Override
    public JsonObject getJson() {
        return this.bedwarsjson;
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

    public int getGamesLoosed() {
        return this.gamesloosed;
    }

    public int getFinalKills() {
        return this.finalkills;
    }

    public int getFinalDeaths() {
        return this.finaldeaths;
    }

    public int getBedsDestroyed() {
        return this.bedsdestroyed;
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
