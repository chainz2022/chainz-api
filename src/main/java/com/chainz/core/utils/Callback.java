package com.chainz.core.utils;

public interface Callback<V> {
    void then(V paramV);

    void error(Exception paramException);
}
