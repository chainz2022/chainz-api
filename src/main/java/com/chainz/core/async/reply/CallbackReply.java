package com.chainz.core.async.reply;

public interface CallbackReply {
    void then(Reply p0);

    void error(Exception p0);
}
