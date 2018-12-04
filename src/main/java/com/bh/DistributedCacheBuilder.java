package com.bh;

import com.bh.cacheclient.CacheClient;
import com.bh.cacheclient.CacheClientImpl;
import com.bh.model.NodeDefinition;
import com.bh.model.NodeType;
import com.bh.nodeeventhandler.NodeEventHandler;
import com.bh.nodeeventhandler.impl.NodeEventHandlerImpl;
import com.bh.nodemanager.NodeManager;
import com.bh.nodemanager.NodeManagerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DistributedCacheBuilder {

    static DistributedCacheBuilder instance;

    CacheClient cacheClient;
    NodeEventHandler nodeEventHandler;

    protected DistributedCacheBuilder() {

    }

    // Not thread safe singleton pattern
    // Expected use during start up only to provide
    // other beans for singleton containers
    public static DistributedCacheBuilder getInstance() {
        if (instance == null) {
            instance = new DistributedCacheBuilder();
        }
        return instance;
    }

    CacheClient getCacheClient() {
        if (cacheClient == null) {
            init();
        }
        return cacheClient;
    }

    NodeEventHandler getNodeEventHandler() {
        if (nodeEventHandler == null) {
            init();
        }
        return nodeEventHandler;
    }

    protected void init() {
        NodeManager nodeManager = buildNodeManager(loadDefaultNodeConfig());
        CacheClient cacheClient = new CacheClientImpl(nodeManager);
        this.cacheClient = cacheClient;
        NodeEventHandler nodeEventHandler = new NodeEventHandlerImpl(cacheClient);
        this.nodeEventHandler = nodeEventHandler;
    }

    protected NodeManager buildNodeManager(List<NodeDefinition> definitions) {
        NodeManagerImpl nodeManager = new NodeManagerImpl();
        nodeManager.initialNodes(definitions);
        return nodeManager;
    }

    // Basic configuration
    protected List<NodeDefinition> loadDefaultNodeConfig() {
        List<NodeDefinition> definitions = new ArrayList<>();
        definitions.add(new NodeDefinition(UUID.randomUUID(), "cache-node-0", 8080, NodeType.LOCAL));
        definitions.add(new NodeDefinition(UUID.randomUUID(), "cache-node-1", 8080, NodeType.LOCAL));
        return definitions;
    }
}
