package com.chainz.core.chat;

import com.chainz.core.ChainZAPI;
import com.chainz.core.utils.JsonMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static com.chainz.core.Core.col;

public class ChatManager {
    public static void sendChatMessage(Player p, String text) {
        JsonMessage msg = new JsonMessage();
        if (Bukkit.getPluginManager().getPlugin("ChainZLobby") != null) {
            msg.append(col(ChainZAPI.getDisplayFormatter().prefix(p) + ChatColor.RESET + "&e" + p.getDisplayName() + "&f "))
                    .setHoverAsTooltip("Click to open\n" + p.getName() + "'s profile").setClickAsExecuteCmd("/pprofile " + p.getName()).save();
        } else {
            msg.append(col(ChainZAPI.getDisplayFormatter().prefix(p) + ChatColor.RESET + "&e" + p.getDisplayName() + "&f ")).save();
        }

        msg.append(text).save();

        for (Player plys : Bukkit.getOnlinePlayers()) {
            msg.send(plys);
            /*if (ChainZPlayer.getChainZPlayer(plys.getUniqueId()).getPersonalSettings().getChatVisibility()) {
                msg.send(plys);
            }*/
        }
    }
}
