package com.bh.nodemanager;

import com.bh.model.NodeDefinition;
import com.bh.nodemanager.nodeproxy.NodeProxy;
import com.bh.nodemanager.nodeproxy.impl.LocalImpl;
import com.bh.nodemanager.nodeproxy.impl.MemcacheImpl;
import com.bh.nodemanager.nodeproxy.impl.RedisImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.IntStream;

public class NodeManagerImpl implements NodeManager {
    private Map<BigDecimal, NodeProxy> availableNodes = new TreeMap<>();
    private Integer numberOfReplications = 2;

    public Map<BigDecimal, NodeProxy> getAvailableNodes() {
        return availableNodes;
    }

    public void setAvailableNodes(Map<BigDecimal, NodeProxy> availableNodes) {
        this.availableNodes = availableNodes;
    }

    public Integer getNumberOfReplications() {
        return numberOfReplications;
    }

    public void setNumberOfReplications(Integer numberOfReplications) {
        this.numberOfReplications = numberOfReplications;
    }

    @Override
    public NodeProxy findNode(String key) {
        return findNodeForKey(key);
    }

    @Override
    public void addNode(NodeDefinition nodeDefinition) {
        switch (nodeDefinition.getType()) {
            case LOCAL:
                LocalImpl localImpl = new LocalImpl();
                localImpl.setNodeDefinition(nodeDefinition);
                addPositionsWithReplications(nodeDefinition, localImpl);
                break;
            case REDIS:
                RedisImpl redisImpl = new RedisImpl();
                redisImpl.setNodeDefinition(nodeDefinition);
                addPositionsWithReplications(nodeDefinition, redisImpl);
                break;
            case MEMCACHED:
                MemcacheImpl memcacheImpl = new MemcacheImpl();
                memcacheImpl.setNodeDefinition(nodeDefinition);
                addPositionsWithReplications(nodeDefinition, memcacheImpl);
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public void removeNode(NodeDefinition nodeDefinition) {
        IntStream.range(0, numberOfReplications).forEach(replication -> {
            BigDecimal positionForKey = findPositionForKey(buildNodeKey(nodeDefinition.getHostname(), replication));
            availableNodes.remove(positionForKey);
        });
    }

    @Override
    public void nodeShuttingDown(NodeDefinition nodeDefinition) {
        IntStream.range(0, numberOfReplications).forEach(replication -> {
            BigDecimal positionForKey = findPositionForKey(buildNodeKey(nodeDefinition.getHostname(), replication));
            NodeProxy nodeToClose = availableNodes.get(positionForKey);
            availableNodes.remove(positionForKey);
            nodeToClose.listKeys().forEach(key -> {
                String value = nodeToClose.retrieve(key);
                NodeProxy updateNode = findNodeForKey(key);
                updateNode.cache(key, value);
            });
        });
    }

    public void initialNodes(List<NodeDefinition> nodeDefinitions) {
        nodeDefinitions.forEach(this::addNode);
    }

    private String buildNodeKey(String hostname, int replicationNumber) {
        return hostname + "_" + replicationNumber;
    }

    private void addPositionsWithReplications(NodeDefinition nodeDefinition, NodeProxy proxy) {
        IntStream.range(0, numberOfReplications).forEach(replication -> {
            BigDecimal positionForKey = findPositionForKey(buildNodeKey(nodeDefinition.getHostname(), replication));
            availableNodes.put(positionForKey, proxy);
            rebalanceForAddedNode(positionForKey);
        });
    }

    private void rebalanceForAddedNode(BigDecimal addedKey) {
        // Natural order guarenteed by using TreeSet
        List<BigDecimal> orderedKeyList = new ArrayList<>(availableNodes.keySet());

        // First node added
        if (orderedKeyList.size() == 0) {
            // Do nothing
            return;
        }

        int nodeIndexToRebalance = findIndexToRebalance(orderedKeyList, addedKey);
        rebalanceNode(orderedKeyList.get(nodeIndexToRebalance), addedKey);
    }

    private void rebalanceNode(BigDecimal existingKey, BigDecimal addedKey) {
        NodeProxy existingNode = availableNodes.get(existingKey);
        NodeProxy addedNode = availableNodes.get(addedKey);

        Set<String> existingSet = existingNode.listKeys();
        existingSet.stream().forEach(key -> {

            // The key needs to be moved
            if (findNode(key).equals(addedNode)) {
                String value = existingNode.retrieve(key);
                addedNode.cache(key, value);

                existingNode.invalidate(key);
            };
        });
    }

    private int findIndexToRebalance(List<BigDecimal> orderedKeyList, BigDecimal addedKey) {

        int addedKeyIndex = orderedKeyList.indexOf(addedKey);

        // Exception case for inserted at first position

        if (addedKeyIndex == 0) {
            return orderedKeyList.size() -1;
        } else {
            return addedKeyIndex - 1;
        }
    }

    private BigDecimal findPositionForKey(String key) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // Would normally throw a custom exception to propagate to users of this client
            // log.error
            throw new RuntimeException();
        }
        m.reset();
        m.update(key.getBytes());

        byte[] digest = m.digest();

        // Magic number here to create a positive integer representation
        // https://docs.oracle.com/javase/8/docs/api/java/math/BigInteger.html
        BigInteger bigInt = new BigInteger(1, digest);
        bigInt = bigInt.mod(BigInteger.valueOf(1000000l));
        BigDecimal circleRotationPercentage = new BigDecimal(bigInt).divide(BigDecimal.valueOf(1000000l));

        return circleRotationPercentage;

    }

    private NodeProxy findNodeForKey(String key) {
        BigDecimal positionForKey = findPositionForKey(key);

        // Natural order guarenteed by using TreeSet
        List<BigDecimal> orderedKeyList = new ArrayList<>(availableNodes.keySet());

        // No nodes in list
        if (orderedKeyList.size() == 0) {
            throw new RuntimeException("No available nodes");
        }

        int searchResult = Collections.binarySearch(orderedKeyList, positionForKey);


        // The binary search function returns the actual bucket index in the case of an exact match.
        // While it is unlikely to resolve to a used positionForKey,
        // it's much more likely than a pure hash function with odds of 1 / 1,000,000
        // So I thought i'd guard against it
        // https://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#binarySearch-java.util.List-T-
        int index;
        if (searchResult >= 0) {
            index = searchResult;
        } else {
            index = Math.abs(searchResult) - 1;
        }

        // Back to start of circle
        if (index == orderedKeyList.size()) {
            index = 0;
        }
        return availableNodes.get(orderedKeyList.get(index));
    }
}
