package com.chainz.core.vanish.commands;

import com.chainz.core.utils.config.ConfigManager;
import com.chainz.core.vanish.VanishManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.chainz.core.Core.col;

public class VanishCommands implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (label.equalsIgnoreCase("vanish")) {
            if (sender instanceof Player) {

                Player p = (Player) sender;
                if (p.hasPermission("chainz.vanish")) {
                    if (ConfigManager.get("config.yml").getBoolean("Enable_vanish")) {
                        if (!VanishManager.isVanished(p)) {
                            for (final Player plys : Bukkit.getOnlinePlayers()) {
                                if (plys.hasPermission("chainz.staff")) {
                                    plys.hidePlayer(p);
                                }
                            }
                            VanishManager.setVanished(p, true);
                            p.sendMessage(col("&aYou're now invisible"));
                        } else {
                            for (final Player plys : Bukkit.getOnlinePlayers()) {
                                plys.showPlayer(p);
                            }
                            VanishManager.setVanished(p, false);
                            p.sendMessage(col("&aYou're now visible again"));
                        }
                    }
                } else {
                    p.sendMessage(col("&fUnknown command. Type \"help\" for help."));
                }
            } else {
                Bukkit.getConsoleSender().sendMessage("&eLinvio&d[API] &8| &cSolo un jugador puede usar ese comando!");
            }
        }
        return true;
    }
}
