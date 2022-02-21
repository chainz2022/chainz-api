package es.eltrueno.npc.skin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TruenoNPCSkin {

    private final SkinType type;
    private String identifier;
    private final Plugin plugin;

    public TruenoNPCSkin(Plugin plugin, SkinType type, String identifier) {
        this.type = type;
        this.identifier = identifier;
        this.plugin = plugin;
    }

    public TruenoNPCSkin(Plugin plugin, SkinType type) {
        this.type = type;
        this.plugin = plugin;
    }

    public SkinType getSkinType() {
        return type;
    }

    public void getSkinDataAsync(SkinDataReply skinreply) {
        if (type == SkinType.IDENTIFIER) {
            SkinManager.getSkinFromMojangAsync(plugin, this.identifier, skinData -> {
                if (skinData != null) {
                    skinreply.done(skinData);
                } else {
                    SkinManager.getSkinFromMineskinAsync(plugin, identifier, skinData1 -> {
                        skinreply.done(skinData1);
                    });
                }
            });
        }
    }

    public void getSkinDataAsync(SkinDataReply skinreply, Player p) {
        if (type == SkinType.PLAYER) {
            SkinManager.getSkinFromMojangAsync(plugin, p.getUniqueId().toString(), skinData -> {
                if (skinData != null) {
                    skinreply.done(skinData);
                } else {
                    SkinManager.getSkinFromMineskinAsync(plugin, p.getUniqueId().toString(), skinData1 -> {
                        skinreply.done(skinData1);
                    });
                }
            });
        }
    }
}
