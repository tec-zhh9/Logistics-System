package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.common.UserContext;
import com.logistics.entity.CourierCert;
import com.logistics.service.CourierCertService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 配送员认证：用户申请，管理员审核（通用预约审核模型）
 */
@RestController
@RequestMapping("/api/courier-cert")
public class CourierCertController {

    @Resource
    private CourierCertService courierCertService;

    @GetMapping("/list")
    public Result<List<CourierCert>> list() {
        return Result.success(courierCertService.listAll());
    }

    @GetMapping("/my")
    public Result<List<CourierCert>> my() {
        return Result.success(courierCertService.listByUser(UserContext.getUserId()));
    }

    /** 普通用户申请认证 */
    @PostMapping
    public Result<Void> apply(@RequestBody CourierCert cert) {
        cert.setUserId(UserContext.getUserId());
        courierCertService.apply(cert);
        return Result.success();
    }

    /** 管理员审核：status = 已通过 / 已驳回 */
    @PutMapping("/audit/{id}")
    public Result<Void> audit(@PathVariable Long id, @RequestBody Map<String, String> body) {
        courierCertService.audit(id, body.get("status"), body.get("reason"));
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        courierCertService.delete(id);
        return Result.success();
    }
}
