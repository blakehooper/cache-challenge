package com.bh.nodemanager.nodeproxy;

import com.bh.model.NodeDefinition;

public interface NodeProxy {
    NodeDefinition getNodeDefinition();
    void cache(String key, String value);
    void invalidate(String key);
    String retrieve(String key);
}
