package es.eltrueno.protocol.protocollib;

import com.chainz.core.Core;
import es.eltrueno.hologram.TruenoHologram;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class TruenoHologram_ProtocolLib implements TruenoHologram {
    private Location location;

    private ArrayList<String> lines;

    private double linesdistance = 0.3D;

    private ArrayList<ArmorStand> armor_lines = new ArrayList<>();

    private ArrayList<EntityArmorStand> NmsArmorLines = new ArrayList<>();

    private Player player = null;

    public void setupWorldHologram(Location loc, ArrayList<String> lines) {
        this.location = loc.clone();
        ArrayList<String> finalLines = new ArrayList<>();
        for (String line : lines) {
            finalLines.add(Core.col(line));
        }
        this.lines = finalLines;
    }

    public void setupPlayerHologram(Player player, Location loc, ArrayList<String> lines) {
        this.player = player;
        this.location = loc.clone();

        ArrayList<String> finalLines = new ArrayList<>();
        for (String line : lines) {
            finalLines.add(Core.col(line));
        }
        this.lines = finalLines;
    }

    public Location getLocation() {
        return this.location;
    }

    public Player getPlayer() {
        return this.player;
    }

    private void NmsDestroy(EntityArmorStand hololine) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(hololine.getId());
        (((CraftPlayer) this.player).getHandle()).playerConnection.sendPacket(packet);
    }

    private Location getNmsLocation(EntityArmorStand hololine) {
        return new Location(hololine.getWorld().getWorld(), hololine.locX, hololine.locY, hololine.locZ);
    }

    public ArrayList<Integer> getEntitiesIds() {
        ArrayList<Integer> ids = new ArrayList<>();
        for (EntityArmorStand line : this.NmsArmorLines)
            ids.add(line.getBukkitEntity().getEntityId());
        return ids;
    }

    private void NmsSpawn(EntityArmorStand stand, String line, Location loc) {
        if (!line.equals("")) {
            stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0.0F, 0.0F);
            stand.setCustomName(line);
            stand.setCustomNameVisible(true);
            stand.setNoGravity(true);
            stand.setSmall(true);
            stand.setInvisible(true);
            stand.setBasePlate(false);
            stand.setArms(false);
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(stand);
            (((CraftPlayer) this.player).getHandle()).playerConnection.sendPacket(packet);
        } else {
            stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0.0F, 0.0F);
        }
    }

    private void spawn() {
        int ind = 0;
        for (String line : this.lines) {
            Location finalLoc = this.location.clone();
            finalLoc.setY(this.location.getY() + this.linesdistance * this.lines.size());
            if (this.player != null) {
                if (ind > 0)
                    finalLoc = getNmsLocation(this.NmsArmorLines.get(ind - 1));
                finalLoc.setY(finalLoc.getY() - this.linesdistance);
                WorldServer s = ((CraftWorld) this.location.getWorld()).getHandle();
                EntityArmorStand stand = new EntityArmorStand(s);
                this.NmsArmorLines.add(stand);
                NmsSpawn(stand, line, finalLoc);
            } else {
                if (ind > 0)
                    finalLoc = this.armor_lines.get(ind - 1).getLocation();
                finalLoc.setY(finalLoc.getY() - this.linesdistance);
                ArmorStand Armorline = (ArmorStand) this.location.getWorld().spawnEntity(finalLoc, EntityType.ARMOR_STAND);
                Armorline.setBasePlate(false);
                Armorline.setCustomNameVisible(true);
                Armorline.setGravity(false);
                Armorline.setCanPickupItems(false);
                Armorline.setCustomName(line);
                Armorline.setSmall(true);
                Armorline.setVisible(false);
                this.armor_lines.add(Armorline);
                if (line.equals(""))
                    Armorline.remove();
            }
            ind++;
        }
    }

    private void despawn() {
        if (this.player != null) {
            for (EntityArmorStand nmsStand : this.NmsArmorLines)
                NmsDestroy(nmsStand);
            this.NmsArmorLines.clear();
        } else {
            for (ArmorStand line : this.armor_lines)
                line.remove();
            this.armor_lines.clear();
        }
    }

    public void setDistanceBetweenLines(Double distance) {
        this.linesdistance = distance;
    }

    public void display() {
        spawn();
    }

    public void update(ArrayList<String> lines) {
        ArrayList<String> finalLines = new ArrayList<>();
        for (String line : lines) {
            finalLines.add(Core.col(line));
        }
        lines = finalLines;

        if (this.player != null) {
            int ind = 0;
            for (String newline : lines) {
                if (this.lines.size() >= ind) {
                    String oldline = this.lines.get(ind);
                    if (!newline.equals(oldline))
                        if (!newline.equals("")) {
                            EntityArmorStand oldstand = this.NmsArmorLines.get(ind);
                            Location location = this.location.clone();
                            location.setY(this.location.getY() + this.linesdistance * lines.size());
                            if (ind > 0)
                                location = getNmsLocation(this.NmsArmorLines.get(ind - 1));
                            location.setY(location.getY() - this.linesdistance);
                            WorldServer worldServer = ((CraftWorld) this.location.getWorld()).getHandle();
                            EntityArmorStand entityArmorStand1 = new EntityArmorStand(worldServer);
                            NmsSpawn(entityArmorStand1, newline, location);
                            this.NmsArmorLines.set(ind, entityArmorStand1);
                            this.lines.set(ind, newline);
                            NmsDestroy(oldstand);
                        } else {
                            this.lines.set(ind, newline);
                            EntityArmorStand oldstand = this.NmsArmorLines.get(ind);
                            NmsDestroy(oldstand);
                        }
                    ind++;
                    continue;
                }
                Location finalLoc = this.location.clone();
                finalLoc.setY(this.location.getY() + this.linesdistance * lines.size());
                finalLoc = getNmsLocation(this.NmsArmorLines.get(ind - 1));
                finalLoc.setY(finalLoc.getY() - this.linesdistance);
                WorldServer s = ((CraftWorld) this.location.getWorld()).getHandle();
                EntityArmorStand stand = new EntityArmorStand(s);
                this.NmsArmorLines.add(stand);
                this.lines.add(newline);
                NmsSpawn(stand, newline, finalLoc);
            }
            if (lines.size() > this.lines.size()) {
                int dif = lines.size() - this.lines.size();
                for (int in = 0; in <= dif; in++) {
                    int arrayind = this.lines.size() - 1 - in;
                    this.lines.remove(arrayind);
                    NmsDestroy(this.NmsArmorLines.get(arrayind));
                    this.NmsArmorLines.remove(arrayind);
                }
            }
        } else {
            int ind = 0;
            for (String newline : lines) {
                if (this.lines.size() >= ind) {
                    String oldline = this.lines.get(ind);
                    if (!newline.equals(oldline))
                        if (!newline.equals("")) {
                            this.armor_lines.get(ind).setCustomName(newline);
                        } else {
                            this.lines.set(ind, newline);
                            ArmorStand oldstand = this.armor_lines.get(ind);
                            oldstand.remove();
                        }
                    ind++;
                    continue;
                }
                Location finalLoc = this.location.clone();
                finalLoc.setY(this.location.getY() + this.linesdistance * lines.size());
                finalLoc = this.armor_lines.get(ind - 1).getLocation();
                finalLoc.setY(finalLoc.getY() - this.linesdistance);
                ArmorStand Armorline = (ArmorStand) this.location.getWorld().spawnEntity(finalLoc, EntityType.ARMOR_STAND);
                Armorline.setBasePlate(false);
                Armorline.setCustomNameVisible(true);
                Armorline.setGravity(false);
                Armorline.setCanPickupItems(false);
                Armorline.setCustomName(newline);
                Armorline.setSmall(true);
                Armorline.setVisible(false);
                this.armor_lines.add(Armorline);
                this.lines.add(newline);
            }
            if (lines.size() > this.lines.size()) {
                int dif = lines.size() - this.lines.size();
                for (int in = 0; in <= dif; in++) {
                    int arrayind = this.lines.size() - 1 - in;
                    this.lines.remove(arrayind);
                    this.armor_lines.get(arrayind).remove();
                    this.armor_lines.remove(arrayind);
                }
            }
        }
    }

    public void updateLine(int index, String text) {
        if (this.lines.size() >= index) {
            int realindex = this.lines.size() - 1 - index;
            String oldtext = this.lines.get(realindex);
            if (!text.equals(oldtext)) {
                if (this.player != null) {
                    if (!text.equals("")) {
                        EntityArmorStand oldstand = this.NmsArmorLines.get(realindex);
                        Location finalLoc = this.location.clone();
                        finalLoc.setY(this.location.getY() + this.linesdistance * this.lines.size());
                        if (realindex > 0)
                            finalLoc = getNmsLocation(this.NmsArmorLines.get(realindex - 1));
                        finalLoc.setY(finalLoc.getY() - this.linesdistance);
                        WorldServer s = ((CraftWorld) this.location.getWorld()).getHandle();
                        EntityArmorStand stand = new EntityArmorStand(s);
                        NmsSpawn(stand, text, finalLoc);
                        this.NmsArmorLines.set(realindex, stand);
                        NmsDestroy(oldstand);
                    } else {
                        this.lines.set(realindex, text);
                        EntityArmorStand oldstand = this.NmsArmorLines.get(realindex);
                        NmsDestroy(oldstand);
                    }
                } else if (!text.equals("")) {
                    this.armor_lines.get(realindex).setCustomName(text);
                } else {
                    ArmorStand oldstand = this.armor_lines.get(realindex);
                    oldstand.remove();
                }
                this.lines.set(realindex, text);
            }
        }
    }

    public void removeLine(int index) {
        if (this.lines.size() >= index) {
            int realindex = this.lines.size() - 1 - index;
            if (this.player != null) {
                EntityArmorStand stand = this.NmsArmorLines.get(realindex);
                this.NmsArmorLines.remove(stand);
                NmsDestroy(stand);
            } else {
                this.armor_lines.get(realindex).remove();
            }
            this.lines.remove(realindex);
        }
    }

    public void delete() {
        despawn();
        this.player = null;
        this.NmsArmorLines = new ArrayList<>();
        this.armor_lines = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.location = null;
    }
}