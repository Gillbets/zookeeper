package com.example.zookeeper.service;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;

public interface DiscoverService {
    void discovery(WatchedEvent e) throws KeeperException, InterruptedException;
}
