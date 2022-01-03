package com.example.zookeeper.service.impl;

import com.example.zookeeper.models.UserEntity;
import com.example.zookeeper.service.TestService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestServiceImpl implements TestService {
    @Override
    public List<UserEntity> userNameList() {
        return null;
    }
}
