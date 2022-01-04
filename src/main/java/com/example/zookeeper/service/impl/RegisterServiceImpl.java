package com.example.zookeeper.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.example.zookeeper.config.ApplicationProperties;
import com.example.zookeeper.config.ServiceInstanceInfo;
import com.example.zookeeper.config.ZookeeperServerConfigProperties;
import com.example.zookeeper.service.RegisterService;
import com.example.zookeeper.utils.IPUtil;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Service;



public class RegisterServiceImpl implements RegisterService {
    private final Logger logger = Logger.getLogger(RegisterServiceImpl.class);
    private final ZooKeeper zooKeeper;
    private final ZookeeperServerConfigProperties configProperties;

    public RegisterServiceImpl(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
        this.configProperties = ZookeeperServerConfigProperties.config();
    }

    /**
     * 会在zookeeper中创建以下znode结构
     * /namespace/serviceName/serviceName0000000000
     * /serviceName0000000001
     * /serviceName0000000002
     *
     * @return 注册成功返回true
     */
    @Override
    public boolean registry() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            ServiceInstanceInfo info = generateServiceInfo();
            oos.writeObject(info);
            ensureZNodeExist(configProperties.getNamespace(), info.getServiceName());
            this.zooKeeper.create(configProperties.getNamespace() + "/" +
                            info.getServiceName() + "/" + info.getServiceName(),
                    baos.toByteArray(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);
        } catch (IOException | InterruptedException | KeeperException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    private void ensureZNodeExist(String namespace, String serviceName) throws KeeperException, InterruptedException {
        Stat exists = zooKeeper.exists(namespace, false);
        if (exists == null) {
            logger.info("zookeeper namespace:" + namespace + "不存在，开始创建...");
            String s = zooKeeper.create(namespace, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            logger.info("zookeeper namespace:" + s + "已创建！");
        }
        Stat isExist = zooKeeper.exists(namespace + "/" + serviceName, false);
        if (isExist == null) {
            logger.info("zookeeper service Znode:" + serviceName + "不存在，开始创建...");
            String s = zooKeeper.create(namespace + "/" + serviceName, null,
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT);
            logger.info("zookeeper service Znode:" + s + "已创建！");
        }
    }

    private ServiceInstanceInfo generateServiceInfo() {
        ApplicationProperties config = ApplicationProperties.config();
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return new ServiceInstanceInfo(config.getApplicationName(), "http://" + address.getHostAddress(),
                config.getServerPort());
    }
}
