package com.chainz.core.listeners;

import com.chainz.core.utils.MessageLoad;
import com.chainz.core.utils.config.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;

import static com.chainz.core.Core.col;

public class CommandBlock implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommandPreProcess(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        final String[] msg = event.getMessage().split(" ");
        final List<String> plugins = new ArrayList<>();
        plugins.add("bukkit:pl");
        plugins.add("bukkit:plugins");
        plugins.add("minecraft:plugins");
        plugins.add("minecraft:pl");
        plugins.add("plugins");
        plugins.add("pl");
        final List<String> version = new ArrayList<>();
        version.add("icanhasbukkit");
        version.add("bukkit:icanhasbukkit");
        version.add("version");
        version.add("ver");
        version.add("bukkit:ver");
        version.add("minecraft:ver");
        version.add("bukkit:about");
        version.add("minecraft:about");
        version.add("about");
        version.add("bukkit:version");
        version.add("minecraft:version");
        final List<String> help = new ArrayList<>();
        help.add("?");
        help.add("help");
        help.add("bukkit:help");
        help.add("minecraft:help");
        help.add("bukkit:?");
        help.add("minecraft:?");
        final List<String> blocked = new ArrayList<>();
        blocked.add("me");
        blocked.add("minecraft:me");
        blocked.addAll(ConfigManager.get("config.yml").getStringList("BlockCommands"));
        if (!player.hasPermission("chainz.staff")) {
            for (final String Loop : plugins) {
                if (msg[0].equalsIgnoreCase("/" + Loop)) {
                    for (String line : MessageLoad.getLargeMessage("PLUGIN_BLOCK_LARGE")) {
                        player.sendMessage(col(line));
                    }
                    event.setCancelled(true);
                }
            }
            for (final String Loop : version) {
                if (msg[0].equalsIgnoreCase("/" + Loop)) {
                    player.sendMessage(col(MessageLoad.getMessage("VERSION_BLOCK")));
                    event.setCancelled(true);
                }
            }
            for (final String Loop : help) {
                if (msg[0].equalsIgnoreCase("/" + Loop)) {
                    for (String line : MessageLoad.getLargeMessage("HELP_BLOCK_LARGE")) {
                        player.sendMessage(col(line));
                    }
                    event.setCancelled(true);
                }
            }
            for (final String Loop : blocked) {
                if (msg[0].equalsIgnoreCase("/" + Loop)) {
                    player.sendMessage(col(MessageLoad.getMessage("BLOCK_COMMAND")));
                    event.setCancelled(true);
                }
            }
        }
        if (msg[0].startsWith("/op")) {
            event.setCancelled(true);
            player.sendMessage(col(MessageLoad.getMessage("BLOCK_COMMAND")));
        }
    }
}
