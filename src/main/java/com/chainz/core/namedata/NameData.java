package com.chainz.core.namedata;

import com.chainz.core.async.reply.CallbackReply;

public interface NameData {
    void getNameFromUuidAsync(final String p0, final CallbackReply p1);

    void getUUIDFromAllMethods(final String p0, final CallbackReply p1);

    void getRealUUIDFromFakeNick(final String p0, final CallbackReply p1);

    void existsName(final String p0, final CallbackReply p1);
}
