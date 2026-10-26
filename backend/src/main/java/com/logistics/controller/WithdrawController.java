package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.common.UserContext;
import com.logistics.entity.WithdrawRecord;
import com.logistics.service.WithdrawService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 提现记录：配送员发起/查看，管理员审核
 */
@RestController
@RequestMapping("/api/withdraw")
public class WithdrawController {

    @Resource
    private WithdrawService withdrawService;

    @GetMapping("/list")
    public Result<List<WithdrawRecord>> list() {
        return Result.success(withdrawService.listAll());
    }

    @GetMapping("/my")
    public Result<List<WithdrawRecord>> my() {
        return Result.success(withdrawService.listByCourier(UserContext.getUserId()));
    }

    /** 配送员发起提现 */
    @PostMapping
    public Result<Void> add(@RequestBody Map<String, Object> body) {
        BigDecimal amount = new BigDecimal(body.get("amount").toString());
        withdrawService.add(UserContext.getUserId(), amount);
        return Result.success();
    }

    /** 管理员审核：status = 已通过 / 已驳回 */
    @PutMapping("/audit/{id}")
    public Result<Void> audit(@PathVariable Long id, @RequestBody Map<String, String> body) {
        withdrawService.audit(id, body.get("status"), body.get("reason"));
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        withdrawService.delete(id);
        return Result.success();
    }
}
