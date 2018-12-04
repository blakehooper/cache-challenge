package com.bh.nodemanager.nodeproxy.impl;

import com.bh.model.NodeDefinition;
import com.bh.nodemanager.nodeproxy.NodeProxy;

import java.util.HashMap;

public class MemcacheImpl implements NodeProxy {

    NodeDefinition nodeDefinition;

    HashMap<String, String> memStore = new HashMap<>();

    public NodeDefinition getNodeDefinition() {
        return nodeDefinition;
    }

    public void setNodeDefinition(NodeDefinition nodeDefinition) {
        this.nodeDefinition = nodeDefinition;
    }


    @Override
    public void cache(String key, String value) {
        memStore.put(key, value);
    }

    @Override
    public void invalidate(String key) {
        memStore.remove(key);
    }

    @Override
    public String retrieve(String key) {
        return memStore.get(key);
    }
}
