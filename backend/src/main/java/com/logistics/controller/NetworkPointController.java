package com.logistics.controller;

import com.github.pagehelper.PageInfo;
import com.logistics.common.Result;
import com.logistics.entity.NetworkPoint;
import com.logistics.service.NetworkPointService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 网点管理
 */
@RestController
@RequestMapping("/api/network-point")
public class NetworkPointController {

    @Resource
    private NetworkPointService networkPointService;

    @GetMapping("/page")
    public Result<PageInfo<NetworkPoint>> page(@RequestParam(defaultValue = "1") int pageNum,
                                               @RequestParam(defaultValue = "10") int pageSize,
                                               @RequestParam(required = false) String name,
                                               @RequestParam(required = false) Long regionId) {
        return Result.success(networkPointService.page(pageNum, pageSize, name, regionId));
    }

    @GetMapping("/list")
    public Result<List<NetworkPoint>> list(@RequestParam(required = false) String name,
                                           @RequestParam(required = false) Long regionId) {
        return Result.success(networkPointService.list(name, regionId));
    }

    @GetMapping("/{id}")
    public Result<NetworkPoint> getById(@PathVariable Long id) {
        return Result.success(networkPointService.getById(id));
    }

    @PostMapping
    public Result<Void> add(@RequestBody NetworkPoint point) {
        networkPointService.add(point);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody NetworkPoint point) {
        networkPointService.update(point);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        networkPointService.delete(id);
        return Result.success();
    }
}
