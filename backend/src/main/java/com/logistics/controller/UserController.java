package com.logistics.controller;

import com.github.pagehelper.PageInfo;
import com.logistics.common.Result;
import com.logistics.entity.User;
import com.logistics.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理（管理员维护）
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/page")
    public Result<PageInfo<User>> page(@RequestParam(defaultValue = "1") int pageNum,
                                       @RequestParam(defaultValue = "10") int pageSize,
                                       @RequestParam(required = false) String username,
                                       @RequestParam(required = false) String name,
                                       @RequestParam(required = false) String phone) {
        return Result.success(userService.page(pageNum, pageSize, username, name, phone));
    }

    @PostMapping
    public Result<Void> add(@RequestBody User user) {
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            user.setPassword("e10adc3949ba59abbe56e057f20f883e");   // 默认密码 123456
        } else {
            user.setPassword(cn.hutool.crypto.SecureUtil.md5(user.getPassword()));
        }
        userService.add(user);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody User user) {
        userService.updateProfile(user);
        return Result.success();
    }

    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        userService.updateStatus(id, status);
        return Result.success();
    }

    @PutMapping("/password/{id}")
    public Result<Void> resetPassword(@PathVariable Long id) {
        userService.updatePassword(id, "e10adc3949ba59abbe56e057f20f883e");
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.success();
    }
}
