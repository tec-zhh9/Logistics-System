package com.logistics.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.logistics.entity.User;
import com.logistics.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public PageInfo<User> page(int pageNum, int pageSize, String username, String name, String phone) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(userMapper.selectAll(username, name, phone));
    }

    public void add(User user) {
        userMapper.insert(user);
    }

    public void updateProfile(User user) {
        userMapper.updateProfile(user);
    }

    public void updatePassword(Long id, String password) {
        userMapper.updatePassword(id, password);
    }

    public void updateStatus(Long id, String status) {
        userMapper.updateStatus(id, status);
    }

    public void delete(Long id) {
        userMapper.deleteById(id);
    }
}
