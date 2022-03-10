package com.chainz.core.playerprofile;

import com.chainz.core.ChainZAPI;
import com.chainz.core.Core;
import com.chainz.core.async.reply.*;
import com.chainz.core.economy.PlayerEconomy;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.chainz.core.utils.JsonUtils.getJsonResponse;

public class PlayerProfileDatabase {
    private static JsonObject reportToDatabase(UUID uuid, String username) {
        JsonElement element = getJsonResponse("http://132.226.157.221:8080/player/" + uuid + "?username=" + username);
        if (element == null) {
            return null;
        } else {
            return element.getAsJsonObject();
        }
    }

    public PlayerProfile getPlayerProfileFromUUID(UUID uuid) {
        CompletableFuture<PlayerProfile> cf = CompletableFuture.supplyAsync(() -> {
            reportToDatabase(uuid, Bukkit.getOfflinePlayer(uuid).getName());
            NameReply namereply = ChainZAPI.getNameDataManager().getNameFromUUID(uuid);
            PlayerSkinDataReply playerSkinDataReply = ChainZAPI.getPlayerSkinDataManager().getPlayerSkinData(uuid);
            PlayerEconomy coinsReply = ChainZAPI.getEconomyManager().getDatabase().getPlayerEconomy(uuid);
            LevelReply levelReply = ChainZAPI.getLevelSystem().getLevelInfo(uuid);
            ExpReply expReply = ChainZAPI.getLevelSystem().getExperienceInfo(uuid);
            PlayerServerReply playersvreply = ChainZAPI.getPlayerServerManager().getPlayerServer(uuid);
            OnlinePlayerProfile onlineProfile = new OnlinePlayerProfile(uuid, namereply.getName(), coinsReply.getCoins(), coinsReply.getMultiplier(), levelReply.getLevel(), expReply.getExp(), playerSkinDataReply.getValue(), playerSkinDataReply.getSignature(), playersvreply.getServer());
            PlayerProfileReply profileReply = new PlayerProfileReply(uuid, onlineProfile);

            return profileReply.getPlayerProfile();
        });
        try {
            return cf.get();
        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updatePlayerProfile(UUID uuid, PlayerProfile oldProfile, PlayerProfile newProfile, boolean async) {
        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
                ChainZAPI.getLevelSystem().setLevel(uuid, newProfile.getLevel());

                double expDiff = newProfile.getExp() - oldProfile.getExp();
                ChainZAPI.getLevelSystem().addExperience(uuid, (int) expDiff);

                double coinsDiff = newProfile.getCoins() - oldProfile.getCoins();
                ChainZAPI.getEconomyManager().getDatabase().addCoins(uuid, coinsDiff, false);

                ChainZAPI.getEconomyManager().getDatabase().setPlayerMultiplier(uuid, newProfile.getMultiplier());
            });
        } else {
            ChainZAPI.getLevelSystem().setLevel(uuid, newProfile.getLevel());

            double expDiff = newProfile.getExp() - oldProfile.getExp();
            ChainZAPI.getLevelSystem().addExperience(uuid, (int) expDiff);

            double coinsDiff = newProfile.getCoins() - oldProfile.getCoins();
            ChainZAPI.getEconomyManager().getDatabase().addCoins(uuid, coinsDiff, false);

            ChainZAPI.getEconomyManager().getDatabase().setPlayerMultiplier(uuid, newProfile.getMultiplier());
        }
    }
}
