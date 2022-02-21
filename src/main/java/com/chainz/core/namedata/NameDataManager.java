package com.chainz.core.namedata;

import com.chainz.core.Core;
import com.chainz.core.async.reply.CallbackReply;
import com.chainz.core.async.reply.NameReply;
import com.chainz.core.sql.SQLManager;
import com.chainz.core.utils.config.ConfigManager;
import com.google.common.net.HttpHeaders;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class NameDataManager implements NameData {
    @Override
    public void getNameFromUuidAsync(final String uuid, final CallbackReply callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try {
                PreparedStatement statement = SQLManager.getConnection().prepareStatement("SELECT * FROM namedata WHERE uuid = '" + uuid + "';");
                ResultSet res = statement.executeQuery();
                if (res.next()) {
                    final NameReply reply = new NameReply(uuid, res.getString("name"));
                    callback.then(reply);
                } else {
                    final NameReply reply = new NameReply(uuid, "Desconocido");
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

    private String fromTrimmed(final String trimmedUUID) throws IllegalArgumentException {
        if (trimmedUUID == null) {
            throw new IllegalArgumentException();
        }
        final StringBuilder builder = new StringBuilder(trimmedUUID.trim());
        try {
            builder.insert(20, "-");
            builder.insert(16, "-");
            builder.insert(12, "-");
            builder.insert(8, "-");
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException();
        }
        return builder.toString();
    }

    private HttpURLConnection getConnection(String url) throws IOException {
        HttpURLConnection httpConnection = (HttpURLConnection) new URL(url).openConnection();

        httpConnection.setConnectTimeout(6000);
        httpConnection.setReadTimeout(2 * 6000);

        httpConnection.setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/json");
        httpConnection.setRequestProperty(HttpHeaders.USER_AGENT, "SimpleSkins-velocity-plugin");
        return httpConnection;
    }

    private JsonElement getJson(HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            return new JsonParser().parse(reader);
        }
    }

    @Override
    public void getUUIDFromAllMethods(String name, final CallbackReply callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try {
                try (Jedis j = Core.pool.getResource()) {
                    if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                        j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                    }
                    j.select(2);
                    if (j.exists("fakenick:" + name)) {
                        final NameReply nameReply = new NameReply(j.get("fakenick:" + name), name);
                        callback.then(nameReply);
                    } else {
                        HttpURLConnection connection = getConnection("https://api.ashcon.app/mojang/v2/uuid/" + name);
                        int responseCode = connection.getResponseCode();

                        if (responseCode == 200) {
                            NameReply nameReply2 = new NameReply(NameDataManager.this.fromTrimmed(getJson(connection).getAsJsonPrimitive().getAsString()), name);
                            callback.then(nameReply2);
                        } else {
                            try {
                                PreparedStatement statement = SQLManager.getConnection().prepareStatement("SELECT * FROM namedata WHERE name = '" + name + "';");
                                ResultSet res = statement.executeQuery();
                                if (res.next()) {
                                    final NameReply reply = new NameReply(res.getString("uuid"), res.getString("name"));
                                    callback.then(reply);
                                } else {
                                    callback.error(new Exception("User not found"));
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
                        }
                    }
                }
            } catch (Exception ex3) {
                callback.error(ex3);
            }
        });
    }

    @Override
    public void getRealUUIDFromFakeNick(final String fakename, final CallbackReply callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try {
                try (Jedis j = Core.pool.getResource()) {
                    if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                        j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                    }
                    j.select(2);
                    if (j.exists("fakenick:" + fakename)) {
                        final NameReply nameReply = new NameReply(j.get("fakenick:" + fakename), fakename);
                        callback.then(nameReply);
                    } else {
                        callback.error(new Exception("Not found"));
                    }
                }
            } catch (Exception ex) {
                callback.error(ex);
            }
        });
    }

    @Override
    public void existsName(final String name, final CallbackReply callback) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
            try {
                final Statement statement = SQLManager.getConnection().createStatement();
                final ResultSet res = statement.executeQuery("SELECT * FROM namedata WHERE name = '" + name + "';");
                if (res.next()) {
                    callback.then(null);
                } else {
                    callback.error(new Exception("Not found"));
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
