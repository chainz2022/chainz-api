package com.chainz.core.playerlevel.commands;

import com.chainz.core.ChainZAPI;
import com.chainz.core.async.reply.CallbackReply;
import com.chainz.core.async.reply.ExpReply;
import com.chainz.core.async.reply.LevelReply;
import com.chainz.core.async.reply.Reply;
import com.chainz.core.utils.MessageLoad;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

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
                    ChainZAPI.getLevelSystem().setLevelAsync(uuid, level, new CallbackReply() {
                        @Override
                        public void error(Exception ex) {
                            ex.printStackTrace();
                            sender.sendMessage("§cHa ocurrido un error");
                        }

                        @Override
                        public void then(Reply reply) {
                            LevelReply lvlreply = (LevelReply) reply;
                            sender.sendMessage("§eEstablecido nivel §b" + level + " §eal jugador §b" + uuid);
                        }
                    });
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
                ChainZAPI.getLevelSystem().setLevelAsync(uuid2, level2, new CallbackReply() {
                    @Override
                    public void error(Exception ex) {
                        ex.printStackTrace();
                        sender.sendMessage("§cHa ocurrido un error");
                    }

                    @Override
                    public void then(Reply reply) {
                        LevelReply lvlreply = (LevelReply) reply;
                        sender.sendMessage("§eEstablecido nivel §b" + level2 + " §eal jugador §b" + uuid2);
                    }
                });
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
                    ChainZAPI.getLevelSystem().addExperienceAsync(uuid, add, new CallbackReply() {
                        @Override
                        public void error(Exception ex) {
                            ex.printStackTrace();
                            sender.sendMessage("§eBauxiteMC§8| §cHa ocurrido un error");
                        }

                        @Override
                        public void then(Reply reply) {
                            ExpReply expreply = (ExpReply) reply;
                            sender.sendMessage("§eA\u00f1adido §b" + add + " §epuntos al jugador §b" + uuid);
                        }
                    });
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
                ChainZAPI.getLevelSystem().addExperienceAsync(uuid2, add2, new CallbackReply() {
                    @Override
                    public void error(Exception ex) {
                        ex.printStackTrace();
                        sender.sendMessage("§cHa ocurrido un error");
                    }

                    @Override
                    public void then(Reply reply) {
                        ExpReply expreply = (ExpReply) reply;
                        sender.sendMessage("§eA\u00f1adido §b" + add2 + " §epuntos al jugador §b" + uuid2);
                    }
                });
            }
        }
        return true;
    }
}
