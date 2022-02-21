package com.chainz.core.stats;

import com.chainz.core.Core;
import com.chainz.core.async.reply.CallbackReply;
import com.chainz.core.async.reply.StatsReply;
import com.chainz.core.stats.top.StatsTopObject;
import com.chainz.core.stats.top.StatsTopRow;
import com.chainz.core.stats.top.StatsTopType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.UUID;

import static com.chainz.core.utils.JsonUtils.getJsonResponse;

public class StatsManager implements Stats {

    private static JsonObject getStatsJsonResponse(String uuid, String username) {
        JsonElement element = getJsonResponse("http://132.226.157.221:8080/player/" + uuid + "?username=" + username);
        if (element == null) {
            return null;
        } else {
            return element.getAsJsonObject();
        }
    }

    private static JsonArray getTopJsonResponse(StatsTopType toptype, String column, int top) {
        JsonObject element = (JsonObject) getJsonResponse("http://132.226.157.221:8080/" + toptype.toString().toLowerCase() + "/top/?limit=" + top + "&col=" + column);
        if (element == null) {
            return null;
        } else {
            return element.getAsJsonArray("rows");
        }
    }

    @Override
    public void getStats(String uuid, CallbackReply callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            JsonObject jsonresponse = getStatsJsonResponse(uuid, Bukkit.getPlayer(UUID.fromString(uuid)).getName());
            if (jsonresponse != null) {
                JsonObject skywarsjson = null;
                JsonObject thebridgejson = null;

                try {
                    skywarsjson = jsonresponse.getAsJsonObject("skywars");
                } catch (Exception ignored) {

                }

                try {
                    thebridgejson = jsonresponse.getAsJsonObject("thebridge");
                } catch (Exception ignored) {

                }

                SkywarsStatsObject skywars = new SkywarsStatsObject(skywarsjson);
                TheBridgeStatsObject thebridge = new TheBridgeStatsObject(thebridgejson);
                StatsReply reply = new StatsReply(skywars, thebridge, jsonresponse.toString());
                callback.then(reply);
            } else {
                SkywarsStatsObject skywars = new SkywarsStatsObject(null);
                TheBridgeStatsObject thebridge = new TheBridgeStatsObject(null);
                StatsReply reply = new StatsReply(skywars, thebridge, null);
                callback.then(reply);
            }
        });
    }

    @Override
    public void getTop(StatsTopType toptype, String column, int top, CallbackReply callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            JsonArray jsonresponse = getTopJsonResponse(toptype, column, top);
            if (jsonresponse != null) {
                ArrayList<StatsTopRow> list = new ArrayList<>();
                for (int i = 0; i < top && jsonresponse.size() > i; ++i) {
                    JsonObject e = jsonresponse.get(i).getAsJsonObject();
                    JsonObject player = e.get("player").getAsJsonObject();
                    StatsTopRow row = new StatsTopRow(player.get("uuid").getAsString(), player.get("username").getAsString(), e.get(column).getAsInt());
                    list.add(i, row);
                }
                StatsTopObject topObject = new StatsTopObject(list);
                StatsReply reply = new StatsReply(topObject);
                callback.then(reply);
            } else {
                callback.error(new Exception("Unknown Error"));
            }
        });
    }
}
