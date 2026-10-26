package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.common.UserContext;
import com.logistics.service.StatsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 数据统计（管理员首页看板 ECharts）+ 用户/配送员首页数据
 */
@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Resource
    private StatsService statsService;

    /** 近一个月寄件订单金额（折线图） */
    @GetMapping("/order-amount")
    public Result<List<Map<String, Object>>> orderAmount() {
        return Result.success(statsService.orderAmountLast30Days());
    }

    /** 不同网点寄件订单数量（柱状图） */
    @GetMapping("/order-network")
    public Result<List<Map<String, Object>>> orderNetwork() {
        return Result.success(statsService.orderCountByNetwork());
    }

    /** 不同类别寄件数量占比（饼图） */
    @GetMapping("/order-category")
    public Result<List<Map<String, Object>>> orderCategory() {
        return Result.success(statsService.orderCountByCategory());
    }

    /** 配送员首页统计 */
    @GetMapping("/courier-home")
    public Result<Map<String, Object>> courierHome() {
        return Result.success(statsService.courierHome(UserContext.getUserId()));
    }

    /** 用户首页：热门网点 + 热门资讯 */
    @GetMapping("/user-home")
    public Result<Map<String, Object>> userHome() {
        return Result.success(statsService.userHome());
    }
}
