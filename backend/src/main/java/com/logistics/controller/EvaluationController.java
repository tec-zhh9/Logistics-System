package com.logistics.controller;

import com.github.pagehelper.PageInfo;
import com.logistics.common.Result;
import com.logistics.common.UserContext;
import com.logistics.entity.Evaluation;
import com.logistics.service.EvaluationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评价管理：管理员查看全部/删除；用户管理自己的评价
 */
@RestController
@RequestMapping("/api/evaluation")
public class EvaluationController {

    @Resource
    private EvaluationService evaluationService;

    @GetMapping("/page")
    public Result<PageInfo<Evaluation>> page(@RequestParam(defaultValue = "1") int pageNum,
                                             @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(evaluationService.page(pageNum, pageSize));
    }

    @GetMapping("/my")
    public Result<List<Evaluation>> my() {
        return Result.success(evaluationService.listByUser(UserContext.getUserId()));
    }

    /** 用户对已签收订单发起评价 */
    @PostMapping
    public Result<Void> add(@RequestBody Evaluation evaluation) {
        evaluation.setUserId(UserContext.getUserId());
        evaluationService.add(evaluation);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        evaluationService.delete(id);
        return Result.success();
    }
}
