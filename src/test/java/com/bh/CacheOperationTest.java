package com.bh;

import com.bh.cacheclient.CacheClient;
import com.bh.model.NodeDefinition;
import com.bh.model.NodeType;
import com.bh.nodeeventhandler.NodeEventHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

/**
 * In a production environment i'd move these to a different
 * source set as they are really integration tests
 */
@DisplayName("Smoke Spec")
class CacheOperationTest {

    private static DistributedCacheBuilder distributedCacheBuilder;

    private CacheClient cacheClient;
    private NodeEventHandler nodeEventHandler;

    @BeforeAll
    static void setUpOnce() {
        distributedCacheBuilder = DistributedCacheBuilder.getInstance();
    }

    @BeforeEach
    void setUp() {
        cacheClient = distributedCacheBuilder.getCacheClient();
        nodeEventHandler = distributedCacheBuilder.getNodeEventHandler();
    }

    @DisplayName("Simple read / write")
    @Test
    void basicFunctionality() {
        cacheClient.cache("test", "value");

        assert cacheClient.retrieve("test").equals("value");

        cacheClient.invalidate("test");

        assert cacheClient.retrieve("test") == null;
    }


    @DisplayName("Multiple read / write")
    @ParameterizedTest
    @ValueSource(strings = {
            "test",
            "other",
            "key-z"
    })
    void testCacheOperationsWithMultipleIterations(String key) {
        cacheClient.cache(key, key);

        assert cacheClient.retrieve(key).equals(key);

        cacheClient.invalidate(key);

        assert cacheClient.retrieve(key) == null;
    }

    @DisplayName("Can Add and Remove Nodes")
    @Test
    void addRemoveNodes() {
        NodeDefinition nodeDefinition = new NodeDefinition(UUID.randomUUID(), "test-node", 8080, NodeType.LOCAL);
        nodeEventHandler.nodeAdded(nodeDefinition);
        nodeEventHandler.nodeRemoved(nodeDefinition);
    }


}
