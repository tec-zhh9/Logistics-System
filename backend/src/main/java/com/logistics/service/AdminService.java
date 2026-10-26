package com.logistics.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.logistics.entity.Admin;
import com.logistics.mapper.AdminMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Resource
    private AdminMapper adminMapper;

    public PageInfo<Admin> page(int pageNum, int pageSize, String username) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(adminMapper.selectAll(username));
    }

    public void add(Admin admin) {
        adminMapper.insert(admin);
    }

    public void updateProfile(Admin admin) {
        adminMapper.updateProfile(admin);
    }

    public void updatePassword(Long id, String password) {
        adminMapper.updatePassword(id, password);
    }

    public void delete(Long id) {
        adminMapper.deleteById(id);
    }
}
