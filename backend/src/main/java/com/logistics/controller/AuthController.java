package com.logistics.controller;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.jwt.JWTUtil;
import com.logistics.common.*;
import com.logistics.entity.Admin;
import com.logistics.entity.Courier;
import com.logistics.entity.User;
import com.logistics.mapper.AdminMapper;
import com.logistics.mapper.CourierMapper;
import com.logistics.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证与账户中心：登录 / 注册 / 个人信息 / 修改密码（三种角色统一入口）
 */
@RestController
@RequestMapping("/api")
public class AuthController {

    @Resource
    private AdminMapper adminMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CourierMapper courierMapper;

    /** 登录：role = ADMIN / COURIER / USER */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = SecureUtil.md5(body.get("password"));
        String role = body.get("role");

        Long id = null;
        String name = null;
        if ("ADMIN".equals(role)) {
            Admin admin = adminMapper.selectByUsername(username);
            if (admin != null && admin.getPassword().equals(password)) {
                id = admin.getId();
                name = admin.getName();
            }
        } else if ("COURIER".equals(role)) {
            Courier courier = courierMapper.selectByUsername(username);
            if (courier != null && courier.getPassword().equals(password)) {
                if ("禁用".equals(courier.getStatus())) {
                    return Result.error("该账号已被禁用，请联系管理员");
                }
                id = courier.getId();
                name = courier.getName();
            }
        } else {
            User user = userMapper.selectByUsername(username);
            if (user != null && user.getPassword().equals(password)) {
                if ("禁用".equals(user.getStatus())) {
                    return Result.error("该账号已被禁用，请联系管理员");
                }
                id = user.getId();
                name = user.getName();
            }
        }

        if (id == null) {
            return Result.error("用户名或密码错误");
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("id", id);
        payload.put("role", role);
        String token = JWTUtil.createToken(payload, JwtInterceptor.SECRET);
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("role", role);
        data.put("name", name);
        return Result.success(data);
    }

    /** 注册：role = USER / COURIER */
    @PostMapping("/register")
    public Result<Void> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String role = body.get("role");
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return Result.error("用户名和密码不能为空");
        }
        String md5 = SecureUtil.md5(password);

        if ("COURIER".equals(role)) {
            if (courierMapper.selectByUsername(username) != null) {
                return Result.error("用户名已存在");
            }
            Courier courier = new Courier();
            courier.setUsername(username);
            courier.setPassword(md5);
            courier.setName(body.get("name"));
            courier.setPhone(body.get("phone"));
            courierMapper.insert(courier);
        } else {
            if (userMapper.selectByUsername(username) != null) {
                return Result.error("用户名已存在");
            }
            User user = new User();
            user.setUsername(username);
            user.setPassword(md5);
            user.setName(body.get("name"));
            user.setPhone(body.get("phone"));
            userMapper.insert(user);
        }
        return Result.success();
    }

    /** 当前登录人个人信息 */
    @GetMapping("/profile")
    public Result<Object> profile() {
        Long id = UserContext.getUserId();
        String role = UserContext.getRole();
        if ("ADMIN".equals(role)) {
            Admin admin = adminMapper.selectById(id);
            admin.setPassword(null);
            return Result.success(admin);
        }
        if ("COURIER".equals(role)) {
            Courier courier = courierMapper.selectById(id);
            courier.setPassword(null);
            return Result.success(courier);
        }
        User user = userMapper.selectById(id);
        user.setPassword(null);
        return Result.success(user);
    }

    /** 修改个人信息 */
    @PutMapping("/profile")
    public Result<Void> updateProfile(@RequestBody Map<String, Object> body) {
        Long id = UserContext.getUserId();
        String role = UserContext.getRole();
        String name = (String) body.get("name");
        String phone = (String) body.get("phone");
        String avatar = (String) body.get("avatar");
        if ("ADMIN".equals(role)) {
            Admin admin = new Admin();
            admin.setId(id);
            admin.setName(name);
            admin.setPhone(phone);
            admin.setAvatar(avatar);
            admin.setEmail((String) body.get("email"));
            adminMapper.updateProfile(admin);
        } else if ("COURIER".equals(role)) {
            Courier courier = new Courier();
            courier.setId(id);
            courier.setName(name);
            courier.setPhone(phone);
            courier.setAvatar(avatar);
            courier.setGender((String) body.get("gender"));
            courierMapper.updateProfile(courier);
        } else {
            User user = new User();
            user.setId(id);
            user.setName(name);
            user.setPhone(phone);
            user.setAvatar(avatar);
            user.setGender((String) body.get("gender"));
            userMapper.updateProfile(user);
        }
        return Result.success();
    }

    /** 修改密码 */
    @PutMapping("/password")
    public Result<Void> updatePassword(@RequestBody Map<String, String> body) {
        Long id = UserContext.getUserId();
        String role = UserContext.getRole();
        String oldPwd = SecureUtil.md5(body.get("oldPassword"));
        String newPwd = SecureUtil.md5(body.get("newPassword"));

        if ("ADMIN".equals(role)) {
            Admin admin = adminMapper.selectById(id);
            if (!admin.getPassword().equals(oldPwd)) {
                return Result.error("原密码错误");
            }
            adminMapper.updatePassword(id, newPwd);
        } else if ("COURIER".equals(role)) {
            Courier courier = courierMapper.selectById(id);
            if (!courier.getPassword().equals(oldPwd)) {
                return Result.error("原密码错误");
            }
            courierMapper.updatePassword(id, newPwd);
        } else {
            User user = userMapper.selectById(id);
            if (!user.getPassword().equals(oldPwd)) {
                return Result.error("原密码错误");
            }
            userMapper.updatePassword(id, newPwd);
        }
        return Result.success();
    }
}
