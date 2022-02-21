package com.chainz.core.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JsonUtils {
    private static String readAll(final Reader rd) throws IOException {
        final StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JsonElement getJsonResponse(String url) {
        JsonElement rootobj = null;
        try {
            final InputStream in = new URL(url).openStream();
            final BufferedReader rd = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            final String jsonString = readAll(rd);
            if (!jsonString.isEmpty()) {
                final JsonParser jsonParser = new JsonParser();
                rootobj = jsonParser.parse(jsonString);
                in.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException ignored) {
        }
        return rootobj;
    }

}
