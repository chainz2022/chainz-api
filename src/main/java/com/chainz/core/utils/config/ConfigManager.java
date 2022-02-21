package com.chainz.core.utils.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private static Plugin PLUGIN = (Plugin) JavaPlugin.getProvidingPlugin(ConfigManager.class);

    private static Map<String, FileConfiguration> configs = new HashMap<>();

    public static boolean isFileLoaded(String fileName) {
        return configs.containsKey(fileName);
    }

    public static void load(String fileName) {
        File file = new File(PLUGIN.getDataFolder(), fileName);
        if (!file.exists())
            try {
                PLUGIN.saveResource(fileName, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        if (!isFileLoaded(fileName))
            configs.put(fileName, YamlConfiguration.loadConfiguration(file));
    }

    public static FileConfiguration get(String fileName) {
        if (isFileLoaded(fileName))
            return configs.get(fileName);
        return null;
    }

    public static boolean update(String fileName, String path, Object value) {
        if (isFileLoaded(fileName) &&
                !((FileConfiguration) configs.get(fileName)).contains(path)) {
            ((FileConfiguration) configs.get(fileName)).set(path, value);
            return true;
        }
        return false;
    }

    public static void set(String fileName, String path, Object value) {
        if (isFileLoaded(fileName))
            ((FileConfiguration) configs.get(fileName)).set(path, value);
    }

    public static void remove(String fileName, String path) {
        if (isFileLoaded(fileName))
            ((FileConfiguration) configs.get(fileName)).set(path, null);
    }

    public static boolean contains(String fileName, String path) {
        if (isFileLoaded(fileName))
            return ((FileConfiguration) configs.get(fileName)).contains(path);
        return false;
    }

    public static void reload(String fileName) {
        File file = new File(PLUGIN.getDataFolder(), fileName);
        if (isFileLoaded(fileName))
            try {
                ((FileConfiguration) configs.get(fileName)).load(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public static void save(String fileName) {
        File file = new File(PLUGIN.getDataFolder(), fileName);
        if (isFileLoaded(fileName))
            try {
                ((FileConfiguration) configs.get(fileName)).save(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void addComment(String fileName, String path, String... comments) {
        if (isFileLoaded(fileName))
            for (String comment : comments) {
                if (!((FileConfiguration) configs.get(fileName)).contains(path))
                    ((FileConfiguration) configs.get(fileName)).set("_COMMENT_" + comments.length, " " + comment);
            }
    }
}