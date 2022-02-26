package es.eltrueno.npc.skin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.chainz.core.utils.JsonUtils.getJsonResponse;

public class SkinManager {
    public static UUID getUUIDFromName(String name) {
        CompletableFuture<UUID> cf = CompletableFuture.supplyAsync(() -> {
            long unixTime = System.currentTimeMillis() / 1000L;
            JsonObject jsonresponse = getJsonResponse("https://api.mojang.com/users/profiles/minecraft/" + name + "?at=" + unixTime).getAsJsonObject();
            if (jsonresponse != null && jsonresponse.get("error") == null) {
                return UUID.fromString(jsonresponse.get("id").getAsString());
            } else {
                return null;
            }
        });
        try {
            return cf.get();
        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SkinData getSkinFromMojangAsync(String identifier) {
        CompletableFuture<SkinData> cf = CompletableFuture.supplyAsync(() -> {
            if (identifier.length() > 16) {
                JsonElement jsonresponse = getJsonResponse("https://sessionserver.mojang.com/session/minecraft/profile/" + identifier + "?unsigned=false");
                if (jsonresponse != null) {
                    JsonObject response = jsonresponse.getAsJsonObject();
                    if (!response.has("error")) {
                        JsonObject prop = response.getAsJsonArray("properties").get(0).getAsJsonObject();
                        String value = prop.get("value").getAsString();
                        String signature = prop.get("signature").getAsString();
                        return new SkinData(value, signature);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                UUID uuid = getUUIDFromName(identifier);
                JsonElement jsonresponse = getJsonResponse("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString() + "?unsigned=false");
                if (jsonresponse != null) {
                    JsonObject response = jsonresponse.getAsJsonObject();
                    if (!response.has("error")) {
                        JsonObject prop = response.getAsJsonArray("properties").get(0).getAsJsonObject();
                        String value = prop.get("value").getAsString();
                        String signature = prop.get("signature").getAsString();
                        return new SkinData(value, signature);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        });
        try {
            return cf.get();
        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
