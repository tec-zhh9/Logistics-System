package com.logistics.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.logistics.entity.Region;
import com.logistics.mapper.RegionMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionService {

    @Resource
    private RegionMapper regionMapper;

    public PageInfo<Region> page(int pageNum, int pageSize, String name) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(regionMapper.selectAll(name));
    }

    public List<Region> list() {
        return regionMapper.selectAll(null);
    }

    public void add(Region region) {
        regionMapper.insert(region);
    }

    public void update(Region region) {
        regionMapper.update(region);
    }

    public void delete(Long id) {
        regionMapper.deleteById(id);
    }
}
