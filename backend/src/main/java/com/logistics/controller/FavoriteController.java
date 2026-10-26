package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.common.UserContext;
import com.logistics.service.FavoriteService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 我的收藏（收藏网点）
 */
@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {

    @Resource
    private FavoriteService favoriteService;

    @GetMapping("/my")
    public Result<List<Map<String, Object>>> my() {
        return Result.success(favoriteService.listByUser(UserContext.getUserId()));
    }

    @PostMapping("/{networkPointId}")
    public Result<Void> add(@PathVariable Long networkPointId) {
        favoriteService.add(UserContext.getUserId(), networkPointId);
        return Result.success();
    }

    @DeleteMapping("/{networkPointId}")
    public Result<Void> remove(@PathVariable Long networkPointId) {
        favoriteService.remove(UserContext.getUserId(), networkPointId);
        return Result.success();
    }
}
