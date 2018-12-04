package com.bh.nodemanager;

import com.bh.model.NodeDefinition;
import com.bh.model.NodeType;
import com.bh.nodemanager.nodeproxy.NodeProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class NodeManagerTest {

    private NodeManagerImpl nodeManager;

    @BeforeEach
    void setUp() {

        List<NodeDefinition> definitions = new ArrayList<>();
        definitions.add(new NodeDefinition(UUID.randomUUID(), "test-node", 8080, NodeType.LOCAL));
        definitions.add(new NodeDefinition(UUID.randomUUID(), "something", 8080, NodeType.LOCAL));

        this.nodeManager = new NodeManagerImpl();
        this.nodeManager.initialNodes(definitions);
    }

    @DisplayName("Can find node")
    @ParameterizedTest
    @MethodSource("keyNodeToMap")
    void findNode(String key, String expectedNode) {
        NodeProxy nodeProxy = this.nodeManager.findNode(key);
        assert nodeProxy.getNodeDefinition().getHostname().equals(expectedNode);
    }

    private static Stream<Arguments> keyNodeToMap() {
        return Stream.of(
                Arguments.of("any-key", "test-node"),
                Arguments.of("other-key-aa", "test-node"),
                Arguments.of("some-key-1", "something")
        );
    }

    @DisplayName("Can add and remove node")
    @Test
    void addNode() {
        // Preconditions
        assert nodeManager.getAvailableNodes().keySet().size() == 4;

        NodeDefinition nodeDefinition = new NodeDefinition(UUID.randomUUID(), "new-test-node", 8080, NodeType.LOCAL);

        nodeManager.addNode(nodeDefinition);

        assert nodeManager.getAvailableNodes().keySet().size() == 6;

        nodeManager.removeNode(nodeDefinition);

        assert nodeManager.getAvailableNodes().keySet().size() == 4;

    }

}
