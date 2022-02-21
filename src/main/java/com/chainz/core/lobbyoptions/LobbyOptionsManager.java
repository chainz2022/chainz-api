package com.chainz.core.lobbyoptions;

import com.chainz.core.Core;
import com.chainz.core.async.reply.CallbackReply;
import com.chainz.core.async.reply.LobbyOptionsReply;
import com.chainz.core.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LobbyOptionsManager implements LobbyOptions {
    public static boolean playerExists(Player p) {
        String uuid = p.getUniqueId() + "";
        try {
            Statement statement = SQLManager.getConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM lobbyoptions WHERE uuid = '" + uuid + "';");
            res.next();
            return res.getRow() != 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void setDefaults(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            String uuid = p.getUniqueId().toString();
            try {
                PreparedStatement statement = SQLManager.getConnection().prepareStatement("SELECT * FROM lobbyoptions WHERE uuid = '" + uuid + "';");
                ResultSet res = statement.executeQuery();
                if (!res.next()) {
                    statement.executeUpdate("INSERT INTO lobbyoptions (`uuid`, `visibility`, `speed`, `fly`) VALUES ('" + uuid + "', 'true', 'false', 'false');");
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

    @Override
    public void getLobbyOptionsAsync(String uuid, CallbackReply reply) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try {
                boolean visibility = LobbyOptionsManager.this.getVisibility(uuid);
                boolean speed = LobbyOptionsManager.this.getSpeed(uuid);
                boolean fly = LobbyOptionsManager.this.getFly(uuid);
                LobbyOptionsReply optionsreply = new LobbyOptionsReply(uuid, visibility, speed, fly);
                reply.then(optionsreply);
            } catch (Exception ex) {
                reply.error(ex);
            }
        });
    }

    @Override
    public void setLobbyOptionsAsync(String uuid, boolean visibility, boolean speed, boolean fly) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try {
                LobbyOptionsManager.this.setVisibility(uuid, visibility);
                LobbyOptionsManager.this.setSpeed(uuid, speed);
                LobbyOptionsManager.this.setFly(uuid, fly);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    @Override
    public Boolean getVisibility(String uuid) throws Exception {
        String visibility;
        try {
            Statement statement = SQLManager.getConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM lobbyoptions WHERE uuid = '" + uuid + "';");
            if (res.next()) {
                visibility = res.getString("visibility");
            } else {
                visibility = "false";
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
            visibility = "false";
            throw new Exception(ex2.getMessage());
        }
        return Boolean.valueOf(visibility);
    }

    @Override
    public Boolean getSpeed(String uuid) throws Exception {
        String speed;
        try {
            Statement statement = SQLManager.getConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM lobbyoptions WHERE uuid = '" + uuid + "';");
            if (res.next()) {
                speed = res.getString("speed");
            } else {
                speed = "false";
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
            speed = "false";
            throw new Exception(ex2.getMessage());
        }
        return Boolean.valueOf(speed);
    }

    @Override
    public Boolean getFly(String uuid) throws Exception {
        String fly;
        try {
            Statement statement = SQLManager.getConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM lobbyoptions WHERE uuid = '" + uuid + "';");
            if (res.next()) {
                fly = res.getString("fly");
            } else {
                fly = "false";
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
            fly = "false";
            throw new Exception(ex2);
        }
        return Boolean.valueOf(fly);
    }

    @Override
    public void setVisibility(String uuid, Boolean bool) {
        try {
            Statement statement = SQLManager.getConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM lobbyoptions WHERE uuid = '" + uuid + "';");
            res.next();
            PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE lobbyoptions SET visibility=? WHERE uuid=?");
            ps.clearParameters();
            ps.setString(2, uuid);
            ps.setString(1, bool + "");
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
    }

    @Override
    public void setVisibilityAsync(String uuid, Boolean bool, CallbackReply reply) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM lobbyoptions WHERE uuid = '" + uuid + "';");
                res.next();
                PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE lobbyoptions SET visibility=? WHERE uuid=?");
                ps.clearParameters();
                ps.setString(2, uuid);
                ps.setString(1, bool + "");
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
    public void setSpeed(String uuid, Boolean bool) {
        try {
            Statement statement = SQLManager.getConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM lobbyoptions WHERE uuid = '" + uuid + "';");
            res.next();
            PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE lobbyoptions SET speed=? WHERE uuid=?");
            ps.clearParameters();
            ps.setString(2, uuid);
            ps.setString(1, bool + "");
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
    }

    @Override
    public void setSpeedAsync(String uuid, Boolean bool, CallbackReply reply) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM lobbyoptions WHERE uuid = '" + uuid + "';");
                res.next();
                PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE lobbyoptions SET speed=? WHERE uuid=?");
                ps.clearParameters();
                ps.setString(2, uuid);
                ps.setString(1, bool + "");
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
    public void setFly(String uuid, Boolean bool) {
        try {
            Statement statement = SQLManager.getConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM lobbyoptions WHERE uuid = '" + uuid + "';");
            res.next();
            PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE lobbyoptions SET fly=? WHERE uuid=?");
            ps.clearParameters();
            ps.setString(2, uuid);
            ps.setString(1, bool + "");
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
    }

    @Override
    public void setFlyAsync(String uuid, Boolean bool, CallbackReply reply) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM lobbyoptions WHERE uuid = '" + uuid + "';");
                res.next();
                PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE lobbyoptions SET fly=? WHERE uuid=?");
                ps.clearParameters();
                ps.setString(2, uuid);
                ps.setString(1, bool + "");
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
}
