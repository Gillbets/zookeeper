package com.example.zookeeper.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class ServiceInstanceInfo implements Serializable {
    private String serviceName;
    private String ip;
    private int port;

    public ServiceInstanceInfo(String serviceName, String ip, int port) {
        this.serviceName = serviceName;
        this.ip = ip;
        this.port = port;
    }


}
