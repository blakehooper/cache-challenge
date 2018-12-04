package com.bh.nodemanager;

import com.bh.nodemanager.nodeproxy.NodeProxy;

public interface NodeManager {
    NodeProxy findNode(String key);
}
