package com.bh;

import com.bh.nodeproxy.NodeProxy;

public class CacheClient {
    NodeManager nodeManager;

    public CacheClient(NodeManager nodeManager) {
        this.nodeManager = nodeManager;
    }

    void cache(String key, String value) {
        NodeProxy proxy = nodeManager.findNode(key);
        proxy.cache(key, value);
    }

    void invalidate(String key) {
        NodeProxy proxy = nodeManager.findNode(key);
        proxy.invalidate(key);
    }

    String retrieve(String key) {
        NodeProxy proxy = nodeManager.findNode(key);
        return proxy.retrieve(key);
    }

}
