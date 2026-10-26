package com.logistics.controller;

import com.github.pagehelper.PageInfo;
import com.logistics.common.Result;
import com.logistics.entity.Courier;
import com.logistics.service.CourierService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 配送员管理（管理员维护）
 */
@RestController
@RequestMapping("/api/courier")
public class CourierController {

    @Resource
    private CourierService courierService;

    @GetMapping("/page")
    public Result<PageInfo<Courier>> page(@RequestParam(defaultValue = "1") int pageNum,
                                          @RequestParam(defaultValue = "10") int pageSize,
                                          @RequestParam(required = false) String username,
                                          @RequestParam(required = false) String name) {
        return Result.success(courierService.page(pageNum, pageSize, username, name));
    }

    @PostMapping
    public Result<Void> add(@RequestBody Courier courier) {
        if (courier.getPassword() == null || courier.getPassword().isBlank()) {
            courier.setPassword("e10adc3949ba59abbe56e057f20f883e");   // 默认密码 123456
        } else {
            courier.setPassword(cn.hutool.crypto.SecureUtil.md5(courier.getPassword()));
        }
        courierService.add(courier);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Courier courier) {
        courierService.updateProfile(courier);
        return Result.success();
    }

    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        courierService.updateStatus(id, status);
        return Result.success();
    }

    @PutMapping("/password/{id}")
    public Result<Void> resetPassword(@PathVariable Long id) {
        courierService.updatePassword(id, "e10adc3949ba59abbe56e057f20f883e");
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        courierService.delete(id);
        return Result.success();
    }
}
