package com.chainz.core.playerlevel.commands;

import com.chainz.core.ChainZAPI;
import com.chainz.core.utils.MessageLoad;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.UUID;

import static com.chainz.core.Core.col;

public class PlayerLevelCommands implements Listener, CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("setlevel")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!p.hasPermission("chainz.levels.*")) {
                    sender.sendMessage(col(MessageLoad.getMessage("NO_PERMS")));
                } else if (args.length < 2) {
                    sender.sendMessage("§c¡Debes incluir un jugador y un nivel!");
                } else if (args.length == 2) {
                    String playerName = args[0];
                    String uuid;
                    if (Bukkit.getPlayer(playerName) != null) {
                        uuid = Bukkit.getPlayer(playerName).getUniqueId().toString();
                    } else {
                        uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId().toString();
                    }
                    int level = Integer.parseInt(args[1]);
                    ChainZAPI.getLevelSystem().setLevel(UUID.fromString(uuid), level);
                    sender.sendMessage("§eEstablecido nivel §b" + level + " §eal jugador §b" + uuid);
                }
            } else if (args.length < 2) {
                sender.sendMessage("§c¡Debes incluir un jugador y un nivel!");
            } else if (args.length == 2) {
                String playerName2 = args[0];
                String uuid2;
                if (Bukkit.getPlayer(playerName2) != null) {
                    uuid2 = Bukkit.getPlayer(playerName2).getUniqueId().toString();
                } else {
                    uuid2 = Bukkit.getOfflinePlayer(playerName2).getUniqueId().toString();
                }
                int level2 = Integer.parseInt(args[1]);
                ChainZAPI.getLevelSystem().setLevel(UUID.fromString(uuid2), level2);
                sender.sendMessage("§eEstablecido nivel §b" + level2 + " §eal jugador §b" + uuid2);
            }
        }
        if (label.equalsIgnoreCase("addxp")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!p.hasPermission("chainz.levels.*")) {
                    sender.sendMessage(col(MessageLoad.getMessage("NO_PERMS")));
                } else if (args.length < 2) {
                    sender.sendMessage("§c¡Debes incluir un jugador y una cantidad!");
                } else if (args.length == 2) {
                    String playerName = args[0];
                    String uuid;
                    if (Bukkit.getPlayer(playerName) != null) {
                        uuid = Bukkit.getPlayer(playerName).getUniqueId().toString();
                    } else {
                        uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId().toString();
                    }
                    int add = Integer.parseInt(args[1]);
                    ChainZAPI.getLevelSystem().addExperience(UUID.fromString(uuid), add);
                    sender.sendMessage("§eAñadido §b" + add + " §epuntos al jugador §b" + uuid);
                }
            } else if (args.length < 2) {
                sender.sendMessage("§c¡Debes incluir un jugador y un nivel!");
            } else if (args.length == 2) {
                String playerName2 = args[0];
                String uuid2;
                if (Bukkit.getPlayer(playerName2) != null) {
                    uuid2 = Bukkit.getPlayer(playerName2).getUniqueId().toString();
                } else {
                    uuid2 = Bukkit.getOfflinePlayer(playerName2).getUniqueId().toString();
                }
                int add2 = Integer.valueOf(args[1]);
                ChainZAPI.getLevelSystem().addExperience(UUID.fromString(uuid2), add2);
                sender.sendMessage("§eAñadido §b" + add2 + " §epuntos al jugador §b" + uuid2);
            }
        }
        return true;
    }
}
