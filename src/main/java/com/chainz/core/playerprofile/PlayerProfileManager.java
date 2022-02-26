package com.chainz.core.playerprofile;

import com.chainz.core.ChainZAPI;
import com.chainz.core.sql.SQLManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PlayerProfileManager implements PlayerProfileInterface {
    private final HashMap<UUID, Optional<PlayerProfile>> profileCache = new HashMap<>();
    private final HashMap<UUID, Optional<PlayerProfile>> dbCache = new HashMap<>();

    private final List<UUID> pending = new ArrayList<>();

    private final PlayerProfileDatabase db;

    public PlayerProfileManager(PlayerProfileDatabase playerProfileDatabase) {
        this.db = playerProfileDatabase;
    }

    public Double getCoins(UUID uuid) {
        return this.getPlayerProfileFromUUID(uuid).getCoins();
    }

    public boolean addCoins(UUID uuid, Double amount, boolean multiply) {
        Optional<PlayerProfile> profile = profileCache.get(uuid);
        if (profile == null || profile.isEmpty()) {
            return false;
        }

        amount = multiply ? (amount * profile.get().getMultiplier()) : amount;
        profile.get().addCoins(amount);
        return true;
    }

    public boolean setLevel(UUID uuid, int level) {
        Optional<PlayerProfile> profile = profileCache.get(uuid);
        if (profile == null || profile.isEmpty()) {
            return false;
        }

        profile.get().setLevel(level);
        return true;
    }

    public boolean setPlayerMultiplier(UUID uuid, Double multiplier) {
        Optional<PlayerProfile> profile = profileCache.get(uuid);
        if (profile == null || profile.isEmpty()) {
            return false;
        }

        profile.get().setMultiplier(multiplier);
        return true;
    }

    public boolean removeCoins(UUID uuid, Double remove) {
        Optional<PlayerProfile> profile = profileCache.get(uuid);
        if (profile == null || profile.isEmpty()) {
            return false;
        }

        profile.get().addCoins(-remove);
        return true;
    }

    public Double getPlayerMultiplier(UUID uuid) {
        return this.getPlayerProfileFromUUID(uuid).getMultiplier();
    }

    public PlayerProfile getPlayerProfileFromUUID(UUID uuid) {
        if (pending.contains(uuid))
            return new OnlinePlayerProfile(uuid, "Unknown", 0D, 0D, 0, 0, null, null, null);

        Optional<PlayerProfile> profile = profileCache.computeIfAbsent(uuid, u -> {
            pending.add(u);
            Optional<PlayerProfile> current = Optional.of(db.getPlayerProfileFromUUID(u));
            dbCache.put(u, current);
            pending.remove(u);
            PlayerProfile oldProfile = current.get();

            return Optional.of(new OnlinePlayerProfile(u, oldProfile.getName(), oldProfile.getCoins(),
                    oldProfile.getMultiplier(), oldProfile.getLevel(), oldProfile.getExp(),
                    oldProfile.getSkinValue(), oldProfile.getSkinSignature(), ChainZAPI.getServerName()));
        });

        if (profile.isEmpty())
            return new OnlinePlayerProfile(uuid, "Unknown", 0D, 0D, 0, 0, null, null, null);

        return profile.get();
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
        profileCache.computeIfPresent(uuid, (u, balance) -> {
            sync(u, balance);
            return null;
        });
    }

    private void sync(UUID uuid, Optional<PlayerProfile> balance) {
        Optional<PlayerProfile> old = dbCache.remove(uuid);

        if (balance == null || balance.isEmpty()) {
            return;
        }

        if (old == null || old.isEmpty()) {
            throw new IllegalStateException();
        }

        if (balance.get().differs(old.get())) {
            db.updatePlayerProfile(uuid, old.get(), balance.get());
        }
    }

    public void save(UUID uuid) {
        sync(uuid, profileCache.remove(uuid));
    }

    public void saveAll() {
        profileCache.entrySet().removeIf(e -> {
            sync(e.getKey(), e.getValue());
            return true;
        });
    }
}