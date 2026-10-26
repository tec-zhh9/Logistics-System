package com.logistics.controller;

import com.github.pagehelper.PageInfo;
import com.logistics.common.Result;
import com.logistics.entity.Announcement;
import com.logistics.service.AnnouncementService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 公告管理
 */
@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    @Resource
    private AnnouncementService announcementService;

    @GetMapping("/page")
    public Result<PageInfo<Announcement>> page(@RequestParam(defaultValue = "1") int pageNum,
                                               @RequestParam(defaultValue = "10") int pageSize,
                                               @RequestParam(required = false) String title) {
        return Result.success(announcementService.page(pageNum, pageSize, title));
    }

    @GetMapping("/list")
    public Result<java.util.List<Announcement>> list(@RequestParam(required = false) String title) {
        return Result.success(announcementService.list(title));
    }

    @GetMapping("/{id}")
    public Result<Announcement> getById(@PathVariable Long id) {
        return Result.success(announcementService.getById(id));
    }

    @PostMapping
    public Result<Void> add(@RequestBody Announcement announcement) {
        announcementService.add(announcement);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Announcement announcement) {
        announcementService.update(announcement);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        announcementService.delete(id);
        return Result.success();
    }
}
