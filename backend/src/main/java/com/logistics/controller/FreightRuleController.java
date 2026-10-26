package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.entity.FreightRule;
import com.logistics.service.FreightRuleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 运费规则管理（按寄件类别配置首重/续重价格）
 */
@RestController
@RequestMapping("/api/freight-rule")
public class FreightRuleController {

    @Resource
    private FreightRuleService freightRuleService;

    @GetMapping("/list")
    public Result<List<FreightRule>> list() {
        return Result.success(freightRuleService.list());
    }

    @PostMapping
    public Result<Void> add(@RequestBody FreightRule rule) {
        freightRuleService.add(rule);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody FreightRule rule) {
        freightRuleService.update(rule);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        freightRuleService.delete(id);
        return Result.success();
    }
}
