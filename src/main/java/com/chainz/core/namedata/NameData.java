package com.chainz.core.namedata;

import com.chainz.core.async.reply.NameReply;

import java.util.UUID;

public interface NameData {
    NameReply getNameFromUUID(UUID p0);

    NameReply getUUIDFromAllMethods(String p0);

    NameReply getRealUUIDFromFakeNick(String p0);

    boolean existsName(String p0);
}
