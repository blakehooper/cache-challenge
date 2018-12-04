package com.bh.nodeeventhandler;

import com.bh.DistributedCacheBuilder;
import com.bh.cacheclient.CacheClient;
import com.bh.model.NodeDefinition;
import com.bh.model.NodeType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class NodeEventHandlerTest {
    private static DistributedCacheBuilder distributedCacheBuilder;

    private NodeEventHandler nodeEventHandler;

    @BeforeAll
    static void setUpOnce() {
        distributedCacheBuilder = DistributedCacheBuilder.getInstance();
    }

    @BeforeEach
    void setUp() {
        nodeEventHandler = distributedCacheBuilder.getNodeEventHandler();
    }

    @DisplayName("Can Add Nodes")
    @Test
    void basicFunctionality() {
        // Given
        NodeDefinition nodeDefinition = new NodeDefinition(UUID.randomUUID(), "test-node", 8080, NodeType.LOCAL);
        nodeEventHandler.nodeAdded(nodeDefinition);
        // Verify
        nodeEventHandler.nodeRemoved(nodeDefinition);
    }
}
