package com.bh.nodemanager.nodeproxy;

public interface NodeProxy {
    void cache(String key, String value);
    void invalidate(String key);
    String retrieve(String key);
}
