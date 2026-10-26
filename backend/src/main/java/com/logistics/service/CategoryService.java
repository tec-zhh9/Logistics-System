package com.logistics.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.logistics.entity.Category;
import com.logistics.mapper.CategoryMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    public PageInfo<Category> page(int pageNum, int pageSize, String name) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(categoryMapper.selectAll(name));
    }

    public List<Category> list() {
        return categoryMapper.selectAll(null);
    }

    public void add(Category category) {
        categoryMapper.insert(category);
    }

    public void update(Category category) {
        categoryMapper.update(category);
    }

    public void delete(Long id) {
        categoryMapper.deleteById(id);
    }
}
