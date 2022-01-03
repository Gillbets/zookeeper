package com.example.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class ZookeeperApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZookeeperApplication.class, args);


    }

}
