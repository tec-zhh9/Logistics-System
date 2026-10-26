package com.logistics.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.logistics.entity.NetworkPoint;
import com.logistics.mapper.NetworkPointMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NetworkPointService {

    @Resource
    private NetworkPointMapper networkPointMapper;

    public PageInfo<NetworkPoint> page(int pageNum, int pageSize, String name, Long regionId) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(networkPointMapper.selectAll(name, regionId));
    }

    public List<NetworkPoint> list(String name, Long regionId) {
        return networkPointMapper.selectAll(name, regionId);
    }

    public NetworkPoint getById(Long id) {
        return networkPointMapper.selectById(id);
    }

    public void add(NetworkPoint point) {
        networkPointMapper.insert(point);
    }

    public void update(NetworkPoint point) {
        networkPointMapper.update(point);
    }

    public void delete(Long id) {
        networkPointMapper.deleteById(id);
    }
}
