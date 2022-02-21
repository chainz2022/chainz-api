package com.chainz.core.reservedslots;

import com.chainz.core.async.reply.CallbackReply;
import com.chainz.core.async.reply.ServerReservedSlotsReply;

import java.util.ArrayList;

public interface ReserveSlots {
    void reserveSlot(String p0, String p1, CallbackReply p2);

    void reserveSlots(String p0, ArrayList<String> p1, CallbackReply p2);

    void getReservedSlots(String p0, CallbackReply p1);

    ServerReservedSlotsReply getReservedSlotsSync(String p0);

    void removePlayerReservedSlot(String p0, String p1, CallbackReply p2);

    void removePlayersReservedSlot(String p0, ArrayList<String> p1, CallbackReply p2);
}
