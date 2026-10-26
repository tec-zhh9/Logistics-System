package com.logistics.controller;

import com.github.pagehelper.PageInfo;
import com.logistics.common.Result;
import com.logistics.entity.Region;
import com.logistics.service.RegionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 区域管理
 */
@RestController
@RequestMapping("/api/region")
public class RegionController {

    @Resource
    private RegionService regionService;

    @GetMapping("/page")
    public Result<PageInfo<Region>> page(@RequestParam(defaultValue = "1") int pageNum,
                                         @RequestParam(defaultValue = "10") int pageSize,
                                         @RequestParam(required = false) String name) {
        return Result.success(regionService.page(pageNum, pageSize, name));
    }

    @GetMapping("/list")
    public Result<List<Region>> list() {
        return Result.success(regionService.list());
    }

    @PostMapping
    public Result<Void> add(@RequestBody Region region) {
        regionService.add(region);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Region region) {
        regionService.update(region);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        regionService.delete(id);
        return Result.success();
    }
}
