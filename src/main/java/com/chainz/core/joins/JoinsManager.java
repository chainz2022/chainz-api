package com.chainz.core.joins;

import com.chainz.core.Core;
import com.chainz.core.async.reply.CallbackReply;
import com.chainz.core.async.reply.JoinsReply;
import com.chainz.core.sql.SQLManager;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JoinsManager implements Joins {
    @Override
    public void getJoinData(String uuid, CallbackReply callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try {
                Statement statement = SQLManager.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM joins WHERE uuid = '" + uuid + "';");
                if (res.next()) {
                    JoinsReply reply = new JoinsReply(uuid, res.getTimestamp("firstjoin"), res.getTimestamp("lastjoin"), res.getLong("timeplayed"));
                    callback.then(reply);
                } else {
                    callback.error(new Exception("User not found in database"));
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
}
