package es.eltrueno.npc.skin;

import com.mojang.authlib.GameProfile;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
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

    public SkinData getSkinData() {
        if (type == SkinType.IDENTIFIER) {
            return SkinManager.getSkinFromMojangAsync(this.identifier);
        }
        return null;
    }

    public SkinData getSkinData(OfflinePlayer p) {
        if (type == SkinType.PLAYER) {
            GameProfile profile = ((CraftPlayer) p).getHandle().getProfile();
            if (profile != null && p.isOnline()) {
                return SkinManager.extractFromProfile(profile);
            } else {
                return SkinManager.getSkinFromMojangAsync(p.getUniqueId().toString());
            }
        }
        return null;
    }
}
