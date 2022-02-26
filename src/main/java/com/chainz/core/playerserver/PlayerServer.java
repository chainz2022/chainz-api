package com.chainz.core.playerserver;

import com.chainz.core.async.reply.PlayerServerReply;

import java.util.UUID;

public interface PlayerServer {
    PlayerServerReply getPlayerServer(UUID p0);
}
