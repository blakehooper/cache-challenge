package com.bh;

import com.bh.model.NodeDefinition;
import com.bh.model.NodeType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CacheClientBuilder {
    static CacheClient buildCacheClient() {
        return buildCacheClient(loadDefaultNodeConfig());
    }

    static CacheClient buildCacheClient(List<NodeDefinition> definitions) {
        NodeManager nodeManager = buildNodeManager(definitions);
        return new CacheClient(nodeManager);
    }

    static NodeManager buildNodeManager(List<NodeDefinition> definitions) {
        NodeManager nodeManager = new NodeManager();
        definitions.forEach(nodeManager::addNode);
        return nodeManager;
    }

    // Basic configuration
    static List<NodeDefinition> loadDefaultNodeConfig() {
        List<NodeDefinition> definitions = new ArrayList<>();
        definitions.add(new NodeDefinition(UUID.randomUUID(), "hostname", 8080, NodeType.LOCAL));
        return definitions;
    }
}
