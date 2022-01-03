package com.example.zookeeper.models;

import lombok.Data;

@Data
public class UserEntity {
    private Long id;
    private String userName;
    private String gender;
    private String mobile;
}
