package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.common.UserContext;
import com.logistics.entity.RechargeRecord;
import com.logistics.service.RechargeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 充值记录：用户充值/查看，管理员查看全部
 */
@RestController
@RequestMapping("/api/recharge")
public class RechargeController {

    @Resource
    private RechargeService rechargeService;

    @GetMapping("/list")
    public Result<List<RechargeRecord>> list() {
        return Result.success(rechargeService.listAll());
    }

    @GetMapping("/my")
    public Result<List<RechargeRecord>> my() {
        return Result.success(rechargeService.listByUser(UserContext.getUserId()));
    }

    /** 用户充值（模拟支付成功，余额同步增加） */
    @PostMapping
    public Result<Void> add(@RequestBody Map<String, Object> body) {
        BigDecimal amount = new BigDecimal(body.get("amount").toString());
        rechargeService.add(UserContext.getUserId(), amount);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        rechargeService.delete(id);
        return Result.success();
    }
}
