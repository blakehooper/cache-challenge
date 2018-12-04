package com.bh.nodeeventhandler;

import com.bh.model.NodeDefinition;

public interface NodeEventHandler {
    /*
     * Difference between nodeRemoved and nodeShuttingDown is that nodeRemoved means the
     * node is down already, while nodeShuttingDown signals the intent to shutdown.
     * nodeShuttingDown is only required in the challenge portion
     */


    void nodeAdded(NodeDefinition node);

    /**
     * Please use the graceful version "nodeShuttingDown"
     */
    @Deprecated()
    void nodeRemoved(NodeDefinition node);

    void nodeShuttingDown(NodeDefinition node);
}
