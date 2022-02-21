package es.eltrueno.npc.skin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import static com.chainz.core.utils.JsonUtils.getJsonResponse;

public class SkinManager {
    public static void getUUIDFromName(final Plugin plugin, String name, Callback<String> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            long unixTime = System.currentTimeMillis() / 1000L;
            JsonObject jsonresponse = getJsonResponse("https://api.mojang.com/users/profiles/minecraft/" + name + "?at=" + unixTime).getAsJsonObject();
            if (jsonresponse != null && jsonresponse.get("error") == null) {
                callback.call(jsonresponse.get("id").getAsString());
            } else {
                callback.call(null);
            }
        });
    }

    public static void getSkinFromMojangAsync(final Plugin plugin, final String identifier, final SkinDataReply skinreply) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (identifier.length() > 16) {
                JsonElement jsonresponse = getJsonResponse("https://sessionserver.mojang.com/session/minecraft/profile/" + identifier + "?unsigned=false");
                if (jsonresponse != null) {
                    JsonObject response = jsonresponse.getAsJsonObject();
                    if (!response.has("error")) {
                        JsonObject prop = response.getAsJsonArray("properties").get(0).getAsJsonObject();
                        String value = prop.get("value").getAsString();
                        String signature = prop.get("signature").getAsString();
                        skinreply.done(new SkinData(value, signature));
                    } else {
                        skinreply.done(null);
                    }
                } else {
                    skinreply.done(null);
                }
            } else {
                getUUIDFromName(plugin, identifier, uuid -> {
                    JsonElement jsonresponse = getJsonResponse("https://sessionserver.mojang.com/session/minecraft/profile/" + identifier + "?unsigned=false");
                    if (jsonresponse != null) {
                        JsonObject response = jsonresponse.getAsJsonObject();
                        if (!response.has("error")) {
                            JsonObject prop = response.getAsJsonArray("properties").get(0).getAsJsonObject();
                            String value = prop.get("value").getAsString();
                            String signature = prop.get("signature").getAsString();
                            skinreply.done(new SkinData(value, signature));
                        } else {
                            skinreply.done(null);
                        }
                    } else {
                        skinreply.done(null);
                    }
                });
            }
        });
    }

    public static void getSkinFromMineskinAsync(final Plugin plugin, final String uuid, final SkinDataReply skinreply) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            JsonObject jsonresponse = getJsonResponse("https://api.mineskin.org/get/uuid/" + uuid).getAsJsonObject();
            if (jsonresponse != null && !jsonresponse.has("error")) {
                JsonObject textureProperty = jsonresponse.getAsJsonObject("data").getAsJsonObject("texture");
                String value = textureProperty.get("value").getAsString();
                String signature = textureProperty.get("signature").getAsString();
                skinreply.done(new SkinData(value, signature));
            } else {
                skinreply.done(null);
            }
        });
    }


}
