package com.logistics.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.logistics.entity.News;
import com.logistics.mapper.NewsMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class NewsService {

    @Resource
    private NewsMapper newsMapper;

    public PageInfo<News> page(int pageNum, int pageSize, String title) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(newsMapper.selectAll(title));
    }

    /** 查看详情并累加浏览量 */
    public News getById(Long id) {
        News news = newsMapper.selectById(id);
        if (news != null) {
            newsMapper.addViewCount(id);
            news.setViewCount(news.getViewCount() + 1);
        }
        return news;
    }

    public void add(News news) {
        newsMapper.insert(news);
    }

    public void update(News news) {
        newsMapper.update(news);
    }

    public void delete(Long id) {
        newsMapper.deleteById(id);
    }
}
