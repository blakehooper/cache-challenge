package com.bh.cacheclient;

import com.bh.nodemanager.NodeManager;
import com.bh.nodemanager.nodeproxy.NodeProxy;

public class CacheClientImpl implements CacheClient {
    NodeManager nodeManager;

    public CacheClientImpl(NodeManager nodeManager) {
        this.nodeManager = nodeManager;
    }

    @Override
    public void cache(String key, String value) {
        NodeProxy proxy = nodeManager.findNode(key);
        proxy.cache(key, value);
    }

    @Override
    public void invalidate(String key) {
        NodeProxy proxy = nodeManager.findNode(key);
        proxy.invalidate(key);
    }

    @Override
    public String retrieve(String key) {
        NodeProxy proxy = nodeManager.findNode(key);
        return proxy.retrieve(key);
    }

}
