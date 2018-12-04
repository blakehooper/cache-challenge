package com.bh.model;

import java.util.UUID;

public class NodeDefinition {

    private UUID nodeId;

    private String hostname;

    private int port;

    private NodeType type;

    public NodeDefinition() {
    }

    public NodeDefinition(UUID nodeId, String hostname, int port, NodeType type) {
        this.nodeId = nodeId;
        this.hostname = hostname;
        this.port = port;
        this.type = type;
    }

    public UUID getNodeId() {
        return nodeId;
    }

    public void setNodeId(UUID nodeId) {
        this.nodeId = nodeId;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "NodeDefinition{" +
                "nodeId=" + nodeId +
                ", hostname='" + hostname + '\'' +
                ", port=" + port +
                ", type=" + type +
                '}';
    }

}
