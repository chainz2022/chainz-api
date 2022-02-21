package com.chainz.core.utils;

import com.chainz.core.utils.config.ConfigManager;

import java.util.List;

public class MessageLoad {
    public static String getMessage(String path) {
        return ConfigManager.get("messages.yml").getString(path).replaceAll("&", "§");
    }

    public static List<String> getLargeMessage(String path) {
        return ConfigManager.get("messages.yml").getStringList(path);
    }
}
