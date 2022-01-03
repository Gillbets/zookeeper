package com.example.zookeeper;

import com.alibaba.fastjson.JSON;
import com.example.zookeeper.config.RegisterConfig;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

@SpringBootTest
class ZookeeperApplicationTests {

    @Test
    void contextLoads() {
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            ZooKeeper zooKeeper = new ZooKeeper("192.168.50.192:2181," +
                    "192.168.50.193:2181,192.168.50.194:2181",
                    4000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (Event.KeeperState.SyncConnected == event.getState()) {
                        //如果收到了服务端的响应事件，连接成功
                        countDownLatch.countDown();
                    }
                }
            });

            countDownLatch.await();
            //测试连接
            System.out.println(zooKeeper.getState());

            String data = "创业慧康";
            Stat stat = new Stat();
            if (zooKeeper.exists("/bsoft",true) == null){
                zooKeeper.create("/bsoft", data.getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            data = "果丹皮";
            if (zooKeeper.exists("/bsoft/mdt",true) == null){
                zooKeeper.create("/bsoft/mdt", data.getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }


            byte[] responseData = zooKeeper.getData("/bsoft/mdt", true, stat);
            String responseString = new String(responseData);
            System.out.println(responseString);
            System.out.println(JSON.toJSONString(stat));


        } catch (Exception ignore) {

        }
    }

    @Test
    void testZookeeper(){
        RegisterConfig config = new RegisterConfig();
        // 模拟一个配置项，实际生产中会在系统初始化时从配置文件中加载进来
        config.save("timeout", "1000");

        // 每3S打印一次获取到的配置项
        for (int i = 0; i < 100; i++) {
            System.out.println(config.get("timeout"));
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
