package com.chainz.core.listeners;

import com.chainz.core.Core;
import com.chainz.core.chat.ChatManager;
import com.chainz.core.utils.config.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

import static com.chainz.core.Core.col;

public class ChatListener implements Listener {
    private static final HashMap<Player, BukkitRunnable> cooldown = new HashMap<Player, BukkitRunnable>();
    private static final HashMap<Player, String> lastMessage = new HashMap<Player, String>();

    @EventHandler
    public void onChat(final AsyncPlayerChatEvent ev) {
        final Player p = ev.getPlayer();
        if (!ev.getMessage().startsWith("/") && ConfigManager.get("config.yml").getBoolean("CustomChat.enable")) {
            if (p.hasPermission("chainz.staff")) {
                ev.setCancelled(true);
                ChatManager.sendChatMessage(p, ev.getMessage());
            } else if (ChatListener.lastMessage.containsKey(p) && ChatListener.lastMessage.get(p) != null) {
                final String lastmsg = ChatListener.lastMessage.get(p);
                if (!lastmsg.equalsIgnoreCase(ev.getMessage())) {
                    if (ChatListener.cooldown.containsKey(p) && ChatListener.cooldown.get(p) != null) {
                        p.sendMessage(col("&cPlease wait a few moments before sending another message!"));
                        ev.setCancelled(true);
                    } else {
                        ev.setCancelled(true);
                        ChatManager.sendChatMessage(p, ev.getMessage());
                        if (ChatListener.lastMessage.containsKey(p)) {
                            ChatListener.lastMessage.replace(p, ev.getMessage());
                        } else {
                            ChatListener.lastMessage.put(p, ev.getMessage());
                        }
                        ChatListener.cooldown.put(p, new BukkitRunnable() {
                            public void run() {
                                ChatListener.cooldown.remove(p);
                                this.cancel();
                            }
                        });
                        ChatListener.cooldown.get(p).runTaskLater(Core.core, 60L);
                    }
                } else {
                    p.sendMessage(col("&cYou cannot say the same message twice!"));
                }
                ev.setCancelled(true);
            } else if (ChatListener.cooldown.containsKey(p) && ChatListener.cooldown.get(p) != null) {
                p.sendMessage(col("&cPlease wait a few moments before sending another message!"));
                ev.setCancelled(true);
            } else {
                ev.setCancelled(true);
                ChatManager.sendChatMessage(p, ev.getMessage());
                if (ChatListener.lastMessage.containsKey(p)) {
                    ChatListener.lastMessage.replace(p, ev.getMessage());
                } else {
                    ChatListener.lastMessage.put(p, ev.getMessage());
                }
                ChatListener.cooldown.put(p, new BukkitRunnable() {
                    public void run() {
                        ChatListener.cooldown.remove(p);
                        this.cancel();
                    }
                });
                ChatListener.cooldown.get(p).runTaskLater(Core.core, 60L);
            }
        }
    }

}
