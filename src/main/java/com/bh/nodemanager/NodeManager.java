package com.bh.nodemanager;

import com.bh.model.NodeDefinition;
import com.bh.nodemanager.nodeproxy.NodeProxy;

public interface NodeManager {
    NodeProxy findNode(String key);

    void addNode(NodeDefinition nodeDefinition);
    void removeNode(NodeDefinition nodeDefinition);
    void nodeShuttingDown(NodeDefinition nodeDefinition);
}
