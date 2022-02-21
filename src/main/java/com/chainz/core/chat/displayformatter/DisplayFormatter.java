package com.chainz.core.chat.displayformatter;

import com.chainz.core.Core;
import com.chainz.core.luckperms.LuckpermsInformationProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DisplayFormatter {
    private LuckpermsInformationProvider displayInformationProvider;

    public DisplayFormatter() {
        loadDisplayInformation();
    }

    private void loadDisplayInformation() {
        if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null)
            this.displayInformationProvider = new LuckpermsInformationProvider();
    }

    public LuckpermsInformationProvider getDisplayInformationProvider() {
        return this.displayInformationProvider;
    }

    public String prefix(Player player) {
        UUID uuid = player.getUniqueId();
        return Core.col(getDisplayInformationProvider().getPrefix(uuid).orElse(""));
    }

    public void update(Player player) {
        UUID uuid = player.getUniqueId();

        String prefix = Core.col(getDisplayInformationProvider().getPrefix(uuid).orElse(""));
        String suffix = Core.col(getDisplayInformationProvider().getSuffix(uuid).orElse(""));
    }
}
