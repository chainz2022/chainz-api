package com.chainz.core.playerskindata.commands;

import com.chainz.core.ChainZAPI;
import com.chainz.core.Core;
import com.chainz.core.async.reply.CallbackReply;
import com.chainz.core.async.reply.Reply;
import com.chainz.core.utils.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import redis.clients.jedis.Jedis;

import javax.naming.NameAlreadyBoundException;

import static com.chainz.core.Core.col;

public class PlayerSkinCommands implements Listener, CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (label.equalsIgnoreCase("nick")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length < 1) {
                    p.sendMessage(col("&cInvalid arguments. Usage: &8/&enick <name>"));
                } else {
                    if (p.hasPermission("chainz.nick")) {
                        if (args[0].equalsIgnoreCase("off")) {
                            Bukkit.getScheduler().runTaskAsynchronously(Core.core, () -> {
                                try {
                                    try (Jedis j = Core.pool.getResource()) {
                                        if (!ConfigManager.get("config.yml").getString("Redis.pass").isEmpty()) {
                                            j.auth(ConfigManager.get("config.yml").getString("Redis.pass"));
                                        }
                                        j.select(2);
                                        j.del("playerprofilechange:" + p.getUniqueId().toString());
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            });
                            p.sendMessage(col("&eOnce you change servers, you'll be able to get back your identity"));
                        } else {
                            if (args[0].length() > 16) {
                                p.sendMessage(col("&cThe name can't be more than 16 characters long"));
                                return true;
                            }
                            ChainZAPI.getPlayerSkinDataManager().changeNickname(p, args[0], new CallbackReply() {
                                @Override
                                public void error(final Exception ex) {
                                    if (ex instanceof NullPointerException) {
                                        p.sendMessage(col("&cAn unknown error has ocurred, please report this to a server admin"));
                                    } else if (ex instanceof NameAlreadyBoundException) {
                                        p.sendMessage(col("&cThat name is already in use, please try again"));
                                    }
                                }

                                @Override
                                public void then(final Reply reply) {
                                    p.sendMessage(col("&eYou've successfully changed your nick to &f" + args[0]));
                                    p.sendMessage(col("&eYou need to change servers/re-log to apply changes."));
                                    p.sendMessage(col("&eYour rank will show as &a&lVIP &ealthough your current rank and permissions will remain"));
                                    p.sendMessage(col("&eIf you stop seeing the world -> &fF3+A"));
                                    p.sendMessage(col("&eYou can recover your identity by re-logging or by using &8/&enick off"));
                                }
                            });
                        }
                    } else {
                        p.sendMessage(col("&cYou don't have the permission to execute this command!"));
                    }
                }
            } else {
                sender.sendMessage("&cThe command can only be executed by a player");
            }
        }
        return true;
    }
}
