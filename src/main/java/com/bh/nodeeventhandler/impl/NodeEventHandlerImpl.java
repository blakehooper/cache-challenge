package com.bh.nodeeventhandler.impl;

import com.bh.model.NodeDefinition;
import com.bh.nodeeventhandler.NodeEventHandler;
import com.bh.nodemanager.NodeManager;

public class NodeEventHandlerImpl implements NodeEventHandler {
    NodeManager nodeManager;

    public NodeEventHandlerImpl() {
    }

    public NodeEventHandlerImpl(NodeManager nodeManager) {
        this.nodeManager = nodeManager;
    }

    public NodeManager getNodeManager() {
        return nodeManager;
    }

    public void setNodeManager(NodeManager nodeManager) {
        this.nodeManager = nodeManager;
    }

    @Override
    public void nodeAdded(NodeDefinition node) {
        nodeManager.addNode(node);
    }

    @Override
    public void nodeRemoved(NodeDefinition node) {
        nodeManager.removeNode(node);
    }

    @Override
    public void nodeShuttingDown(NodeDefinition node) {
        nodeManager.nodeShuttingDown(node);
    }
}
