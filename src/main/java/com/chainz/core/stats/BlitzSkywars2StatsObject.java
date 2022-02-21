package com.chainz.core.stats;

import com.chainz.core.utils.time.TimeUtils;
import com.google.gson.JsonObject;

public class BlitzSkywars2StatsObject extends StatsObject {
    private String uuid;
    private final int games_played_solo;
    private final int games_played_team;
    private final int games_played_total;
    private final int games_winned_solo;
    private final int games_winned_team;
    private final int games_winned_total;
    private final int games_loosed_solo;
    private final int games_loosed_team;
    private final int games_loosed_total;
    private final int kills_solo;
    private final int kills_team;
    private final int kills_total;
    private final int deaths_solo;
    private final int deaths_team;
    private final int deaths_total;
    private final int assists_solo;
    private final int assists_team;
    private final int assists_total;
    private final int seconds_played_solo;
    private final int seconds_played_team;
    private final int seconds_played_total;
    private final String timeplayedformated_solo;
    private final String timeplayedformated_team;
    private final String timeplayedformated_total;
    private final JsonObject skywarsjson;

    public BlitzSkywars2StatsObject(JsonObject skywarsjson) {
        this.skywarsjson = skywarsjson;
        JsonObject solo = this.skywarsjson.getAsJsonObject("solo");
        this.games_played_solo = solo.get("games_played").getAsInt();
        this.games_winned_solo = solo.get("games_winned").getAsInt();
        this.games_loosed_solo = solo.get("games_loosed").getAsInt();
        this.kills_solo = solo.get("kills").getAsInt();
        this.deaths_solo = solo.get("deaths").getAsInt();
        this.assists_solo = solo.get("assists").getAsInt();
        this.seconds_played_solo = solo.get("time_played").getAsInt();
        this.timeplayedformated_solo = TimeUtils.formatSeconds(this.seconds_played_solo);
        JsonObject team = this.skywarsjson.getAsJsonObject("team");
        this.games_played_team = team.get("games_played").getAsInt();
        this.games_winned_team = team.get("games_winned").getAsInt();
        this.games_loosed_team = team.get("games_loosed").getAsInt();
        this.kills_team = team.get("kills").getAsInt();
        this.deaths_team = team.get("deaths").getAsInt();
        this.assists_team = solo.get("assists").getAsInt();
        this.seconds_played_team = team.get("time_played").getAsInt();
        this.timeplayedformated_team = TimeUtils.formatSeconds(this.seconds_played_team);
        JsonObject total = this.skywarsjson.getAsJsonObject("total");
        this.games_played_total = total.get("games_played").getAsInt();
        this.games_winned_total = total.get("games_winned").getAsInt();
        this.games_loosed_total = total.get("games_loosed").getAsInt();
        this.kills_total = total.get("kills").getAsInt();
        this.deaths_total = total.get("deaths").getAsInt();
        this.assists_total = solo.get("assists").getAsInt();
        this.seconds_played_total = total.get("time_played").getAsInt();
        this.timeplayedformated_total = TimeUtils.formatSeconds(this.seconds_played_total);
    }

    @Override
    public String getPlayerUUID() {
        return this.uuid;
    }

    @Override
    public JsonObject getJson() {
        return this.skywarsjson;
    }

    public Double getSoloWinRatePercent() {
        if (this.games_winned_solo != 0 && this.games_played_solo != 0) {
            Double d = this.games_winned_solo * 100.0 / this.games_played_solo;
            Double rounded = Math.round(d * 100.0) / 100.0;
            return rounded;
        }
        return null;
    }

    public Double getTeamWinRatePercent() {
        if (this.games_winned_team != 0 && this.games_played_team != 0) {
            Double d = this.games_winned_team * 100.0 / this.games_played_team;
            Double rounded = Math.round(d * 100.0) / 100.0;
            return rounded;
        }
        return null;
    }

    public Double getTotalWinRatePercent() {
        if (this.games_winned_total != 0 && this.games_played_total != 0) {
            Double d = this.games_winned_total * 100.0 / this.games_played_total;
            Double rounded = Math.round(d * 100.0) / 100.0;
            return rounded;
        }
        return null;
    }

    public Double getSoloKDR() {
        if (this.kills_solo != 0 && this.deaths_solo != 0) {
            Double d = this.kills_solo / (double) this.deaths_solo;
            Double rounded = Math.round(d * 100.0) / 100.0;
            return rounded;
        }
        return null;
    }

    public Double getTeamKDR() {
        if (this.kills_team != 0 && this.deaths_team != 0) {
            Double d = this.kills_team / (double) this.deaths_team;
            Double rounded = Math.round(d * 100.0) / 100.0;
            return rounded;
        }
        return null;
    }

    public Double getTotalKDR() {
        if (this.kills_total != 0 && this.deaths_total != 0) {
            Double d = this.kills_total / (double) this.deaths_total;
            Double rounded = Math.round(d * 100.0) / 100.0;
            return rounded;
        }
        return null;
    }

    public Double getSoloKDAR() {
        if (this.deaths_solo != 0) {
            Double d = (this.assists_solo / 2.0 + this.kills_solo) / this.deaths_solo;
            Double rounded = Math.round(d * 100.0) / 100.0;
            return rounded;
        }
        return null;
    }

    public Double getTeamKDAR() {
        if (this.deaths_team != 0) {
            Double d = (this.assists_team / 2.0 + this.kills_team) / this.deaths_team;
            Double rounded = Math.round(d * 100.0) / 100.0;
            return rounded;
        }
        return null;
    }

    public Double getTotalKDAR() {
        if (this.deaths_total != 0) {
            Double d = (this.assists_total / 2.0 + this.kills_total) / this.deaths_total;
            Double rounded = Math.round(d * 100.0) / 100.0;
            return rounded;
        }
        return null;
    }

    public int getGamesPlayedSolo() {
        return this.games_played_solo;
    }

    public int getGamesPlayedTeam() {
        return this.games_played_team;
    }

    public int getGamesPlayedTotal() {
        return this.games_played_total;
    }

    public int getGamesWinnedSolo() {
        return this.games_winned_solo;
    }

    public int getGamesWinnedTeam() {
        return this.games_winned_team;
    }

    public int getGamesWinnedTotal() {
        return this.games_winned_total;
    }

    public int getGamesLoosedSolo() {
        return this.games_loosed_solo;
    }

    public int getGamesLoosedTeam() {
        return this.games_loosed_team;
    }

    public int getGamesLoosedTotal() {
        return this.games_loosed_total;
    }

    public int getKillsSolo() {
        return this.kills_solo;
    }

    public int getKillsTeam() {
        return this.kills_team;
    }

    public int getKillsTotal() {
        return this.kills_total;
    }

    public int getDeathsSolo() {
        return this.deaths_solo;
    }

    public int getDeathsTeam() {
        return this.deaths_team;
    }

    public int getDeathsTotal() {
        return this.deaths_total;
    }

    public int getAssistsSolo() {
        return this.assists_solo;
    }

    public int getAssistsTeam() {
        return this.assists_team;
    }

    public int getAssistsTotal() {
        return this.assists_total;
    }
}
