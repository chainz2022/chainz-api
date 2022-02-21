package com.chainz.core.economy;

import com.chainz.core.Core;
import com.chainz.core.async.reply.CallbackReply;
import com.chainz.core.async.reply.CoinsReply;
import com.chainz.core.sql.SQLManager;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;

public class EconomyManager implements Economy {
    @Override
    public Boolean playerExists(String uuid) {
        boolean is = true;
        try {
            Statement statement = SQLManager.getConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM economy WHERE uuid = '" + uuid + "';");
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
        } catch (Exception ex2) {
            ex2.printStackTrace();
            is = false;
        }

        return is;
    }

    @Override
    public void addCoins(String uuid, Double add, boolean multiply) {
        CompletableFuture<ResultSet> cf = CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement statement = SQLManager.getConnection().prepareStatement("SELECT coins, multiplier FROM player WHERE uuid = '" + uuid + "';");
                ResultSet res = statement.executeQuery();

                res.next();
                double currentCoins = res.getDouble("coins");

                PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE player SET coins=? WHERE uuid=?");
                ps.clearParameters();
                ps.setString(2, uuid);

                if (multiply) {
                    double multiplier = res.getDouble("multiplier");
                    double multiplied = add * multiplier;
                    ps.setDouble(1, currentCoins + multiplied);
                } else {
                    ps.setDouble(1, currentCoins + add);
                }

                ps.executeUpdate();
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
    public void addCoinsAsync(String uuid, Double add, boolean multiply, CallbackReply reply) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT coins, multiplier FROM player WHERE uuid = '" + uuid + "';");
                res.next();
                double currentCoins = res.getDouble("coins");
                PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE player SET coins=? WHERE uuid=?");
                ps.clearParameters();
                ps.setString(2, uuid);

                if (multiply) {
                    double multiplier = res.getDouble("multiplier");
                    double multiplied = add * multiplier;
                    ps.setDouble(1, currentCoins + multiplied);
                } else {
                    ps.setDouble(1, currentCoins + add);
                }

                ps.executeUpdate();
                reply.then(null);
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
                try {
                    ps.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } catch (Exception ex2) {
                reply.error(ex2);
            }
        });
    }

    @Override
    public void setPlayerMultiplier(String uuid, Double multiplier) {
        CompletableFuture<ResultSet> cf = CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE player SET multiplier=? WHERE uuid=?");
                ps.clearParameters();
                ps.setString(2, uuid);
                ps.setDouble(1, multiplier);
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
    public void setPlayerMultiplierAsync(String uuid, Double multiplier, CallbackReply reply) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try {
                PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE player SET multiplier=? WHERE uuid=?");
                ps.clearParameters();
                ps.setString(2, uuid);
                ps.setDouble(1, multiplier);
                ps.executeUpdate();
                reply.then(null);
                try {
                    ps.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } catch (Exception ex2) {
                reply.error(ex2);
            }
        });
    }

    @Override
    public void removeCoins(String uuid, Double remove) {

        CompletableFuture<ResultSet> cf = CompletableFuture.supplyAsync(() -> {
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT coins, multiplier FROM player WHERE uuid = '" + uuid + "';");
                res.next();
                double coins = res.getDouble("coins");
                PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE player SET coins=? WHERE uuid=?");
                ps.clearParameters();
                ps.setString(2, uuid);
                ps.setDouble(1, coins - remove);
                ps.executeUpdate();
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
    public void removeCoinsAsync(String uuid, Double remove, CallbackReply reply) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT coins, multiplier FROM player WHERE uuid = '" + uuid + "';");
                res.next();
                double coins = res.getDouble("coins");
                PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE player SET coins=? WHERE uuid=?");
                ps.clearParameters();
                ps.setString(2, uuid);
                ps.setDouble(1, coins - remove);
                ps.executeUpdate();
                reply.then(null);
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
                try {
                    ps.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } catch (Exception ex2) {
                reply.error(ex2);
            }
        });
    }

    @Override
    public Double getCoins(String uuid) {
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(() -> {
            double coins;
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT coins,multiplier FROM player WHERE uuid = '" + uuid + "';");
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
    public void getCoinsAsync(String uuid, CallbackReply reply) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT coins,multiplier FROM player WHERE uuid = '" + uuid + "';");
                if (res.next()) {
                    CoinsReply coinsreply = new CoinsReply(uuid, res.getDouble("coins"), res.getDouble("multiplier"));
                    reply.then(coinsreply);
                } else {
                    reply.error(new Exception("Not found"));
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
                reply.error(ex2);
            }
        });
    }

    @Override
    public Double getPlayerMultiplier(String uuid) {
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(() -> {
            double multiplier;
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT coins,multiplier FROM player WHERE uuid = '" + uuid + "';");
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
}
