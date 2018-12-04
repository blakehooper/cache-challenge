package com.bh.nodeeventhandler.impl;

import com.bh.cacheclient.CacheClient;
import com.bh.model.NodeDefinition;
import com.bh.nodeeventhandler.NodeEventHandler;

public class NodeEventHandlerImpl implements NodeEventHandler {
    CacheClient cacheClient;

    public NodeEventHandlerImpl() {
    }

    public NodeEventHandlerImpl(CacheClient cacheClient) {
        this.cacheClient = cacheClient;
    }

    public CacheClient getCacheClient() {
        return cacheClient;
    }

    public void setCacheClient(CacheClient cacheClient) {
        this.cacheClient = cacheClient;
    }

    @Override
    public void nodeAdded(NodeDefinition node) {

    }

    @Override
    public void nodeRemoved(NodeDefinition node) {

    }
}
