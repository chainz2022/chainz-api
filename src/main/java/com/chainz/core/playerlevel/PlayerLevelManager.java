package com.chainz.core.playerlevel;

import com.chainz.core.Core;
import com.chainz.core.async.reply.CallbackReply;
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
    public void addeExperience(String uuid, Integer add) {
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
                ps.setString(3, uuid);
                ps.setInt(2, rest);
                ps.setInt(1, tolevel);
                ps.executeUpdate();
                if (Bukkit.getPlayer(UUID.fromString(uuid)) != null) {
                    Player p = Bukkit.getPlayer(UUID.fromString(uuid));
                    NetworkLevelUpEvent event = new NetworkLevelUpEvent(p, xp, add, xp + add, lvl, tolevel);
                    Bukkit.getPluginManager().callEvent(event);
                }
            } else {
                ps.clearParameters();
                ps.setString(3, uuid);
                ps.setInt(2, xp + add);
                ps.setInt(1, lvl);
                ps.executeUpdate();
            }
            if (Bukkit.getPlayer(UUID.fromString(uuid)) != null) {
                Player p2 = Bukkit.getPlayer(UUID.fromString(uuid));
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
    }

    @Override
    public void addExperienceAsync(String uuid, Integer add, CallbackReply reply) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, new Runnable() {
            @Override
            public void run() {
                try {
                    Statement statement = SQLManager.getConnection().createStatement();
                    ResultSet res = statement.executeQuery("SELECT * FROM playerlevel WHERE uuid = '" + uuid + "';");
                    res.next();
                    int xp = res.getInt("xp");
                    int lvl = res.getInt("level");
                    int xpneeded = PlayerLevelManager.this.getNeededExp(lvl + 1);
                    PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE playerlevel SET level=?, xp=? WHERE uuid=?");
                    if (xp + add >= xpneeded) {
                        int totalexpneeded = PlayerLevelManager.this.getNeededExp(lvl + 1);
                        int tolevel = lvl + 1;
                        for (int i = lvl + 2; xp + add >= totalexpneeded + PlayerLevelManager.this.getNeededExp(i); totalexpneeded += PlayerLevelManager.this.getNeededExp(i), ++i) {
                            tolevel = i;
                        }
                        int rest = xp + add - totalexpneeded;
                        ps.clearParameters();
                        ps.setString(3, uuid);
                        ps.setInt(2, rest);
                        ps.setInt(1, tolevel);
                        ps.executeUpdate();
                        if (Bukkit.getPlayer(UUID.fromString(uuid)) != null) {
                            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
                            NetworkLevelUpEvent event = new NetworkLevelUpEvent(p, xp, add, xp + add, lvl, tolevel);
                            Bukkit.getPluginManager().callEvent(event);
                        }
                    } else {
                        ps.clearParameters();
                        ps.setString(3, uuid);
                        ps.setInt(2, xp + add);
                        ps.setInt(1, lvl);
                        ps.executeUpdate();
                    }
                    reply.then(null);
                    if (Bukkit.getPlayer(UUID.fromString(uuid)) != null) {
                        Player p2 = Bukkit.getPlayer(UUID.fromString(uuid));
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
                    reply.error(ex2);
                }
            }
        });
    }

    @Override
    public Integer getExperience(String uuid) {
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
    }

    @Override
    public void getExperienceAsync(String uuid, CallbackReply reply) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM playerlevel WHERE uuid = '" + uuid + "';");
                res.next();
                ExpReply expreply = new ExpReply(uuid, res.getInt("xp"), null);
                reply.then(expreply);
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
    public Integer getLevel(String uuid) {
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
    }

    @Override
    public void getLevelAsync(String uuid, CallbackReply reply) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM playerlevel WHERE uuid = '" + uuid + "';");
                res.next();
                LevelReply lvlreply = new LevelReply(uuid, res.getInt("level"), null);
                reply.then(lvlreply);
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
    public Integer getExperienceToNextLevel(String uuid) {
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
    }

    @Override
    public void getExperienceToNextLevelAsync(String uuid, CallbackReply reply) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM playerlevel WHERE uuid = '" + uuid + "';");
                res.next();
                int xp = res.getInt("xp");
                int level = res.getInt("level");
                int tolevel = level + 1;
                int xpneeded = PlayerLevelManager.this.getNeededExp(tolevel);
                int xptolevel = xpneeded - xp;
                ExpReply expreply = new ExpReply(uuid, null, xptolevel);
                reply.then(expreply);
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
    public Integer getLevelPercent(String uuid) {
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
    }

    @Override
    public void getLevelPercentAsync(String uuid, CallbackReply reply) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, new Runnable() {
            @Override
            public void run() {
                try {
                    Statement statement = SQLManager.getConnection().createStatement();
                    ResultSet res = statement.executeQuery("SELECT * FROM playerlevel WHERE uuid = '" + uuid + "';");
                    res.next();
                    int xp = res.getInt("xp");
                    int level = res.getInt("level");
                    int tolevel = level + 1;
                    int xpneeded = PlayerLevelManager.this.getNeededExp(tolevel);
                    int percent = xp * 100 / xpneeded;
                    LevelReply lvlreply = new LevelReply(uuid, null, percent);
                    reply.then(lvlreply);
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
            }
        });
    }

    @Override
    public void setLevel(String uuid, Integer level) {
        try {
            PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE playerlevel SET level=? WHERE uuid=?");
            ps.clearParameters();
            ps.setString(2, uuid);
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
    }

    @Override
    public void setLevelAsync(String uuid, Integer level, CallbackReply reply) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE playerlevel SET level=? WHERE uuid=?");
                    ps.clearParameters();
                    ps.setString(2, uuid);
                    ps.setInt(1, level);
                    ps.executeUpdate();
                    LevelReply lvlreply = new LevelReply(uuid, level, null);
                    reply.then(lvlreply);
                    try {
                        ps.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } catch (Exception ex2) {
                    reply.error(ex2);
                }
            }
        });
    }

    @Override
    public void getLevelInfoAsync(String uuid, CallbackReply reply) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, new Runnable() {
            @Override
            public void run() {
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
                    reply.then(lvlreply);
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
            }
        });
    }

    @Override
    public void getExperienceInfoAsync(String uuid, CallbackReply reply) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, new Runnable() {
            @Override
            public void run() {
                try {
                    Statement statement = SQLManager.getConnection().createStatement();
                    ResultSet res = statement.executeQuery("SELECT * FROM playerlevel WHERE uuid = '" + uuid + "';");
                    res.next();
                    int xp = res.getInt("xp");
                    int level = res.getInt("level");
                    int tolevel = level + 1;
                    int xpneeded = PlayerLevelManager.this.getNeededExp(tolevel);
                    int xptolevel = xpneeded - xp;
                    ExpReply expreply = new ExpReply(uuid, xp, xptolevel);
                    reply.then(expreply);
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
            }
        });
    }
}
