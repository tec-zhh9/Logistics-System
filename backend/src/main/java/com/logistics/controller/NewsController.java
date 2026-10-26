package com.logistics.controller;

import com.github.pagehelper.PageInfo;
import com.logistics.common.Result;
import com.logistics.entity.News;
import com.logistics.service.NewsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 物流资讯管理
 */
@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Resource
    private NewsService newsService;

    @GetMapping("/page")
    public Result<PageInfo<News>> page(@RequestParam(defaultValue = "1") int pageNum,
                                       @RequestParam(defaultValue = "10") int pageSize,
                                       @RequestParam(required = false) String title) {
        return Result.success(newsService.page(pageNum, pageSize, title));
    }

    /** 查看详情（自动累加浏览量） */
    @GetMapping("/{id}")
    public Result<News> getById(@PathVariable Long id) {
        return Result.success(newsService.getById(id));
    }

    @PostMapping
    public Result<Void> add(@RequestBody News news) {
        newsService.add(news);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody News news) {
        newsService.update(news);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        newsService.delete(id);
        return Result.success();
    }
}
