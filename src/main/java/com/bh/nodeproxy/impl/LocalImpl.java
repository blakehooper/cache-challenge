package com.bh.nodeproxy.impl;

import com.bh.model.NodeDefinition;
import com.bh.nodeproxy.NodeProxy;

import java.util.HashMap;

public class LocalImpl implements NodeProxy {

    NodeDefinition nodeDefinition;

    HashMap<String, String> memStore = new HashMap<>();

    public NodeDefinition getNodeDefinition() {
        return nodeDefinition;
    }

    public void setNodeDefinition(NodeDefinition nodeDefinition) {
        this.nodeDefinition = nodeDefinition;
    }

    public HashMap<String, String> getMemStore() {
        return memStore;
    }

    public void setMemStore(HashMap<String, String> memStore) {
        this.memStore = memStore;
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
