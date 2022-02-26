package com.chainz.core.namedata;

import com.chainz.core.Core;
import com.chainz.core.async.reply.NameReply;
import com.chainz.core.sql.SQLManager;
import com.chainz.core.utils.config.ConfigManager;
import com.google.common.net.HttpHeaders;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class NameDataManager implements NameData {
    @Override
    public NameReply getNameFromUUID(UUID uuid) {
        CompletableFuture<NameReply> cf = CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement statement = SQLManager.getConnection().prepareStatement("SELECT * FROM namedata WHERE uuid = '" + uuid.toString() + "';");
                ResultSet res = statement.executeQuery();
                NameReply reply;
                if (res.next()) {
                    reply = new NameReply(uuid, res.getString("name"));
                } else {
                    reply = new NameReply(uuid, "Unknown");
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
                return reply;
            } catch (Exception ex2) {
                ex2.printStackTrace();
                return null;
            }
        });
        try {
            return cf.get();
        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
            return new NameReply(uuid, "Unknown");
        }
    }

    private UUID fromTrimmed(String trimmedUUID) throws IllegalArgumentException {
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
        return UUID.fromString(builder.toString());
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
    public NameReply getUUIDFromAllMethods(String name) {
        CompletableFuture<NameReply> cf = CompletableFuture.supplyAsync(() -> {
            try (Jedis j = Core.pool.getResource()) {
                if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                    j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                }
                j.select(2);
                NameReply nameReply;
                if (j.exists("fakenick:" + name)) {
                    nameReply = new NameReply(UUID.fromString(j.get("fakenick:" + name)), name);
                } else {
                    HttpURLConnection connection = getConnection("https://api.ashcon.app/mojang/v2/uuid/" + name);
                    int responseCode = connection.getResponseCode();

                    if (responseCode == 200) {
                        nameReply = new NameReply(NameDataManager.this.fromTrimmed(getJson(connection).getAsJsonPrimitive().getAsString()), name);
                    } else {
                        try {
                            PreparedStatement statement = SQLManager.getConnection().prepareStatement("SELECT * FROM namedata WHERE name = '" + name + "';");
                            ResultSet res = statement.executeQuery();
                            if (res.next()) {
                                nameReply = new NameReply(UUID.fromString(res.getString("uuid")), res.getString("name"));
                            } else {
                                return null;
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
                            return null;
                        }
                    }
                }

                return nameReply;
            } catch (IOException e) {
                e.printStackTrace();
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
    public NameReply getRealUUIDFromFakeNick(final String fakename) {
        CompletableFuture<NameReply> cf = CompletableFuture.supplyAsync(() -> {
            try {
                try (Jedis j = Core.pool.getResource()) {
                    if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                        j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                    }
                    j.select(2);
                    NameReply nameReply;
                    if (j.exists("fakenick:" + fakename)) {
                        return new NameReply(UUID.fromString(j.get("fakenick:" + fakename)), fakename);
                    } else {
                        return null;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
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
    public boolean existsName(final String name) {
        CompletableFuture<Boolean> cf = CompletableFuture.supplyAsync(() -> {
            try {
                final Statement statement = SQLManager.getConnection().createStatement();
                final ResultSet res = statement.executeQuery("SELECT * FROM namedata WHERE name = '" + name + "';");
                boolean result;
                result = res.next();

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
                return result;
            } catch (Exception ex2) {
                ex2.printStackTrace();
                return true;
            }
        });
        try {
            return cf.get();
        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
            return true;
        }
    }
}
