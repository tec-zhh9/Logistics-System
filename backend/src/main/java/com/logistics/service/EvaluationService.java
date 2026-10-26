package com.logistics.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.logistics.entity.Evaluation;
import com.logistics.mapper.EvaluationMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationService {

    @Resource
    private EvaluationMapper evaluationMapper;

    public PageInfo<Evaluation> page(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(evaluationMapper.selectAll());
    }

    public List<Evaluation> listByUser(Long userId) {
        return evaluationMapper.selectByUserId(userId);
    }

    public List<Evaluation> listAll() {
        return evaluationMapper.selectAll();
    }

    public void add(Evaluation evaluation) {
        evaluationMapper.insert(evaluation);
    }

    public void delete(Long id) {
        evaluationMapper.deleteById(id);
    }
}
