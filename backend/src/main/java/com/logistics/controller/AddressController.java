package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.common.UserContext;
import com.logistics.entity.Address;
import com.logistics.service.AddressService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 我的地址管理（普通用户）
 */
@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Resource
    private AddressService addressService;

    @GetMapping("/my")
    public Result<List<Address>> my() {
        return Result.success(addressService.listByUser(UserContext.getUserId()));
    }

    @PostMapping
    public Result<Void> add(@RequestBody Address address) {
        address.setUserId(UserContext.getUserId());
        addressService.add(address);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Address address) {
        address.setUserId(UserContext.getUserId());
        addressService.update(address);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        addressService.delete(id, UserContext.getUserId());
        return Result.success();
    }
}
