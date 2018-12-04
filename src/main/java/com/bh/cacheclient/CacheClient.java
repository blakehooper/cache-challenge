package com.bh.cacheclient;

public interface CacheClient {
    void cache(String key, String value);
    void invalidate(String key);
    String retrieve(String key);
}
