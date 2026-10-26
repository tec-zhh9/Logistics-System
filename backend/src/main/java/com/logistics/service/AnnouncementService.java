package com.logistics.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.logistics.entity.Announcement;
import com.logistics.mapper.AnnouncementMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementService {

    @Resource
    private AnnouncementMapper announcementMapper;

    public PageInfo<Announcement> page(int pageNum, int pageSize, String title) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(announcementMapper.selectAll(title));
    }

    public List<Announcement> list(String title) {
        return announcementMapper.selectAll(title);
    }

    public Announcement getById(Long id) {
        return announcementMapper.selectById(id);
    }

    public void add(Announcement announcement) {
        announcementMapper.insert(announcement);
    }

    public void update(Announcement announcement) {
        announcementMapper.update(announcement);
    }

    public void delete(Long id) {
        announcementMapper.deleteById(id);
    }
}
