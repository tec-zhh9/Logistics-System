package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.common.UserContext;
import com.logistics.entity.ExpressOrder;
import com.logistics.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 寄件订单：用户下单/签收，配送员接单/送达，管理员查看全部
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    /** 管理员查看全部订单 */
    @GetMapping("/list")
    public Result<List<ExpressOrder>> list(@RequestParam(required = false) String orderNo,
                                           @RequestParam(required = false) String status) {
        return Result.success(orderService.listAll(orderNo, status));
    }

    /** 用户历史寄件 */
    @GetMapping("/my")
    public Result<List<ExpressOrder>> my() {
        return Result.success(orderService.listByUser(UserContext.getUserId()));
    }

    /** 配送员历史接单 */
    @GetMapping("/courier")
    public Result<List<ExpressOrder>> courierOrders() {
        return Result.success(orderService.listByCourier(UserContext.getUserId()));
    }

    /** 待接订单（配送员抢单池） */
    @GetMapping("/pending")
    public Result<List<ExpressOrder>> pending() {
        return Result.success(orderService.listPending());
    }

    @GetMapping("/{id}")
    public Result<ExpressOrder> getById(@PathVariable Long id) {
        return Result.success(orderService.getById(id));
    }

    /** 用户下单（自动计算运费并扣除余额） */
    @PostMapping
    public Result<Void> add(@RequestBody ExpressOrder order) {
        orderService.add(order);
        return Result.success();
    }

    /** 配送员接单 */
    @PutMapping("/accept/{id}")
    public Result<Void> accept(@PathVariable Long id) {
        orderService.accept(id);
        return Result.success();
    }

    /** 配送员送达 */
    @PutMapping("/deliver/{id}")
    public Result<Void> deliver(@PathVariable Long id) {
        orderService.deliver(id);
        return Result.success();
    }

    /** 用户签收（结算配送员收入） */
    @PutMapping("/sign/{id}")
    public Result<Void> sign(@PathVariable Long id) {
        orderService.sign(id);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return Result.success();
    }
}
