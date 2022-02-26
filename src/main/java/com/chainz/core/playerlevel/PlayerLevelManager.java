package com.chainz.core.playerlevel;

import com.chainz.core.Core;
import com.chainz.core.async.reply.ExpReply;
import com.chainz.core.async.reply.LevelReply;
import com.chainz.core.playerlevel.events.NetworkExperienceAddedEvent;
import com.chainz.core.playerlevel.events.NetworkLevelUpEvent;
import com.chainz.core.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerLevelManager implements PlayerLevel {
    public static void setDefaults(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            String uuid = p.getUniqueId().toString();
            try {
                PreparedStatement statement = SQLManager.getConnection().prepareStatement("SELECT * FROM playerlevel WHERE uuid = '" + uuid + "';");
                ResultSet res = statement.executeQuery();
                if (!res.next()) {
                    statement.executeUpdate("INSERT INTO playerlevel (`uuid`, `level`, `xp`) VALUES ('" + uuid + "', '1', '0');");
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
            } catch (SQLException ex2) {
                ex2.printStackTrace();
            }
        });
    }

    private int getNeededExp(int tolevel) {
        return 1000 + 250 * (tolevel - 2);
    }

    @Override
    public void addExperience(UUID uuid, Integer add) {
        CompletableFuture.supplyAsync(() -> {
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM playerlevel WHERE uuid = '" + uuid + "';");
                res.next();
                int xp = res.getInt("xp");
                int lvl = res.getInt("level");
                int xpneeded = this.getNeededExp(lvl + 1);
                PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE playerlevel SET level=?, xp=? WHERE uuid=?");
                if (xp + add >= xpneeded) {
                    int totalexpneeded = this.getNeededExp(lvl + 1);
                    int tolevel = lvl + 1;
                    for (int i = lvl + 2; xp + add >= totalexpneeded + this.getNeededExp(i); totalexpneeded += this.getNeededExp(i), ++i) {
                        tolevel = i;
                    }
                    int rest = xp + add - totalexpneeded;
                    ps.clearParameters();
                    ps.setString(3, uuid.toString());
                    ps.setInt(2, rest);
                    ps.setInt(1, tolevel);
                    ps.executeUpdate();
                    if (Bukkit.getPlayer(uuid) != null) {
                        Player p = Bukkit.getPlayer(uuid);
                        NetworkLevelUpEvent event = new NetworkLevelUpEvent(p, xp, add, xp + add, lvl, tolevel);
                        Bukkit.getPluginManager().callEvent(event);
                    }
                } else {
                    ps.clearParameters();
                    ps.setString(3, uuid.toString());
                    ps.setInt(2, xp + add);
                    ps.setInt(1, lvl);
                    ps.executeUpdate();
                }
                if (Bukkit.getPlayer(uuid) != null) {
                    Player p2 = Bukkit.getPlayer(uuid);
                    NetworkExperienceAddedEvent event2 = new NetworkExperienceAddedEvent(p2, xp, add, xp + add, lvl);
                    Bukkit.getPluginManager().callEvent(event2);
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
    public Integer getExperience(UUID uuid) {
        CompletableFuture<Integer> cf = CompletableFuture.supplyAsync(() -> {
            int xp;
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM playerlevel WHERE uuid = '" + uuid + "';");
                res.next();
                xp = res.getInt("xp");
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
                xp = -1;
            }
            return xp;
        });
        try {
            return cf.get();
        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Integer getLevel(UUID uuid) {
        CompletableFuture<Integer> cf = CompletableFuture.supplyAsync(() -> {
            int lvl;
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM playerlevel WHERE uuid = '" + uuid + "';");
                res.next();
                lvl = res.getInt("level");
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
                lvl = -1;
            }
            return lvl;
        });
        try {
            return cf.get();
        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Integer getExperienceToNextLevel(UUID uuid) {
        CompletableFuture<Integer> cf = CompletableFuture.supplyAsync(() -> {
            int xpret;
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM playerlevel WHERE uuid = '" + uuid + "';");
                res.next();
                int xp = res.getInt("xp");
                int level = res.getInt("level");
                int tolevel = level + 1;
                int xpneeded = this.getNeededExp(tolevel);
                xpret = xpneeded - xp;
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
                xpret = -1;
            }
            return xpret;
        });
        try {
            return cf.get();
        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Integer getLevelPercent(UUID uuid) {
        CompletableFuture<Integer> cf = CompletableFuture.supplyAsync(() -> {
            int percent;
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM playerlevel WHERE uuid = '" + uuid + "';");
                res.next();
                int xp = res.getInt("xp");
                int level = res.getInt("level");
                int tolevel = level + 1;
                int xpneeded = this.getNeededExp(tolevel);
                percent = xp * 100 / xpneeded;
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
                percent = -1;
            }
            return percent;
        });
        try {
            return cf.get();
        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void setLevel(UUID uuid, Integer level) {
        CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE playerlevel SET level=? WHERE uuid=?");
                ps.clearParameters();
                ps.setString(2, uuid.toString());
                ps.setInt(1, level);
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
    public LevelReply getLevelInfo(UUID uuid) {
        CompletableFuture<LevelReply> cf = CompletableFuture.supplyAsync(() -> {
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM playerlevel WHERE uuid = '" + uuid + "';");
                res.next();
                int xp = res.getInt("xp");
                int level = res.getInt("level");
                int tolevel = level + 1;
                int xpneeded = PlayerLevelManager.this.getNeededExp(tolevel);
                int percent = xp * 100 / xpneeded;
                LevelReply lvlreply = new LevelReply(uuid, level, percent);
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
                return lvlreply;
            } catch (Exception ex2) {
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
    public ExpReply getExperienceInfo(UUID uuid) {
        CompletableFuture<ExpReply> cf = CompletableFuture.supplyAsync(() -> {
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM playerlevel WHERE uuid = '" + uuid.toString() + "';");
                res.next();
                int xp = res.getInt("xp");
                int level = res.getInt("level");
                int tolevel = level + 1;
                int xpneeded = PlayerLevelManager.this.getNeededExp(tolevel);
                int xptolevel = xpneeded - xp;
                ExpReply expreply = new ExpReply(uuid, xp, xptolevel);
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
                return expreply;
            } catch (Exception ex2) {
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
}
