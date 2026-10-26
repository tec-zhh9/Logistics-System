package com.logistics.controller;

import com.github.pagehelper.PageInfo;
import com.logistics.common.Result;
import com.logistics.entity.Admin;
import com.logistics.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员信息管理（管理员维护）
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @GetMapping("/page")
    public Result<PageInfo<Admin>> page(@RequestParam(defaultValue = "1") int pageNum,
                                        @RequestParam(defaultValue = "10") int pageSize,
                                        @RequestParam(required = false) String username) {
        return Result.success(adminService.page(pageNum, pageSize, username));
    }

    @PostMapping
    public Result<Void> add(@RequestBody Admin admin) {
        if (admin.getPassword() == null || admin.getPassword().isBlank()) {
            admin.setPassword("e10adc3949ba59abbe56e057f20f883e");   // 默认密码 123456
        } else {
            admin.setPassword(cn.hutool.crypto.SecureUtil.md5(admin.getPassword()));
        }
        adminService.add(admin);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Admin admin) {
        adminService.updateProfile(admin);
        return Result.success();
    }

    @PutMapping("/password/{id}")
    public Result<Void> resetPassword(@PathVariable Long id) {
        adminService.updatePassword(id, "e10adc3949ba59abbe56e057f20f883e");
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        adminService.delete(id);
        return Result.success();
    }
}
