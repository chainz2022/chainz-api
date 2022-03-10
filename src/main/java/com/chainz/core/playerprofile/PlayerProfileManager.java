package com.chainz.core.playerprofile;

import com.chainz.core.ChainZAPI;
import com.chainz.core.sql.SQLManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerProfileManager implements PlayerProfileInterface {
    private final HashMap<UUID, PlayerProfile> profileCache = new HashMap<>();
    private final HashMap<UUID, PlayerProfile> dbCache = new HashMap<>();

    private final List<UUID> pending = new ArrayList<>();

    private final PlayerProfileDatabase db;

    public PlayerProfileManager(PlayerProfileDatabase playerProfileDatabase) {
        this.db = playerProfileDatabase;
    }

    public Double getCoins(UUID uuid) {
        PlayerProfile profile = profileCache.get(uuid);
        if (profile == null) {
            return 0.0D;
        }

        return profile.getCoins();
    }

    public boolean addCoins(UUID uuid, Double amount, boolean multiply) {
        PlayerProfile profile = profileCache.get(uuid);
        if (profile == null) {
            return false;
        }

        amount = multiply ? (amount * profile.getMultiplier()) : amount;
        profile.addCoins(amount);
        return true;
    }

    public boolean setLevel(UUID uuid, int level) {
        PlayerProfile profile = profileCache.get(uuid);
        if (profile == null) {
            return false;
        }

        profile.setLevel(level);
        return true;
    }

    public boolean setPlayerMultiplier(UUID uuid, Double multiplier) {
        PlayerProfile profile = profileCache.get(uuid);
        if (profile == null) {
            return false;
        }

        profile.setMultiplier(multiplier);
        return true;
    }

    public boolean removeCoins(UUID uuid, Double remove) {
        PlayerProfile profile = profileCache.get(uuid);
        if (profile == null) {
            return false;
        }

        profile.addCoins(-remove);
        return true;
    }

    public Double getPlayerMultiplier(UUID uuid) {
        PlayerProfile profile = profileCache.get(uuid);
        if (profile == null) {
            return 0.0D;
        }

        return profile.getMultiplier();
    }

    public PlayerProfile getPlayerProfileFromUUID(UUID uuid) {
        if (pending.contains(uuid))
            return new OnlinePlayerProfile(uuid, "Unknown", 0D, 0D, 0, 0, null, null, null);

        return profileCache.computeIfAbsent(uuid, u -> {
            pending.add(u);
            PlayerProfile current = db.getPlayerProfileFromUUID(u);
            dbCache.put(u, current);
            pending.remove(u);

            return new OnlinePlayerProfile(u, current.getName(), current.getCoins(),
                    current.getMultiplier(), current.getLevel(), current.getExp(),
                    current.getSkinValue(), current.getSkinSignature(), ChainZAPI.getServerName());
        });
    }

    public boolean playerExists(UUID uuid) {
        CompletableFuture<Boolean> cf = CompletableFuture.supplyAsync(() -> {
            boolean is;
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM player WHERE uuid = '" + uuid.toString() + "';");
                res.next();
                is = res.getRow() != 0;
                try {
                    res.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                try {
                    statement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                return is;
            } catch (Exception ex2) {
                ex2.printStackTrace();
                is = false;
            }
            return is;
        });
        try {
            return cf.get();
        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void consistency(UUID uuid) {
        profileCache.computeIfPresent(uuid, (u, profile) -> {
            sync(u, profile, true);
            return null;
        });
    }

    private void sync(UUID uuid, PlayerProfile profile, boolean async) {
        PlayerProfile old = dbCache.remove(uuid);

        if (profile == null) {
            return;
        }

        if (old == null) {
            throw new IllegalStateException();
        }

        if (profile.differs(old)) {
            db.updatePlayerProfile(uuid, old, profile, async);
        }
    }

    public void save(UUID uuid) {
        sync(uuid, profileCache.remove(uuid), true);
    }

    public void saveAll() {
        profileCache.entrySet().removeIf(e -> {
            sync(e.getKey(), e.getValue(), false);
            return true;
        });
    }
}