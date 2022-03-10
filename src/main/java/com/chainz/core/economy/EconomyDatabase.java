package com.chainz.core.economy;

import com.chainz.core.sql.SQLManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class EconomyDatabase implements Economy {
    @Override
    public boolean addCoins(UUID uuid, Double amount, boolean multiply) {
        CompletableFuture<Boolean> cf = CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement statement = SQLManager.getConnection().prepareStatement("SELECT coins, multiplier FROM player WHERE uuid = '" + uuid.toString() + "';");
                ResultSet res = statement.executeQuery();

                res.next();
                double currentCoins = res.getFloat("coins");

                PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE player SET coins=? WHERE uuid=?");
                ps.clearParameters();
                ps.setString(2, uuid.toString());

                if (multiply) {
                    double multiplier = res.getFloat("multiplier");
                    double multiplied = amount * multiplier;
                    ps.setDouble(1, currentCoins + multiplied);
                } else {
                    ps.setDouble(1, currentCoins + amount);
                }

                ps.executeUpdate();
                try {
                    res.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    return false;
                }
                try {
                    statement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    return false;
                }
                try {
                    ps.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    return false;
                }
            } catch (Exception ex2) {
                ex2.printStackTrace();
                return false;
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
    public boolean setPlayerMultiplier(UUID uuid, Double multiplier) {
        CompletableFuture<Boolean> cf = CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE player SET multiplier=? WHERE uuid=?");
                ps.clearParameters();
                ps.setString(2, uuid.toString());
                ps.setDouble(1, multiplier);
                ps.executeUpdate();
                try {
                    ps.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    return false;
                }
            } catch (Exception ex2) {
                ex2.printStackTrace();
                return false;
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

    public void updateEconomy(UUID uuid, Double coins, Double multiplier) {
        CompletableFuture<ResultSet> cf = CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE player SET coins=?, multiplier=? WHERE uuid=?");
                ps.clearParameters();
                ps.setString(3, uuid.toString());
                ps.setDouble(2, multiplier);
                ps.setDouble(1, coins);
                ps.executeUpdate();
                try {
                    ps.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public boolean removeCoins(UUID uuid, Double remove) {
        return this.addCoins(uuid, -remove, false);
    }

    public PlayerEconomy getPlayerEconomy(UUID uuid) {
        CompletableFuture<PlayerEconomy> cf = CompletableFuture.supplyAsync(() -> {
            PlayerEconomy playerEconomy;
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT coins, multiplier FROM player WHERE uuid = '" + uuid.toString() + "';");
                if (res.next()) {
                    playerEconomy = new PlayerEconomy(uuid, res.getDouble("coins"), res.getDouble("multiplier"));
                } else {
                    playerEconomy = null;
                }
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
            } catch (Exception ex2) {
                ex2.printStackTrace();
                playerEconomy = null;
            }
            return playerEconomy;
        });
        try {
            return cf.get();
        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Double getCoins(UUID uuid) {
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(() -> {
            double coins;
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT coins,multiplier FROM player WHERE uuid = '" + uuid.toString() + "';");
                if (res.next()) {
                    coins = res.getDouble("coins");
                } else {
                    coins = -1;
                }
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
            } catch (Exception ex2) {
                ex2.printStackTrace();
                coins = -1;
            }
            return coins;
        });
        try {
            return cf.get();
        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
            return 0.0D;
        }
    }

    @Override
    public Double getPlayerMultiplier(UUID uuid) {
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(() -> {
            double multiplier;
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT coins,multiplier FROM player WHERE uuid = '" + uuid.toString() + "';");
                res.next();
                multiplier = res.getDouble("multiplier");
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
            } catch (Exception ex2) {
                ex2.printStackTrace();
                multiplier = -1;
            }
            return multiplier;
        });
        try {
            return cf.get();
        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
            return 0.0D;
        }
    }

    @Override
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

}
