package com.bh;

import com.bh.model.NodeDefinition;
import com.bh.nodeproxy.NodeProxy;
import com.bh.nodeproxy.impl.LocalImpl;

import java.util.ArrayList;
import java.util.List;

public class NodeManager {
    List<NodeProxy> availableNodes = new ArrayList<>();

    public NodeProxy findNode(String key) {
        return availableNodes.get(0);
    }

    public void addNode(NodeDefinition nodeDefinition) {
        switch(nodeDefinition.getType()) {
            case LOCAL:
                LocalImpl localImpl = new LocalImpl();
                localImpl.setNodeDefinition(nodeDefinition);
                availableNodes.add(localImpl);
                break;
            default:
                throw new UnsupportedOperationException();
        }

    }
}
