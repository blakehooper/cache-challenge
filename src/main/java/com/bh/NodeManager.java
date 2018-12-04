package com.bh;

import com.bh.model.NodeDefinition;
import com.bh.nodeproxy.NodeProxy;
import com.bh.nodeproxy.impl.LocalImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class NodeManager {
    Map<BigDecimal, NodeProxy> availableNodes = new TreeMap<>();

    public NodeProxy findNode(String key) {
        return findNodeForKey(key);
    }

    public void initialNodes(List<NodeDefinition> nodeDefinitions) {
        nodeDefinitions.forEach( nodeDefinition -> {
            switch(nodeDefinition.getType()) {
                case LOCAL:
                    LocalImpl localImpl = new LocalImpl();
                    localImpl.setNodeDefinition(nodeDefinition);
                    BigDecimal positionForKey = findPositionForKey(nodeDefinition.getNodeId().toString());
                    availableNodes.put(positionForKey, localImpl);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        });
    }

    private BigDecimal findPositionForKey(String key) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // Would normally throw a custom exception to propigate to users of this client
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
        int searchResult = Collections.binarySearch(orderedKeyList, positionForKey);


        // Just to explain this gem, the binary search function returns the actual bucket index
        // in the case of an exact match. While it is unlikely to resolve to a used positionForKey,
        // it's much more likely than a pure hash function with odds of 1 / 1,000,000
        // So I thought i'd guard against it
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