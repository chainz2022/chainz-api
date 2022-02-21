package com.chainz.core.playersettings;

import com.chainz.core.Core;
import com.chainz.core.async.reply.CallbackReply;
import com.chainz.core.async.reply.PlayerSettingsReply;
import com.chainz.core.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PlayerSettingsManager implements PlayerSettings {
    public static void setDefaults(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            String uuid = p.getUniqueId().toString();
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM playersettings WHERE uuid = '" + uuid + "';");
                if (!res.next()) {
                    statement.executeUpdate("INSERT INTO playersettings (`uuid`, `friendrequests`, `chat`, `ads`) VALUES ('" + uuid + "', 'true', 'true', 'true');");
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
    public void setCanFriendRequests(String uuid, boolean bol, CallbackReply callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM playersettings WHERE uuid = '" + uuid + "';");
                res.next();
                PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE playersettings SET friendrequests=? WHERE uuid=?");
                ps.clearParameters();
                ps.setString(2, uuid);
                ps.setString(1, bol + "");
                ps.executeUpdate();
                callback.then(null);
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
                callback.error(ex2);
            }
        });
    }

    @Override
    public void setChatVisibility(String uuid, boolean bol, CallbackReply callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM playersettings WHERE uuid = '" + uuid + "';");
                res.next();
                PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE playersettings SET chat=? WHERE uuid=?");
                ps.clearParameters();
                ps.setString(2, uuid);
                ps.setString(1, bol + "");
                ps.executeUpdate();
                callback.then(null);
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
                callback.error(ex2);
            }
        });
    }

    @Override
    public void setAdVisibility(String uuid, boolean bol, CallbackReply callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM playersettings WHERE uuid = '" + uuid + "';");
                res.next();
                PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE playersettings SET ads=? WHERE uuid=?");
                ps.clearParameters();
                ps.setString(2, uuid);
                ps.setString(1, bol + "");
                ps.executeUpdate();
                callback.then(null);
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
                callback.error(ex2);
            }
        });
    }

    @Override
    public void setCanPartyRequests(String uuid, boolean bol, CallbackReply callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, new Runnable() {
            @Override
            public void run() {
                try {
                    Statement statement = SQLManager.getConnection().createStatement();
                    ResultSet res = statement.executeQuery("SELECT * FROM playersettings WHERE uuid = '" + uuid + "';");
                    res.next();
                    PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE playersettings SET partyrequests=? WHERE uuid=?");
                    ps.clearParameters();
                    ps.setString(2, uuid);
                    ps.setString(1, bol + "");
                    ps.executeUpdate();
                    callback.then(null);
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
                    callback.error(ex2);
                }
            }
        });
    }

    @Override
    public void setCanMsg(String uuid, boolean bol, CallbackReply callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, new Runnable() {
            @Override
            public void run() {
                try {
                    Statement statement = SQLManager.getConnection().createStatement();
                    ResultSet res = statement.executeQuery("SELECT * FROM playersettings WHERE uuid = '" + uuid + "';");
                    res.next();
                    PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE playersettings SET msg=? WHERE uuid=?");
                    ps.clearParameters();
                    ps.setString(2, uuid);
                    ps.setString(1, bol + "");
                    ps.executeUpdate();
                    callback.then(null);
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
                    callback.error(ex2);
                }
            }
        });
    }

    @Override
    public void setPersonalSettingsAsync(String uuid, boolean friendrequests, boolean partyrequests, boolean msg, boolean chat, boolean ads, CallbackReply callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM playersettings WHERE uuid = '" + uuid + "';");
                res.next();
                PreparedStatement ps = SQLManager.getConnection().prepareStatement("UPDATE playersettings SET friendrequests=?, partyrequests=?, msg=?, chat=?, ads=? WHERE uuid=?");
                ps.clearParameters();
                ps.setString(6, uuid);
                ps.setString(1, friendrequests + "");
                ps.setString(2, partyrequests + "");
                ps.setString(3, msg + "");
                ps.setString(4, chat + "");
                ps.setString(5, ads + "");
                ps.executeUpdate();
                callback.then(null);
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
                callback.error(ex2);
            }
        });
    }

    @Override
    public void getPersonalSettingsAsync(String uuid, CallbackReply callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM playersettings WHERE uuid = '" + uuid + "';");
                if (res.next()) {
                    boolean friend = Boolean.parseBoolean(res.getString("friendrequests"));
                    boolean party = Boolean.parseBoolean(res.getString("partyrequests"));
                    boolean msg = Boolean.parseBoolean(res.getString("msg"));
                    boolean chat = Boolean.parseBoolean(res.getString("chat"));
                    boolean ads = Boolean.parseBoolean(res.getString("ads"));
                    PlayerSettingsReply reply = new PlayerSettingsReply(uuid, friend, party, msg, chat, ads);
                    callback.then(reply);
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
                callback.error(ex2);
            }
        });
    }

    @Override
    public boolean getCanFriendRequests(String uuid) throws Exception {
        String friendrequests;
        try {
            Statement statement = SQLManager.getConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM playersettings WHERE uuid = '" + uuid + "';");
            if (res.next()) {
                friendrequests = res.getString("friendrequests");
            } else {
                friendrequests = "false";
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
            friendrequests = "false";
            throw new Exception(ex2.getMessage());
        }
        return Boolean.parseBoolean(friendrequests);
    }

    @Override
    public boolean getCanPartyRequests(String uuid) throws Exception {
        String partyrequests;
        try {
            Statement statement = SQLManager.getConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM playersettings WHERE uuid = '" + uuid + "';");
            if (res.next()) {
                partyrequests = res.getString("partyrequests");
            } else {
                partyrequests = "false";
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
            partyrequests = "false";
            throw new Exception(ex2.getMessage());
        }
        return Boolean.valueOf(partyrequests);
    }

    @Override
    public boolean getCanMsg(String uuid) throws Exception {
        String msg;
        try {
            Statement statement = SQLManager.getConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM playersettings WHERE uuid = '" + uuid + "';");
            if (res.next()) {
                msg = res.getString("msg");
            } else {
                msg = "false";
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
            msg = "false";
            throw new Exception(ex2.getMessage());
        }
        return Boolean.valueOf(msg);
    }

    @Override
    public boolean getChatVisibility(String uuid) throws Exception {
        String chat;
        try {
            Statement statement = SQLManager.getConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM playersettings WHERE uuid = '" + uuid + "';");
            if (res.next()) {
                chat = res.getString("chat");
            } else {
                chat = "false";
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
            chat = "false";
            throw new Exception(ex2.getMessage());
        }
        return Boolean.valueOf(chat);
    }

    @Override
    public boolean getAdVisibility(String uuid) throws Exception {
        String ads;
        try {
            Statement statement = SQLManager.getConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM playersettings WHERE uuid = '" + uuid + "';");
            if (res.next()) {
                ads = res.getString("ads");
            } else {
                ads = "false";
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
            ads = "false";
            throw new Exception(ex2.getMessage());
        }
        return Boolean.valueOf(ads);
    }
}
