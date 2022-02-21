// 
// Decompiled by Procyon v0.5.36
// 

package es.eltrueno.protocol.protocollib.wrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerEntityDestroy extends AbstractPacket {
    public static final PacketType TYPE;

    static {
        TYPE = PacketType.Play.Server.ENTITY_DESTROY;
    }

    public WrapperPlayServerEntityDestroy() {
        super(new PacketContainer(WrapperPlayServerEntityDestroy.TYPE), WrapperPlayServerEntityDestroy.TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerEntityDestroy(final PacketContainer packet) {
        super(packet, WrapperPlayServerEntityDestroy.TYPE);
    }

    public int getCount() {
        return this.handle.getIntegerArrays().read(0).length;
    }

    public int[] getEntityIDs() {
        return this.handle.getIntegerArrays().read(0);
    }

    public void setEntityIds(final int[] value) {
        this.handle.getIntegerArrays().write(0, value);
    }
}
