package com.logistics.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.logistics.entity.Courier;
import com.logistics.mapper.CourierMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CourierService {

    @Resource
    private CourierMapper courierMapper;

    public PageInfo<Courier> page(int pageNum, int pageSize, String username, String name) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(courierMapper.selectAll(username, name));
    }

    public void add(Courier courier) {
        courierMapper.insert(courier);
    }

    public void updateProfile(Courier courier) {
        courierMapper.updateProfile(courier);
    }

    public void updatePassword(Long id, String password) {
        courierMapper.updatePassword(id, password);
    }

    public void updateStatus(Long id, String status) {
        courierMapper.updateStatus(id, status);
    }

    public void delete(Long id) {
        courierMapper.deleteById(id);
    }
}
