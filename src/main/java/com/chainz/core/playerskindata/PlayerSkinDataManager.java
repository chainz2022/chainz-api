package com.chainz.core.playerskindata;

import com.chainz.core.ChainZAPI;
import com.chainz.core.Core;
import com.chainz.core.async.reply.NameReply;
import com.chainz.core.async.reply.PlayerSkinDataReply;
import com.chainz.core.sql.SQLManager;
import com.chainz.core.utils.config.ConfigManager;
import es.eltrueno.npc.skin.SkinData;
import es.eltrueno.npc.skin.SkinType;
import es.eltrueno.npc.skin.TruenoNPCSkin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PlayerSkinDataManager implements PlayerSkinData {
    private static String[] getSkinData(UUID uuid) {
        CompletableFuture<String[]> cf = CompletableFuture.supplyAsync(() -> {
            TruenoNPCSkin skin = new TruenoNPCSkin(Core.core, SkinType.IDENTIFIER, uuid.toString().replaceAll("-", ""));
            SkinData data = skin.getSkinData();
            final String value = data.getValue();
            final String signature = data.getSignature();
            final String[] skindata = {value, signature};
            return skindata;
        });

        try {
            return cf.get();
        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PlayerSkinDataReply getGameProfileChange(Player p) {
        CompletableFuture<PlayerSkinDataReply> cf = CompletableFuture.supplyAsync(() -> {
            try (Jedis j = Core.pool.getResource()) {
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                j.select(2);
                if (j.exists("playerprofilechange:" + p.getUniqueId().toString())) {
                    Map<String, String> profiledata = j.hgetAll("playerprofilechange:" + p.getUniqueId().toString());
                    return new PlayerSkinDataReply(profiledata.get("skinuuid"), profiledata.get("name"), profiledata.get("skinvalue"), profiledata.get("skinsignature"));
                } else {
                    return null;
                }
            } catch (Exception ex) {
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

    @Override
    public PlayerSkinDataReply getGameProfileChange(UUID uuid) {
        CompletableFuture<PlayerSkinDataReply> cf = CompletableFuture.supplyAsync(() -> {
            try (Jedis j = Core.pool.getResource()) {
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                j.select(2);
                if (j.exists("playerprofilechange:" + uuid.toString())) {
                    final Map<String, String> profiledata = j.hgetAll("playerprofilechange:" + uuid);
                    PlayerSkinDataReply reply = new PlayerSkinDataReply(profiledata.get("skinuuid"), profiledata.get("name"), profiledata.get("skinvalue"), profiledata.get("skinsignature"));
                    return reply;
                } else {
                    return null;
                }
            } catch (Exception ex) {
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

    @Override
    public boolean changeNickname(Player p, String name) {
        if (ChainZAPI.getNameDataManager().existsName(name)) {
            return false;
        }

        CompletableFuture<Boolean> cf = CompletableFuture.supplyAsync(() -> {
            final Jedis j = Core.pool.getResource();
            if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
            }
            j.select(2);
            Set<String> cachedskins = j.keys("playerskindata:*");
            if (cachedskins != null && !cachedskins.isEmpty()) {
                int r = (cachedskins.size() == 1) ? 0 : new Random().nextInt(cachedskins.size() - 1);
                String cachedskinkey = (String) cachedskins.toArray()[r];
                String cachedskinuuid = cachedskinkey.split(":")[1];
                String cachedskinvalue = j.hget(cachedskinkey, "value");
                String cachedskinsignature = j.hget(cachedskinkey, "signature");
                Map<String, String> profiledata = new HashMap<String, String>();
                profiledata.put("name", name);
                profiledata.put("skinuuid", cachedskinuuid);
                profiledata.put("skinvalue", cachedskinvalue);
                profiledata.put("skinsignature", cachedskinsignature);
                j.hmset("playerprofilechange:" + p.getUniqueId().toString(), profiledata);
                j.set("fakenick:" + name, p.getUniqueId().toString());
                j.close();
            } else {
                j.close();

                Jedis je = Core.pool.getResource();
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    je.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                je.set("fakenick:" + name, p.getUniqueId().toString());

                NameReply reply = ChainZAPI.getNameDataManager().getUUIDFromAllMethods(name);
                if (reply != null) {
                    String[] skindata = getSkinData(reply.getUUID());
                    String[] finalskindata = null;
                    if (skindata == null) {
                        String[] steveskindata = finalskindata = new String[]{"eyJ0aW1lc3RhbXAiOjE1MDc4MzQxNTQyOTUsInByb2ZpbGVJZCI6Ijg2NjdiYTcxYjg1YTQwMDRhZjU0NDU3YTk3MzRlZWQ3IiwicHJvZmlsZU5hbWUiOiJTdGV2ZSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDU2ZWVjMWMyMTY5YzhjNjBhN2FlNDM2YWJjZDJkYzU0MTdkNTZmOGFkZWY4NGYxMTM0M2RjMTE4OGZlMTM4In0sIkNBUEUiOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iNzY3ZDQ4MzI1ZWE1MzI0NTYxNDA2YjhjODJhYmJkNGUyNzU1ZjExMTUzY2Q4NWFiMDU0NWNjMiJ9fX0=", "xKW8YNS4Xbz+JPJ7Uli48hAkp5ymrdM2B2V5QyW7wiUOCO8WDFRm3ZKhuq0xec2ZKa3cRpuqZ9lzWvGRa/5oF11D124gsJcDu5axjP20ujsq+C+KURCyzrbpGi8kjNwOwCwUUEQi1u0CXf3o3kvYWvVi62KEtCaKT51mpadb45UlRj8YHo2INFD3s8r6pfc2rZyl8m9OoKlgktPtM0tq4ZVirJxH6wVV+M3gKGby0ByMJLM3+RwEGflYOKemH+adiYAPzJvAySZUwKe8H6IxRpdMsUC90lMaFwOEcq5JjbNgTIP8mr+kbJmQ4Zqeai5bOu34Wek28VFd0ViIjsNRzXIcTE0UYxyDNoEKlNIwCV5s+gCJpJi7KeNx1B3GTQUuogEIAhqf6/5H6+kT6og8/OilB6L2wb69667j1WIVasLjx+1s65ft8v7YAzV38kyOfAKCpA13toCbhCATvUr37qeXBwAGBWGJIFt10UfpvJZ2Onjo8nxeeM6kLLAaUAmyB54XN6S4tV5pWk2/ksDk3lIFGs04V2I5rJ9tiLL5GthglqFe5Iz76ZERB1I0njOzUYLZSJWyBIKhuzyh+ZdN0C22rD7tyW/+pzmaFY9XpAYQ4tO1lX7NgTkynX1+dofClO4XCcig06Cs5d2yGiBOFw4lWiO+UCNNwnZ9QeyFDMw="};
                    } else {
                        finalskindata = skindata;
                    }
                    final Map<String, String> profiledata = new HashMap<String, String>();
                    profiledata.put("name", name);
                    profiledata.put("skinuuid", reply.getUUID().toString());
                    profiledata.put("skinvalue", finalskindata[0]);
                    profiledata.put("skinsignature", finalskindata[1]);
                    je.hmset("playerprofilechange:" + p.getUniqueId().toString(), profiledata);
                    je.close();
                    return true;
                } else {
                    List<String> uuids = new ArrayList<>();
                    uuids.add("5e18307ff29e4454a030423ef1aaaeb4");
                    uuids.add("3a93c7b324f64367be6222ff0c10e66f");
                    uuids.add("fd312f6472db4c68b28a97aca01fa4de");
                    uuids.add("9968797e40e343d9967b31646deb6fdb");
                    uuids.add("02ec7a3991a244eeae6114bbb6b0513b");
                    uuids.add("4b518b75f7c54903a9f972e0551385bc");
                    uuids.add("3821cbbcec814add9f358263ff7cadc6");
                    uuids.add("46ae37f028ef4461be2d7c3cac8713a2");
                    UUID randomUUID = UUID.fromString(uuids.get(new Random().nextInt(uuids.size() - 1)));
                    String[] skindata = getSkinData(randomUUID);
                    String[] finalskindata = null;
                    if (skindata == null) {
                        String[] steveskindata = finalskindata = new String[]{"eyJ0aW1lc3RhbXAiOjE1MDc4MzQxNTQyOTUsInByb2ZpbGVJZCI6Ijg2NjdiYTcxYjg1YTQwMDRhZjU0NDU3YTk3MzRlZWQ3IiwicHJvZmlsZU5hbWUiOiJTdGV2ZSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDU2ZWVjMWMyMTY5YzhjNjBhN2FlNDM2YWJjZDJkYzU0MTdkNTZmOGFkZWY4NGYxMTM0M2RjMTE4OGZlMTM4In0sIkNBUEUiOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iNzY3ZDQ4MzI1ZWE1MzI0NTYxNDA2YjhjODJhYmJkNGUyNzU1ZjExMTUzY2Q4NWFiMDU0NWNjMiJ9fX0=", "xKW8YNS4Xbz+JPJ7Uli48hAkp5ymrdM2B2V5QyW7wiUOCO8WDFRm3ZKhuq0xec2ZKa3cRpuqZ9lzWvGRa/5oF11D124gsJcDu5axjP20ujsq+C+KURCyzrbpGi8kjNwOwCwUUEQi1u0CXf3o3kvYWvVi62KEtCaKT51mpadb45UlRj8YHo2INFD3s8r6pfc2rZyl8m9OoKlgktPtM0tq4ZVirJxH6wVV+M3gKGby0ByMJLM3+RwEGflYOKemH+adiYAPzJvAySZUwKe8H6IxRpdMsUC90lMaFwOEcq5JjbNgTIP8mr+kbJmQ4Zqeai5bOu34Wek28VFd0ViIjsNRzXIcTE0UYxyDNoEKlNIwCV5s+gCJpJi7KeNx1B3GTQUuogEIAhqf6/5H6+kT6og8/OilB6L2wb69667j1WIVasLjx+1s65ft8v7YAzV38kyOfAKCpA13toCbhCATvUr37qeXBwAGBWGJIFt10UfpvJZ2Onjo8nxeeM6kLLAaUAmyB54XN6S4tV5pWk2/ksDk3lIFGs04V2I5rJ9tiLL5GthglqFe5Iz76ZERB1I0njOzUYLZSJWyBIKhuzyh+ZdN0C22rD7tyW/+pzmaFY9XpAYQ4tO1lX7NgTkynX1+dofClO4XCcig06Cs5d2yGiBOFw4lWiO+UCNNwnZ9QeyFDMw="};
                    } else {
                        finalskindata = skindata;
                    }
                    final Map<String, String> profiledata = new HashMap<String, String>();
                    profiledata.put("name", name);
                    profiledata.put("skinuuid", String.valueOf(randomUUID));
                    profiledata.put("skinvalue", finalskindata[0]);
                    profiledata.put("skinsignature", finalskindata[1]);
                    je.hmset("playerprofilechange:" + p.getUniqueId().toString(), profiledata);
                    je.close();
                    return true;
                }
            }
            return true;
        });
        try {
            return cf.get();
        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void cachePlayerSkin(UUID uuid) {
        String[] call = getSkinData(uuid);
        if (call != null) {
            final String[] apidata = call;
            Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
                try (Jedis j = Core.pool.getResource()) {
                    if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                        j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                    }
                    j.select(2);
                    final Map<String, String> skindata = new HashMap<String, String>();
                    skindata.put("value", apidata[0]);
                    skindata.put("signature", apidata[1]);
                    j.hmset("playerskindata:" + uuid.toString(), skindata);
                    j.expire("playerskindata:" + uuid, 60);
                }
            });
        }
    }

    public void cachePlayerSkin(UUID uuid, final String[] playerskindata) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try (Jedis j = Core.pool.getResource()) {
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                j.select(2);
                final Map<String, String> skindata = new HashMap<String, String>();
                skindata.put("value", playerskindata[0]);
                skindata.put("signature", playerskindata[1]);
                j.hmset("playerskindata:" + uuid.toString(), skindata);
                j.expire("playerskindata:" + uuid, 200);
            }
        });
    }

    @Override
    public PlayerSkinDataReply getPlayerSkinData(UUID uuid) {
        CompletableFuture<PlayerSkinDataReply> cf = CompletableFuture.supplyAsync(() -> {
            try (Jedis j = Core.pool.getResource()) {
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                j.select(2);
                if (j.exists("playerskindata:" + uuid)) {
                    Map<String, String> skindata = j.hgetAll("playerskindata:" + uuid);
                    PlayerSkinDataReply skindatareply = new PlayerSkinDataReply(uuid, skindata.get("value"), skindata.get("signature"));
                    return skindatareply;
                } else {
                    String[] apiskindata = getSkinData(uuid);
                    if (apiskindata != null) {
                        PlayerSkinDataManager.this.cachePlayerSkin(uuid, apiskindata);
                        final PlayerSkinDataReply skindatareply = new PlayerSkinDataReply(uuid, apiskindata[0], apiskindata[1]);
                        return skindatareply;
                    } else {
                        try {
                            PreparedStatement statement = SQLManager.getConnection().prepareStatement("SELECT * FROM skindata WHERE uuid = '" + uuid + "';");
                            ResultSet res = statement.executeQuery();
                            PlayerSkinDataReply value;
                            if (res.next()) {
                                value = new PlayerSkinDataReply(uuid, res.getString("value"), res.getString("signature"));
                                PlayerSkinDataManager.this.cachePlayerSkin(uuid, new String[]{value.getValue(), value.getSignature()});
                            } else {
                                final String[] steveskindata = {"eyJ0aW1lc3RhbXAiOjE1MDc4MzQxNTQyOTUsInByb2ZpbGVJZCI6Ijg2NjdiYTcxYjg1YTQwMDRhZjU0NDU3YTk3MzRlZWQ3IiwicHJvZmlsZU5hbWUiOiJTdGV2ZSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDU2ZWVjMWMyMTY5YzhjNjBhN2FlNDM2YWJjZDJkYzU0MTdkNTZmOGFkZWY4NGYxMTM0M2RjMTE4OGZlMTM4In0sIkNBUEUiOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iNzY3ZDQ4MzI1ZWE1MzI0NTYxNDA2YjhjODJhYmJkNGUyNzU1ZjExMTUzY2Q4NWFiMDU0NWNjMiJ9fX0=", "xKW8YNS4Xbz+JPJ7Uli48hAkp5ymrdM2B2V5QyW7wiUOCO8WDFRm3ZKhuq0xec2ZKa3cRpuqZ9lzWvGRa/5oF11D124gsJcDu5axjP20ujsq+C+KURCyzrbpGi8kjNwOwCwUUEQi1u0CXf3o3kvYWvVi62KEtCaKT51mpadb45UlRj8YHo2INFD3s8r6pfc2rZyl8m9OoKlgktPtM0tq4ZVirJxH6wVV+M3gKGby0ByMJLM3+RwEGflYOKemH+adiYAPzJvAySZUwKe8H6IxRpdMsUC90lMaFwOEcq5JjbNgTIP8mr+kbJmQ4Zqeai5bOu34Wek28VFd0ViIjsNRzXIcTE0UYxyDNoEKlNIwCV5s+gCJpJi7KeNx1B3GTQUuogEIAhqf6/5H6+kT6og8/OilB6L2wb69667j1WIVasLjx+1s65ft8v7YAzV38kyOfAKCpA13toCbhCATvUr37qeXBwAGBWGJIFt10UfpvJZ2Onjo8nxeeM6kLLAaUAmyB54XN6S4tV5pWk2/ksDk3lIFGs04V2I5rJ9tiLL5GthglqFe5Iz76ZERB1I0njOzUYLZSJWyBIKhuzyh+ZdN0C22rD7tyW/+pzmaFY9XpAYQ4tO1lX7NgTkynX1+dofClO4XCcig06Cs5d2yGiBOFw4lWiO+UCNNwnZ9QeyFDMw="};
                                value = new PlayerSkinDataReply(uuid, steveskindata[0], steveskindata[1]);
                            }
                            try {
                                res.close();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                                return null;
                            }
                            try {
                                statement.close();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                                return null;
                            }
                            return value;
                        } catch (Exception ex2) {
                            ex2.printStackTrace();
                            return null;
                        }
                    }
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
