package com.chainz.core.economy.commands;

import com.chainz.core.ChainZAPI;
import com.chainz.core.async.reply.CallbackReply;
import com.chainz.core.async.reply.CoinsReply;
import com.chainz.core.async.reply.Reply;
import com.chainz.core.utils.MessageLoad;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import static com.chainz.core.Core.col;

public class EconomyCommands implements Listener, CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("addcoins")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!p.hasPermission("chainz.economy.*")) {
                    sender.sendMessage(col(MessageLoad.getMessage("NO_PERMS")));
                } else if (args.length == 2) {
                    String playerName = args[0];
                    String uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId().toString();
                    double add = Double.parseDouble(args[1]);
                    ChainZAPI.getEconomyManager().addCoinsAsync(uuid, add, false, new CallbackReply() {
                        @Override
                        public void error(Exception ex) {
                            ex.printStackTrace();
                            sender.sendMessage(col(MessageLoad.getMessage("DO_NOT_EXIST")));
                        }

                        @Override
                        public void then(Reply reply) {
                            sender.sendMessage(col(MessageLoad.getMessage("COINS_ADDED").replaceAll("<amount>", "" + add).replaceAll("<player>", playerName)));
                        }
                    });
                }
            } else if (args.length < 2) {
                sender.sendMessage(col(MessageLoad.getMessage("BAD_USAGE")));
            } else if (args.length == 2) {
                String playerName = args[0];
                String uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId().toString();
                double add = Double.parseDouble(args[1]);
                ChainZAPI.getEconomyManager().addCoinsAsync(uuid, add, false, new CallbackReply() {
                    @Override
                    public void error(Exception ex) {
                        sender.sendMessage(col(MessageLoad.getMessage("DO_NOT_EXIST")));
                    }

                    @Override
                    public void then(Reply reply) {
                        sender.sendMessage(col(MessageLoad.getMessage("COINS_ADDED").replaceAll("<amount>", "" + add).replaceAll("<player>", playerName)));
                    }
                });
            }
        }
        if (label.equalsIgnoreCase("removecoins")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!p.hasPermission("chainz.economy.*")) {
                    sender.sendMessage(col(MessageLoad.getMessage("NO_PERMS")));
                } else if (args.length < 2) {
                    sender.sendMessage(col(MessageLoad.getMessage("BAD_USAGE")));
                } else if (args.length == 2) {
                    String playerName = args[0];
                    String uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId().toString();
                    double add = Double.parseDouble(args[1]);
                    ChainZAPI.getEconomyManager().getCoinsAsync(uuid, new CallbackReply() {
                        @Override
                        public void error(Exception ex) {
                            sender.sendMessage(col(MessageLoad.getMessage("DO_NOT_EXIST")));
                        }

                        @Override
                        public void then(Reply reply) {
                            CoinsReply coinsReply = (CoinsReply) reply;
                            if (coinsReply.getCoins() >= add) {
                                ChainZAPI.getEconomyManager().removeCoinsAsync(uuid, add, new CallbackReply() {
                                    @Override
                                    public void error(Exception ex) {
                                        ex.printStackTrace();
                                        sender.sendMessage(col(MessageLoad.getMessage("DO_NOT_EXIST")));
                                    }

                                    @Override
                                    public void then(Reply reply) {
                                        sender.sendMessage(col(MessageLoad.getMessage("COINS_REMOVED").replaceAll("<amount>", "" + add).replaceAll("<player>", playerName)));
                                    }
                                });
                            } else {
                                sender.sendMessage(col(MessageLoad.getMessage("NOT_COINS_TO_REMOVE")));
                            }
                        }
                    });
                }

            } else if (args.length < 2) {
                sender.sendMessage(col(MessageLoad.getMessage("BAD_USAGE")));
            } else if (args.length == 2) {
                String playerName = args[0];
                String uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId().toString();
                double add = Double.parseDouble(args[1]);
                ChainZAPI.getEconomyManager().getCoinsAsync(uuid, new CallbackReply() {
                    @Override
                    public void error(Exception ex) {
                        sender.sendMessage(col(MessageLoad.getMessage("DO_NOT_EXIST")));
                    }

                    @Override
                    public void then(Reply reply) {
                        CoinsReply coinsReply = (CoinsReply) reply;
                        if (coinsReply.getCoins() >= add) {
                            ChainZAPI.getEconomyManager().addCoinsAsync(uuid, add, false, new CallbackReply() {
                                @Override
                                public void error(Exception ex) {
                                    ex.printStackTrace();
                                    sender.sendMessage(col(MessageLoad.getMessage("DO_NOT_EXIST")));
                                }

                                @Override
                                public void then(Reply reply) {
                                    sender.sendMessage(col(MessageLoad.getMessage("COINS_REMOVED").replaceAll("<amount>", "" + add).replaceAll("<player>", playerName)));
                                }
                            });
                        } else {
                            sender.sendMessage(col(MessageLoad.getMessage("NOT_COINS_TO_REMOVE")));
                        }
                    }
                });
            }
        }
        if (label.equalsIgnoreCase("setplayermultiplier")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!p.hasPermission("chainz.economy.*")) {
                    sender.sendMessage(col(MessageLoad.getMessage("NO_PERMS")));
                } else if (args.length < 2) {
                    sender.sendMessage(col(MessageLoad.getMessage("BAD_USAGE")));
                } else if (args.length == 2) {
                    String playerName2 = args[0];
                    String uuid2 = Bukkit.getOfflinePlayer(playerName2).getUniqueId().toString();
                    double mult = Double.parseDouble(args[1]);
                    if (ChainZAPI.getEconomyManager().playerExists(uuid2)) {
                        ChainZAPI.getEconomyManager().setPlayerMultiplier(uuid2, mult);
                        sender.sendMessage(col(MessageLoad.getMessage("MULTIPLIER_SET").replaceAll("<amount>", "" + mult).replaceAll("<player>", playerName2)));
                    } else {
                        sender.sendMessage(col(MessageLoad.getMessage("DO_NOT_EXIST")));
                    }
                }
            } else if (args.length < 2) {
                sender.sendMessage(col(MessageLoad.getMessage("BAD_USAGE")));
            } else if (args.length == 2) {
                String playerName = args[0];
                String uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId().toString();
                double mult2 = Double.parseDouble(args[1]);
                if (ChainZAPI.getEconomyManager().playerExists(uuid)) {
                    ChainZAPI.getEconomyManager().setPlayerMultiplier(uuid, mult2);
                    sender.sendMessage(col(MessageLoad.getMessage("MULTIPLIER_SET").replaceAll("<amount>", "" + mult2).replaceAll("<player>", playerName)));
                } else {
                    sender.sendMessage(col(MessageLoad.getMessage("DO_NOT_EXIST")));
                }
            }
        }
        return true;
    }
}
